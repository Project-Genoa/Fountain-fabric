package micheal65536.fountain.fabric.content.entities.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DefaultAttributeRegistry.class)
public interface DefaultAttributeRegistryAccessor
{
	@Accessor("DEFAULT_ATTRIBUTE_REGISTRY")
	static Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer> getMap()
	{
		throw new AssertionError();
	}
}