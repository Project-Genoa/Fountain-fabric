package micheal65536.fountain.fabric.connector.mixins.events;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.connector.plugin.ConnectorPlugin;
import micheal65536.fountain.fabric.connector.ConnectorMinecraftServer;
import micheal65536.fountain.fabric.connector.Main;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin
{
	@Inject(method = "setupServer()Z", at = @At(value = "TAIL"))
	private void setupServer(CallbackInfoReturnable<Boolean> callbackInfo)
	{
		ConnectorPlugin connectorPlugin = ((ConnectorMinecraftServer) this).getConnectorPlugin();
		if (connectorPlugin != null)
		{
			try
			{
				connectorPlugin.onServerReady();
			}
			catch (ConnectorPlugin.ConnectorPluginException exception)
			{
				Main.LOGGER.warn("Connector plugin threw exception while handling server ready event", exception);
			}
		}
	}
}