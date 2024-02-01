package micheal65536.fountain.fabric.core.itembehavior.mixins;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

// allows Earth players to pick up entities outside the world border
@Mixin(ServerPlayNetworkHandler.class)
public class EntitiesInteractOutsideWorldBorderMixin
{
	@Redirect(method = "onPlayerInteractEntity(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;contains(Lnet/minecraft/util/math/BlockPos;)Z"))
	private boolean contains(WorldBorder worldBorder, BlockPos pos)
	{
		if (((EarthModePlayer) ((ServerPlayNetworkHandler) (Object) this).getPlayer()).isEarthMode())
		{
			return true;
		}
		else
		{
			return worldBorder.contains(pos);
		}
	}
}