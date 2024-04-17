package micheal65536.fountain.fabric.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;

public class GenoaSlimeEntity extends SlimeEntity
{
	private final int size;

	public GenoaSlimeEntity(EntityType<? extends GenoaSlimeEntity> entityType, World world, int size)
	{
		super(entityType, world);
		this.size = size;
	}

	@Override
	public void setSize(int size, boolean heal)
	{
		super.setSize(this.size, heal);
	}
}