package micheal65536.fountain.fabric.content.entities.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class CowVariant2EntityModel<T extends Entity> extends CowEntityModel<T>
{
	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(new Identifier("fountain", "cow_v2"), "main");

	public static void register()
	{
		EntityModelLayerRegistry.registerModelLayer(ENTITY_MODEL_LAYER, CowVariant2EntityModel::getTexturedModelData);
	}

	public CowVariant2EntityModel(ModelPart root)
	{
		super(root);
	}

	public static TexturedModelData getTexturedModelData()
	{
		ModelData modelData = new ModelData();

		ModelPartData root = modelData.getRoot();

		root.addChild(EntityModelPartNames.HEAD,
				ModelPartBuilder.create()
						.uv(0, 0).cuboid(-4.0f, -4.0f, -6.0f, 8.0f, 8.0f, 6.0f)
						.uv(22, 0).cuboid(EntityModelPartNames.RIGHT_HORN, -5.0f, -5.0f, -4.0f, 1.0f, 3.0f, 1.0f)
						.uv(27, 0).cuboid(EntityModelPartNames.LEFT_HORN, 4.0f, -5.0f, -4.0f, 1.0f, 3.0f, 1.0f),
				ModelTransform.pivot(0.0f, 4.0f, -8.0f));

		// TODO: fix udder
		root.addChild(EntityModelPartNames.BODY,
				ModelPartBuilder.create()
						.uv(20, 4).cuboid(-6.0f, -10.0f, -7.0f, 12.0f, 18.0f, 10.0f)
						.uv(0, 14).cuboid(-2.0f, 2.0f, -8.0f, 4.0f, 6.0f, 1.0f),
				ModelTransform.of(0.0f, 5.0f, 2.0f, 1.5707964f, 0.0f, 0.0f));

		// TODO: separate legs
		ModelPartBuilder legBuilder = ModelPartBuilder.create()
				.uv(0, 32).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f);
		root.addChild(EntityModelPartNames.RIGHT_HIND_LEG, legBuilder, ModelTransform.pivot(-4.0f, 12.0f, 7.0f));
		root.addChild(EntityModelPartNames.LEFT_HIND_LEG, legBuilder, ModelTransform.pivot(4.0f, 12.0f, 7.0f));
		root.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, legBuilder, ModelTransform.pivot(-4.0f, 12.0f, -6.0f));
		root.addChild(EntityModelPartNames.LEFT_FRONT_LEG, legBuilder, ModelTransform.pivot(4.0f, 12.0f, -6.0f));

		return TexturedModelData.of(modelData, 64, 64);
	}
}