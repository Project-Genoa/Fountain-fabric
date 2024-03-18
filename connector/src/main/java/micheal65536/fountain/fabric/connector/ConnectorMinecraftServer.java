package micheal65536.fountain.fabric.connector;

import org.jetbrains.annotations.Nullable;

import micheal65536.fountain.connector.plugin.ConnectorPlugin;

public interface ConnectorMinecraftServer
{
	@Nullable
	ConnectorPlugin getConnectorPlugin();
}