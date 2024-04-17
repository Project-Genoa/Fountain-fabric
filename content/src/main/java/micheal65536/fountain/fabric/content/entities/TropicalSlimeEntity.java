package micheal65536.fountain.fabric.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;

public class TropicalSlimeEntity extends SlimeEntity
{
	public TropicalSlimeEntity(EntityType<? extends TropicalSlimeEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Override
	public void setSize(int size, boolean heal)
	{
		super.setSize(2, heal);
	}

	// TODO: implement behavior - tropical slime is supposed to be passive until attacked
}