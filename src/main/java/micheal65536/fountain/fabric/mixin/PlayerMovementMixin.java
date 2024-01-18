package micheal65536.fountain.fabric.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// disables force-teleport when player moves too quickly or goes inside a block
@Mixin(ServerPlayNetworkHandler.class)
public class PlayerMovementMixin
{
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isInTeleportationState()Z"), method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V")
	private boolean onPlayerMoveInject1(ServerPlayerEntity serverPlayerEntity)
	{
		return true;
	}

	@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;noClip:Z"), method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V")
	private boolean onPlayerMoveInject2(ServerPlayerEntity serverPlayerEntity)
	{
		return true;
	}
}