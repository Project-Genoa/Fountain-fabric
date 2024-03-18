package micheal65536.fountain.fabric.connector.mixins.pluginlifecycle;

import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.connector.ConnectorServerPropertiesHandler;

import java.util.Properties;
import java.util.function.Function;

@Mixin(ServerPropertiesHandler.class)
public class ServerPropertiesHandlerMixin implements ConnectorServerPropertiesHandler
{
	private String connectorPluginJarFilename;
	private String connectorPluginClassName;
	private String connectorPluginArg;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(Properties properties, CallbackInfo callbackInfo)
	{
		this.connectorPluginJarFilename = ((AbstractPropertiesHandlerAccessor) this).callGetString("fountain-connector-plugin-jar", "");
		this.connectorPluginClassName = ((AbstractPropertiesHandlerAccessor) this).callGetString("fountain-connector-plugin-class", "");
		this.connectorPluginArg = ((AbstractPropertiesHandlerAccessor) this).callGetString("fountain-connector-plugin-arg", "");
	}

	@Mixin(AbstractPropertiesHandler.class)
	public interface AbstractPropertiesHandlerAccessor
	{
		@Invoker("get")
		<T> T callGet(String key, Function<String, T> parser, T defaultValue);

		@Invoker("getString")
		String callGetString(String key, String defaultValue);
	}

	@Override
	public String getConnectorPluginJarFilename()
	{
		return this.connectorPluginJarFilename;
	}

	@Override
	public String getConnectorPluginClassName()
	{
		return this.connectorPluginClassName;
	}

	@Override
	public String getConnectorPluginArg()
	{
		return this.connectorPluginArg;
	}
}