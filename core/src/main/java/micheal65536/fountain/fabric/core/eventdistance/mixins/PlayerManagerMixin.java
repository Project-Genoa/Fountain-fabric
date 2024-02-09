package micheal65536.fountain.fabric.core.eventdistance.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

import java.util.List;

// makes sure that distance-limited packets (e.g. sounds) are always sent to Earth players
@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@Inject(method = "sendToAround(Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/registry/RegistryKey;Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "HEAD"), cancellable = true)
	private void sendToAround(PlayerEntity fromPlayer, double x, double y, double z, double distance, RegistryKey<World> worldRegistryKey, Packet<?> packet, CallbackInfo callbackInfo)
	{
		for (ServerPlayerEntity player : ((PlayerManagerAccessor) this).getPlayers())
		{
			boolean isEarthMode = ((EarthModePlayer) player).isEarthMode();
			if (player == fromPlayer && !isEarthMode)
			{
				continue;
			}
			if (player.getWorld().getRegistryKey() != worldRegistryKey)
			{
				continue;
			}
			if (!isEarthMode)
			{
				double dx = x - player.getX();
				double dy = y - player.getY();
				double dz = z - player.getZ();
				double dSquared = dx * dx + dy * dy + dz * dz;
				if (dSquared >= distance * distance)
				{
					continue;
				}
			}
			player.networkHandler.sendPacket(packet);
		}
		callbackInfo.cancel();
	}

	@Mixin(PlayerManager.class)
	public interface PlayerManagerAccessor
	{
		@Accessor("players")
		List<ServerPlayerEntity> getPlayers();
	}
}