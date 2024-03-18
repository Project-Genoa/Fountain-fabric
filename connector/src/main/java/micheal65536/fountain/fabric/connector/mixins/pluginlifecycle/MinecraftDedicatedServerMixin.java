package micheal65536.fountain.fabric.connector.mixins.pluginlifecycle;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.connector.plugin.ConnectorPlugin;
import micheal65536.fountain.fabric.connector.ConnectorMinecraftServer;
import micheal65536.fountain.fabric.connector.ConnectorPluginLogger;
import micheal65536.fountain.fabric.connector.ConnectorServerPropertiesHandler;
import micheal65536.fountain.fabric.connector.Main;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin implements ConnectorMinecraftServer
{
	private ConnectorPlugin connectorPlugin = null;

	@Inject(method = "setupServer()Z", at = @At(value = "HEAD"), cancellable = true)
	private void setupServer(CallbackInfoReturnable<Boolean> callbackInfo)
	{
		ConnectorServerPropertiesHandler connectorServerPropertiesHandler = (ConnectorServerPropertiesHandler) ((MinecraftDedicatedServer) (Object) this).getProperties();
		String jarFilename = connectorServerPropertiesHandler.getConnectorPluginJarFilename();
		String className = connectorServerPropertiesHandler.getConnectorPluginClassName();
		String arg = connectorServerPropertiesHandler.getConnectorPluginArg();

		if (jarFilename.isEmpty() || className.isEmpty())
		{
			Main.LOGGER.warn("Connector plugin is not configured, skipping");
			return;
		}

		ClassLoader classLoader;
		try
		{
			classLoader = new URLClassLoader(new URL[]{new File(jarFilename).toURI().toURL()}, this.getClass().getClassLoader());
		}
		catch (MalformedURLException exception)
		{
			throw new AssertionError(exception);
		}

		ConnectorPlugin connectorPlugin;
		try
		{
			Class<?> aClass = classLoader.loadClass(className);
			if (!ConnectorPlugin.class.isAssignableFrom(aClass))
			{
				Main.LOGGER.error("Connector plugin class does not implement connector plugin interface");
				callbackInfo.setReturnValue(false);
				callbackInfo.cancel();
				return;
			}
			Class<ConnectorPlugin> connectorPluginClass = (Class<ConnectorPlugin>) aClass;

			Constructor<ConnectorPlugin> connectorPluginConstructor = connectorPluginClass.getDeclaredConstructor();
			connectorPlugin = connectorPluginConstructor.newInstance();
		}
		catch (NoClassDefFoundError | ClassNotFoundException exception)
		{
			Main.LOGGER.error("Connector plugin class was not found or could not be loaded", exception);
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
			return;
		}
		catch (NoSuchMethodException exception)
		{
			Main.LOGGER.error("Connector plugin class does not provide a suitable constructor");
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
			return;
		}
		catch (ReflectiveOperationException exception)
		{
			Main.LOGGER.error("Could not create connector plugin instance", exception);
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
			return;
		}

		try
		{
			connectorPlugin.init(arg, new ConnectorPluginLogger(LoggerFactory.getLogger("fountain-connector (plugin)")));
		}
		catch (ConnectorPlugin.ConnectorPluginException exception)
		{
			Main.LOGGER.error("Connector plugin failed to initialise", exception);
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
			return;
		}
		this.connectorPlugin = connectorPlugin;
		Main.LOGGER.info("Connector plugin is initialised");
	}

	@Inject(method = "shutdown()V", at = @At(value = "TAIL"))
	private void shutdown(CallbackInfo callbackInfo)
	{
		if (this.connectorPlugin != null)
		{
			Main.LOGGER.info("Shutting down connector plugin");
			try
			{
				this.connectorPlugin.shutdown();
			}
			catch (ConnectorPlugin.ConnectorPluginException exception)
			{
				Main.LOGGER.warn("Connector plugin threw exception while shutting down", exception);
			}
			this.connectorPlugin = null;
		}
	}

	@Override
	@Nullable
	public ConnectorPlugin getConnectorPlugin()
	{
		return this.connectorPlugin;
	}
}