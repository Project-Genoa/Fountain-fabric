package micheal65536.fountain.fabric.core.mobdespawn;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class MobDespawnGamerule
{
	public static GameRules.Key<GameRules.BooleanRule> DO_MOB_DESPAWN;

	public static void register()
	{
		DO_MOB_DESPAWN = GameRuleRegistry.register("fountain:doMobDespawn", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));
	}
}