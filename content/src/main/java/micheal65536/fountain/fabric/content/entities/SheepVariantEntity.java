package micheal65536.fountain.fabric.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SheepVariantEntity extends SheepEntity
{
	public static EntityType.EntityFactory<SheepVariantEntity> createFactory(boolean shearable, @Nullable DyeColor fixedColor)
	{
		return (entityType, world) -> new SheepVariantEntity(entityType, world, shearable, fixedColor);
	}

	public final boolean shearable;
	public final boolean dyeable;
	private final DyeColor fixedColor;

	public SheepVariantEntity(EntityType<? extends SheepEntity> entityType, World world, boolean shearable, @Nullable DyeColor fixedColor)
	{
		super(entityType, world);

		this.shearable = shearable;

		if (fixedColor != null)
		{
			this.dyeable = false;
			this.fixedColor = fixedColor;
		}
		else
		{
			this.dyeable = true;
			this.fixedColor = null;
		}
	}

	@Override
	public DyeColor getColor()
	{
		return this.fixedColor != null ? this.fixedColor : super.getColor();
	}

	@Override
	public void setColor(DyeColor color)
	{
		super.setColor(this.fixedColor != null ? this.fixedColor : color);
	}

	@Override
	public boolean isSheared()
	{
		return this.shearable ? super.isSheared() : false;
	}

	@Override
	public void setSheared(boolean sheared)
	{
		super.setSheared(this.shearable ? sheared : false);
	}

	@Override
	public boolean isShearable()
	{
		return this.shearable;
	}

	@Override
	public Identifier getLootTableId()
	{
		return this.shearable ? super.getLootTableId() : this.getType().getLootTableId();
	}
}