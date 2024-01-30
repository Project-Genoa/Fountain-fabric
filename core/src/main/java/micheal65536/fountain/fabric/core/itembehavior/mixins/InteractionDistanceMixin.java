package micheal65536.fountain.fabric.core.itembehavior.mixins;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

// removes limit on maximum block and entity interaction distance for Earth players
// there is no need to remove the limit for block breaking because block breaking has been completely rewritten anyway
@Mixin(ServerPlayNetworkHandler.class)
public class InteractionDistanceMixin
{
	@Redirect(method = {"onPlayerInteractBlock(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V", "onPlayerInteractEntity(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V"}, at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
	private double maxBreakSquaredDistance()
	{
		if (((EarthModePlayer) ((ServerPlayNetworkHandler) (Object) this).getPlayer()).isEarthMode())
		{
			return Double.MAX_VALUE;
		}
		else
		{
			return ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
		}
	}

	@Redirect(method = "onPlayerInteractBlock(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;squaredDistanceTo(DDD)D"))
	private double squaredDistanceTo(ServerPlayerEntity serverPlayerEntity, double x, double y, double z)
	{
		if (((EarthModePlayer) serverPlayerEntity).isEarthMode())
		{
			return 0.0;
		}
		else
		{
			return serverPlayerEntity.squaredDistanceTo(x, y, z);
		}
	}
}