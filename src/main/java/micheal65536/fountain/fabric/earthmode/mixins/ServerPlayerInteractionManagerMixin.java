package micheal65536.fountain.fabric.earthmode.mixins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	// ensure that the correct player abilities are set for Earth mode, after setGameMode has overwritten them with the game mode abilities
	@Inject(method = "setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V", at = @At(value = "TAIL"))
	private void setGameMode(GameMode gameMode, GameMode previousGameMode, CallbackInfo callbackInfo)
	{
		ServerPlayerInteractionManager serverPlayerInteractionManager = (ServerPlayerInteractionManager) (Object) this;
		((EarthModePlayer) ((ServerPlayerInteractionManagerAccessor) serverPlayerInteractionManager).getPlayer()).applyAbilitiesForEarthMode();
	}

	@Mixin(ServerPlayerInteractionManager.class)
	public interface ServerPlayerInteractionManagerAccessor
	{
		@Accessor("player")
		ServerPlayerEntity getPlayer();
	}
}