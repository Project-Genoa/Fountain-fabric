package micheal65536.fountain.fabric.content.entities.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

public class SheepVariantWoolEntityModel<T extends SheepEntity> extends SheepWoolEntityModel<T>
{
	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(new Identifier("fountain", "sheep_v2_fur"), "main");

	public static void register()
	{
		EntityModelLayerRegistry.registerModelLayer(ENTITY_MODEL_LAYER, SheepVariantWoolEntityModel::getTexturedModelData);
	}

	public SheepVariantWoolEntityModel(ModelPart root)
	{
		super(root);
	}

	public static TexturedModelData getTexturedModelData()
	{
		ModelData modelData = new ModelData();

		ModelPartData root = modelData.getRoot();

		root.addChild(EntityModelPartNames.HEAD,
				ModelPartBuilder.create()
						.uv(0, 16).cuboid(-3.0f, -4.0f, -4.0f, 6.0f, 6.0f, 6.0f, new Dilation(0.6f)),
				ModelTransform.pivot(0.0f, 6.0f, -8.0f));

		root.addChild(EntityModelPartNames.BODY,
				ModelPartBuilder.create()
						.uv(36, 42).cuboid(-4.0f, -10.0f, -7.0f, 8.0f, 16.0f, 6.0f, new Dilation(1.75f)),
				ModelTransform.of(0.0f, 5.0f, 2.0f, 1.5707964f, 0.0f, 0.0f));

		// TODO: separate legs
		ModelPartBuilder legBuilder = ModelPartBuilder.create()
				.uv(30, 0).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, new Dilation(0.5f));
		root.addChild(EntityModelPartNames.RIGHT_HIND_LEG, legBuilder, ModelTransform.pivot(-3.0f, 12.0f, 7.0f));
		root.addChild(EntityModelPartNames.LEFT_HIND_LEG, legBuilder, ModelTransform.pivot(3.0f, 12.0f, 7.0f));
		root.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, legBuilder, ModelTransform.pivot(-3.0f, 12.0f, -5.0f));
		root.addChild(EntityModelPartNames.LEFT_FRONT_LEG, legBuilder, ModelTransform.pivot(3.0f, 12.0f, -5.0f));

		return TexturedModelData.of(modelData, 64, 64);
	}
}