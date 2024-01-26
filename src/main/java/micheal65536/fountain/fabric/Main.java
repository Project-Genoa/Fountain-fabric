package micheal65536.fountain.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import micheal65536.fountain.fabric.earthmode.EarthModeCommand;

public class Main implements DedicatedServerModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("fountain");

	@Override
	public void onInitializeServer()
	{
		CommandRegistrationCallback.EVENT.register(EarthModeCommand.COMMAND_REGISTRATION_CALLBACK);
	}
}