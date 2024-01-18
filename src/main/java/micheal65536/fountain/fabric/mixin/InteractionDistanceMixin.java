package micheal65536.fountain.fabric.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// removes limit on how far away the player can interact
public class InteractionDistanceMixin
{
	@Mixin({ServerPlayNetworkHandler.class, ServerPlayerInteractionManager.class})
	public static class InteractionDistanceMixin1
	{
		@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"), method = {"*"})
		private double maxBreakSquaredDistanceInject()
		{
			return Double.MAX_VALUE;
		}
	}

	@Mixin({ServerPlayNetworkHandler.class})
	public static class InteractionDistanceMixin2
	{
		@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;squaredDistanceTo(DDD)D"), method = "onPlayerInteractBlock(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V")
		private double squaredDistanceToInject(ServerPlayerEntity serverPlayerEntity, double x, double y, double z)
		{
			return 0.0;
		}
	}
}