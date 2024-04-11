package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import micheal65536.fountain.fabric.content.entities.model.SheepVariantEntityModel;

public class SheepVariantEntityRenderer extends SheepEntityRenderer
{
	private final Identifier texture;

	public SheepVariantEntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);

		this.model = new SheepVariantEntityModel<>(context.getPart(SheepVariantEntityModel.ENTITY_MODEL_LAYER));

		FeatureRenderer<?, ?> featureRenderer = this.features.remove(this.features.size() - 1);
		if (!(featureRenderer instanceof SheepWoolFeatureRenderer))
		{
			throw new AssertionError();
		}
		this.addFeature(new SheepVariantWoolFeatureRenderer(this, context.getModelLoader()));

		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(SheepEntity sheepEntity)
	{
		return this.texture;
	}
}