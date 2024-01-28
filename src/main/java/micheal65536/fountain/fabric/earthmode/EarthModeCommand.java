package micheal65536.fountain.fabric.earthmode;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import micheal65536.fountain.fabric.itembehavior.EarthBlockBreakingServerPlayerInteractionManager;

public class EarthModeCommand
{
	public static final CommandRegistrationCallback COMMAND_REGISTRATION_CALLBACK = (dispatcher, registryAccess, environment) ->
	{
		dispatcher.register(CommandManager.literal("fountain:earthmode")
				.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3))
				.then(CommandManager.argument("player", EntityArgumentType.player())
						.then(CommandManager.literal("get")
								.executes(context ->
								{
									ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(context, "player");
									boolean earthMode = ((EarthModePlayer) serverPlayerEntity).isEarthMode();
									if (earthMode)
									{
										context.getSource().sendFeedback(() -> Text.literal("%s is in Earth mode".formatted(serverPlayerEntity.getName().getString())), false);
									}
									else
									{
										context.getSource().sendFeedback(() -> Text.literal("%s is not in Earth mode".formatted(serverPlayerEntity.getName().getString())), false);
									}
									return Command.SINGLE_SUCCESS;
								})
						)
						.then(CommandManager.literal("set")
								.then(CommandManager.argument("enabled", BoolArgumentType.bool())
										.executes(context ->
										{
											ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(context, "player");
											boolean earthMode = BoolArgumentType.getBool(context, "enabled");
											((EarthModePlayer) serverPlayerEntity).setEarthMode(earthMode);
											((EarthBlockBreakingServerPlayerInteractionManager) serverPlayerEntity.interactionManager).resetBlockBreaking();
											((EarthModePlayer) serverPlayerEntity).applyAbilitiesForEarthMode();
											serverPlayerEntity.sendAbilitiesUpdate();
											if (earthMode)
											{
												context.getSource().sendFeedback(() -> Text.literal("Enabled Earth mode for %s".formatted(serverPlayerEntity.getName().getString())), true);
											}
											else
											{
												context.getSource().sendFeedback(() -> Text.literal("Disabled Earth mode for %s".formatted(serverPlayerEntity.getName().getString())), true);
											}
											return Command.SINGLE_SUCCESS;
										})
								)
						)
				)
		);
	};
}