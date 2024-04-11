package micheal65536.fountain.fabric.content.entities.mixins;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityType.class)
public interface EntityTypeAccessor<T extends Entity>
{
	@Accessor("factory")
	EntityType.EntityFactory<T> getFactory();

	@Accessor("canSpawnInside")
	ImmutableSet<Block> getCanSpawnInside();

	@Accessor("lootTableId")
	void setLootTableId(Identifier lootTableId);
}