package micheal65536.fountain.fabric.mixin;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PowderSnowBucketItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// causes items to be consumed and harvested when in creative mode
public class CreativeModeItemBehaviorMixin
{
	@Mixin({
			AbstractSignBlock.class,
			BarrierBlock.class,
			BedBlock.class,
			BeehiveBlock.class,
			CakeBlock.class,
			//ChiseledBookshelfBlock.class,
			DecoratedPotBlock.class,
			DoorBlock.class,
			ShulkerBoxBlock.class,
			TallPlantBlock.class,
			TntBlock.class,
			PowderSnowBucketItem.class,
			SwordItem.class,
			TridentItem.class
	})
	public static class CreativeModeItemBehaviorMixin1
	{
		@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isCreative()Z"), method = "*")
		private boolean isCreative(PlayerEntity playerEntity)
		{
			return false;
		}
	}

	@Mixin(ServerPlayerInteractionManager.class)
	public static class CreativeModeItemBehaviorMixin2
	{
		@Overwrite
		public boolean isCreative()
		{
			return false;
		}

		@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;isCreative()Z"), method = "processBlockBreakingAction(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket$Action;Lnet/minecraft/util/math/Direction;II)V")
		public boolean isCreative1(ServerPlayerInteractionManager serverPlayerInteractionManager, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence)
		{
			return serverPlayerInteractionManager.getGameMode().isCreative();
		}
	}

	@Mixin({PlayerAbilities.class, GameMode.class})
	public static class CreativeModeItemBehaviorMixin3
	{
		@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z", opcode = Opcodes.PUTFIELD), method = "*")
		public void forceDisableCreativeModeAbility(PlayerAbilities playerAbilities, boolean creativeMode)
		{
			playerAbilities.creativeMode = false;
		}
	}
}