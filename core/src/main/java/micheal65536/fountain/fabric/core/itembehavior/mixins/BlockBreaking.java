package micheal65536.fountain.fabric.core.itembehavior.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;
import micheal65536.fountain.fabric.core.itembehavior.EarthBlockBreakingServerPlayerInteractionManager;

// rewrites block breaking to work in terms of multiple "hits" rather than dig duration, as the Earth client sends hits and not a start/stop event
// TODO: should we reset break progress after some timeout (otherwise the last block to be hit remains partially broken forever if the user never finishes breaking it or starts breaking another block)?
public class BlockBreaking
{
	private static final int EQUIVALENT_TICKS_PER_HIT = 5; // Earth client sends block hit approximately 4 times per second, so we treat each hit as equivalent to 5 ticks of mining

	@Mixin(ServerPlayerInteractionManager.class)
	public static class ServerPlayerInteractionManagerMixin implements EarthBlockBreakingServerPlayerInteractionManager
	{
		private boolean earthMining = false;
		private BlockPos earthMiningPos;
		private int earthMiningHitCount;
		private int earthMiningBlockStateId;

		@Inject(method = "processBlockBreakingAction(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket$Action;Lnet/minecraft/util/math/Direction;II)V", at = @At(value = "HEAD"), cancellable = true)
		private void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo callbackInfo)
		{
			ServerPlayerInteractionManager serverPlayerInteractionManager = (ServerPlayerInteractionManager) (Object) this;
			ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) serverPlayerInteractionManager).getPlayer();
			if (((EarthModePlayer) player).isEarthMode())
			{
				if (pos.getY() < worldHeight)
				{
					if (action == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK)
					{
						this.processEarthBlockBreaking(serverPlayerInteractionManager, player, pos, direction, sequence);
					}
				}
				callbackInfo.cancel();
			}
		}

		@Inject(method = "update()V", at = @At(value = "TAIL"))
		private void update(CallbackInfo callbackInfo)
		{
			if (this.earthMining)
			{
				ServerWorld world = ((ServerPlayerInteractionManagerAccessor) this).getWorld();
				BlockState blockState = world.getBlockState(this.earthMiningPos);
				if (blockState.isAir() || Block.getRawIdFromState(blockState) != this.earthMiningBlockStateId)    // TODO: what is the canonical way to check if a block has been "changed"?
				{
					this.earthMining = false;
					world.setBlockBreakingInfo(((ServerPlayerInteractionManagerAccessor) this).getPlayer().getId(), this.earthMiningPos, -1);
				}
			}
		}

		private void processEarthBlockBreaking(ServerPlayerInteractionManager serverPlayerInteractionManager, ServerPlayerEntity player, BlockPos pos, Direction direction, int sequence)
		{
			ServerWorld world = ((ServerPlayerInteractionManagerAccessor) serverPlayerInteractionManager).getWorld();
			BlockState blockState = world.getBlockState(pos);
			if (!world.canPlayerModifyAt(player, pos))
			{
				player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, blockState));
				return;
			}

			if (serverPlayerInteractionManager.isCreative())
			{
				serverPlayerInteractionManager.finishMining(pos, sequence, "earth creative destroy");
			}
			else
			{
				if (this.earthMining && !pos.equals(this.earthMiningPos))
				{
					this.earthMining = false;
					world.setBlockBreakingInfo(player.getId(), this.earthMiningPos, -1);
				}

				if (!this.earthMining)
				{
					if (player.isBlockBreakingRestricted(world, pos, serverPlayerInteractionManager.getGameMode()))
					{
						player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, blockState));
						return;
					}
					if (blockState.isAir())
					{
						return;
					}

					this.earthMining = true;
					this.earthMiningPos = pos.toImmutable();
					this.earthMiningHitCount = 1;
					this.earthMiningBlockStateId = Block.getRawIdFromState(blockState);

					blockState.onBlockBreakStart(world, pos, player);
				}
				else
				{
					if (blockState.isAir() || Block.getRawIdFromState(blockState) != this.earthMiningBlockStateId)    // TODO: what is the canonical way to check if a block has been "changed"?
					{
						this.earthMining = false;
						world.setBlockBreakingInfo(player.getId(), pos, -1);
						return;
					}

					this.earthMiningHitCount++;
				}

				if (this.earthMining)
				{
					float damagePerTick = blockState.calcBlockBreakingDelta(player, world, pos);
					float damage = damagePerTick * this.earthMiningHitCount * EQUIVALENT_TICKS_PER_HIT;
					if (damage >= 1.0f)
					{
						this.earthMining = false;
						world.setBlockBreakingInfo(player.getId(), pos, -1);
						serverPlayerInteractionManager.finishMining(pos, sequence, "earth survival destroy");
					}
					else
					{
						world.setBlockBreakingInfo(player.getId(), pos, (int) (damage * 10.0f));
					}
				}
			}
		}

		public void resetBlockBreaking()
		{
			if (((ServerPlayerInteractionManagerAccessor) (Object) this).getMining() || ((ServerPlayerInteractionManagerAccessor) (Object) this).getFailedToMine())
			{
				((ServerPlayerInteractionManagerAccessor) (Object) this).getWorld().setBlockBreakingInfo(((ServerPlayerInteractionManagerAccessor) (Object) this).getPlayer().getId(), ((ServerPlayerInteractionManagerAccessor) (Object) this).getMiningPos(), -1);
				((ServerPlayerInteractionManagerAccessor) (Object) this).setMining(false);
				((ServerPlayerInteractionManagerAccessor) (Object) this).setFailedToMine(false);
			}

			if (this.earthMining)
			{
				((ServerPlayerInteractionManagerAccessor) (Object) this).getWorld().setBlockBreakingInfo(((ServerPlayerInteractionManagerAccessor) (Object) this).getPlayer().getId(), this.earthMiningPos, -1);
				this.earthMining = false;
			}
		}
	}

	@Mixin(ServerPlayerInteractionManager.class)
	public interface ServerPlayerInteractionManagerAccessor
	{
		@Accessor("player")
		ServerPlayerEntity getPlayer();

		@Accessor("world")
		ServerWorld getWorld();

		@Accessor("mining")
		void setMining(boolean mining);

		@Accessor("failedToMine")
		void setFailedToMine(boolean failedToMine);

		@Accessor("mining")
		boolean getMining();

		@Accessor("failedToMine")
		boolean getFailedToMine();

		@Accessor("miningPos")
		BlockPos getMiningPos();
	}

	@Mixin(PlayerEntity.class)
	public static class PlayerEntityMixin
	{
		@Redirect(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z"))
		private boolean isOnGround(PlayerEntity playerEntity)
		{
			if (((EarthModePlayer) playerEntity).isEarthMode())
			{
				return true;
			}
			else
			{
				return playerEntity.isOnGround();
			}
		}
	}

	@Mixin(ServerWorld.class)
	public static class ServerWorldMixin
	{
		// always send block breaking to Earth players even if it's their own block break
		@Redirect(method = "setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getId()I"))
		private int getId(ServerPlayerEntity serverPlayerEntity, @Local(ordinal = 0) int entityId)
		{
			if (((EarthModePlayer) serverPlayerEntity).isEarthMode())
			{
				return entityId + 1;
			}
			else
			{
				return serverPlayerEntity.getId();
			}
		}
	}
}