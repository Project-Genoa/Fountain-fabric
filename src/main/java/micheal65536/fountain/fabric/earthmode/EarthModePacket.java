package micheal65536.fountain.fabric.earthmode;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import micheal65536.fountain.fabric.Main;
import micheal65536.fountain.fabric.itembehavior.EarthBlockBreakingServerPlayerInteractionManager;

public class EarthModePacket
{
	public static final Identifier FOUNTAIN_EARTH_MODE_PACKET_ID = new Identifier("fountain", "earth_mode");

	public static final ServerPlayNetworking.PlayChannelHandler CHANNEL_HANDLER = (server, player, handler, buf, responseSender) ->
	{
		server.submit(() ->
		{
			boolean earthMode = buf.readBoolean();
			((EarthModePlayer) player).setEarthMode(earthMode);
			((EarthBlockBreakingServerPlayerInteractionManager) player.interactionManager).resetBlockBreaking();
			((EarthModePlayer) player).applyAbilitiesForEarthMode();
			player.sendAbilitiesUpdate();
			if (earthMode)
			{
				Main.LOGGER.info("Enabled Earth mode for %s (requested by channel message)".formatted(player.getName().getString()));
			}
			else
			{
				Main.LOGGER.info("Disabled Earth mode for %s (requested by channel message)".formatted(player.getName().getString()));
			}
		});
	};
}