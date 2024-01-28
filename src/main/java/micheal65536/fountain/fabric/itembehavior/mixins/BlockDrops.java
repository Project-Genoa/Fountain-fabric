package micheal65536.fountain.fabric.itembehavior.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;
import micheal65536.fountain.fabric.itembehavior.EarthItemPickup;

// makes blocks give drops in creative mode for Earth players, and puts items directly into the player's inventory rather than dropping them on the ground
public class BlockDrops
{
	@Mixin(ServerPlayerInteractionManager.class)
	public static class ServerPlayerInteractionManagerMixin
	{
		@Inject(method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;isCreative()Z"), cancellable = true)
		private void tryBreakBlockEarthMode(CallbackInfoReturnable<Boolean> callbackInfo, @Local(name = "pos") BlockPos pos, @Local(name = "block") Block block, @Local(name = "blockState2") BlockState blockState, @Local(name = "blockEntity") BlockEntity blockEntity, @Local(name = "bl") boolean isRemoved)
		{
			ServerPlayerEntity player = ((ServerPlayerInteractionManagerAccessor) this).getPlayer();
			if (((EarthModePlayer) player).isEarthMode())
			{
				ServerWorld world = ((ServerPlayerInteractionManagerAccessor) this).getWorld();
				if (((ServerPlayerInteractionManager) (Object) this).isCreative())
				{
					ItemStack itemStack = block.getPickStack(world, pos, blockState);
					EarthItemPickup.giveItemToEarthPlayer(player, itemStack, pos.toCenterPos());
				}
				else
				{
					ItemStack itemStack = player.getMainHandStack();
					ItemStack itemStack1 = itemStack.copy();
					boolean canHarvest = player.canHarvest(blockState);
					itemStack.postMine(world, blockState, pos, player);
					if (isRemoved && canHarvest)
					{
						block.afterBreak(world, player, pos, blockState, blockEntity, itemStack1);
					}
				}

				callbackInfo.setReturnValue(true);
				callbackInfo.cancel();
			}
		}
	}

	@Mixin(Block.class)
	public static class BlockMixin
	{
		@Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "HEAD"), cancellable = true)
		private static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfo callbackInfo)
		{
			if (world instanceof ServerWorld serverWorld && entity != null && entity instanceof ServerPlayerEntity serverPlayerEntity && ((EarthModePlayer) serverPlayerEntity).isEarthMode())
			{
				Block.getDroppedStacks(state, serverWorld, pos, blockEntity, entity, tool).forEach(itemStack ->
				{
					EarthItemPickup.giveItemToEarthPlayer(serverPlayerEntity, itemStack, pos.toCenterPos());
				});
				state.onStacksDropped(serverWorld, pos, tool, false);
				callbackInfo.cancel();
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
	}
}