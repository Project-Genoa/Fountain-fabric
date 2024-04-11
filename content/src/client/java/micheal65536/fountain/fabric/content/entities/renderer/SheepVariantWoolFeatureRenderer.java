package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;

import micheal65536.fountain.fabric.content.entities.SheepVariantEntity;
import micheal65536.fountain.fabric.content.entities.model.SheepVariantWoolEntityModel;

public class SheepVariantWoolFeatureRenderer extends FeatureRenderer<SheepEntity, SheepEntityModel<SheepEntity>>
{
	private final SheepVariantWoolEntityModel<SheepEntity> model;

	public SheepVariantWoolFeatureRenderer(FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>> context, EntityModelLoader loader)
	{
		super(context);
		this.model = new SheepVariantWoolEntityModel<>(loader.getModelPart(SheepVariantWoolEntityModel.ENTITY_MODEL_LAYER));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SheepEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if (entity.isSheared())
		{
			return;
		}

		DyeColor dyeColor = (entity instanceof SheepVariantEntity && !((SheepVariantEntity) entity).dyeable) ? null : entity.getColor();
		float[] rgb = dyeColor != null ? SheepEntity.getRgbColor(dyeColor) : new float[]{1.0f, 1.0f, 1.0f};

		FeatureRenderer.render(this.getContextModel(), this.model, this.getTexture(entity), matrices, vertexConsumers, light, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, tickDelta, rgb[0], rgb[1], rgb[2]);
	}
}