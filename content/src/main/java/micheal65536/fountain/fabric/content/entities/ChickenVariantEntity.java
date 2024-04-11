package micheal65536.fountain.fabric.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.World;

public class ChickenVariantEntity extends ChickenEntity
{
	public ChickenVariantEntity(EntityType<? extends ChickenEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Override
	public void tickMovement()
	{
		this.eggLayTime = 6000; // prevent chicken variants from laying eggs
		super.tickMovement();
	}
}