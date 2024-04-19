package micheal65536.fountain.fabric.core.mobdespawn.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.core.mobdespawn.MobDespawnGamerule;

@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
	@Redirect(method = "method_31420(Lnet/minecraft/world/tick/TickManager;Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;checkDespawn()V"))
	private void checkDespawn(Entity entity)
	{
		if (((ServerWorld) (Object) this).getGameRules().getBoolean(MobDespawnGamerule.DO_MOB_DESPAWN))
		{
			entity.checkDespawn();
		}
	}
}