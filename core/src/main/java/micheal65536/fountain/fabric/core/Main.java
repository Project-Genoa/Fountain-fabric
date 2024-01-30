package micheal65536.fountain.fabric.core;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import micheal65536.fountain.fabric.core.earthmode.EarthModeCommand;
import micheal65536.fountain.fabric.core.earthmode.EarthModePacket;

public class Main implements DedicatedServerModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("fountain-core");

	@Override
	public void onInitializeServer()
	{
		CommandRegistrationCallback.EVENT.register(EarthModeCommand.COMMAND_REGISTRATION_CALLBACK);
		ServerPlayNetworking.registerGlobalReceiver(EarthModePacket.FOUNTAIN_EARTH_MODE_PACKET_ID, EarthModePacket.CHANNEL_HANDLER);
	}
}