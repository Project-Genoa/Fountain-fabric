package micheal65536.fountain.fabric.core.earthmode.mixins;

import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	// prevent Earth players from changing flying mode
	@Inject(method = "onUpdatePlayerAbilities(Lnet/minecraft/network/packet/c2s/play/UpdatePlayerAbilitiesC2SPacket;)V", at = @At(value = "HEAD"), cancellable = true)
	private void onUpdatePlayerAbilities(UpdatePlayerAbilitiesC2SPacket updatePlayerAbilitiesC2SPacket, CallbackInfo callbackInfo)
	{
		ServerPlayNetworkHandler serverPlayNetworkHandler = (ServerPlayNetworkHandler) (Object) this;
		NetworkThreadUtils.forceMainThread(updatePlayerAbilitiesC2SPacket, serverPlayNetworkHandler, serverPlayNetworkHandler.player.getServerWorld());
		if (((EarthModePlayer) serverPlayNetworkHandler.player).isEarthMode())
		{
			callbackInfo.cancel();
		}
	}
}