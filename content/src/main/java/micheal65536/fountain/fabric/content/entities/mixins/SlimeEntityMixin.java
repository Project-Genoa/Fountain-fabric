package micheal65536.fountain.fabric.content.entities.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.content.Main;
import micheal65536.fountain.fabric.content.entities.GenoaSlimeEntity;
import micheal65536.fountain.fabric.content.entities.TropicalSlimeEntity;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin
{
	// prevents tropical slime from spawning smaller slimes when killed
	@Redirect(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;getSize()I"))
	private int getSize(SlimeEntity slimeEntity)
	{
		if (((SlimeEntity) (Object) this) instanceof TropicalSlimeEntity)
		{
			return 0;
		}
		else
		{
			return ((SlimeEntity) (Object) this).getSize();
		}
	}

	// handle Genoa slime size decrement
	@Redirect(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;getType()Lnet/minecraft/entity/EntityType;"))
	private EntityType<? extends SlimeEntity> getType(SlimeEntity slimeEntity)
	{
		if (((SlimeEntity) (Object) this) instanceof GenoaSlimeEntity genoaSlimeEntity)
		{
			return switch (genoaSlimeEntity.getSize())
			{
				case 2 -> (EntityType<GenoaSlimeEntity>) Registries.ENTITY_TYPE.get(new Identifier("genoa", "genoa_slime_quarter"));
				case 4 -> (EntityType<GenoaSlimeEntity>) Registries.ENTITY_TYPE.get(new Identifier("genoa", "genoa_slime_half"));
				default ->
				{
					Main.LOGGER.warn("Cannot decrement Genoa slime size {}", genoaSlimeEntity.getSize());
					yield (EntityType<GenoaSlimeEntity>) Registries.ENTITY_TYPE.get(new Identifier("genoa", "genoa_slime_quarter"));
				}
			};
		}
		else
		{
			return ((SlimeEntity) (Object) this).getType();
		}
	}
}