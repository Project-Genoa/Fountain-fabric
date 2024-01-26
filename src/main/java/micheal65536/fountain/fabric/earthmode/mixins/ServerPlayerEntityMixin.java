package micheal65536.fountain.fabric.earthmode.mixins;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements EarthModePlayer
{
	private boolean earthMode = false;

	public boolean isEarthMode()
	{
		return this.earthMode;
	}

	public void setEarthMode(boolean earthMode)
	{
		this.earthMode = earthMode;
	}

	public void applyAbilitiesForEarthMode()
	{
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (Object) this;
		PlayerAbilities abilities = serverPlayerEntity.getAbilities();
		serverPlayerEntity.interactionManager.getGameMode().setAbilities(abilities);
		if (this.earthMode)
		{
			abilities.allowFlying = true;
			abilities.flying = true;
			abilities.creativeMode = false;
		}
	}

	@Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At(value = "HEAD"))
	private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo)
	{
		this.setEarthMode(((EarthModePlayer) oldPlayer).isEarthMode());
	}
}