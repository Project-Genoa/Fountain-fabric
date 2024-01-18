package micheal65536.fountain.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements DedicatedServerModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("fountain");

	@Override
	public void onInitializeServer()
	{
		LOGGER.info("Fountain is installed");
	}
}