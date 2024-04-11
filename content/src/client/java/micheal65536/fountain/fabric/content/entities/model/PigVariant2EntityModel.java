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
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.EnumSet;

public class PigVariant2EntityModel<T extends Entity> extends PigEntityModel<T>
{
	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(new Identifier("fountain", "pig_v2"), "main");

	public static void register()
	{
		EntityModelLayerRegistry.registerModelLayer(ENTITY_MODEL_LAYER, PigVariant2EntityModel::getTexturedModelData);
	}

	public PigVariant2EntityModel(ModelPart root)
	{
		super(root);
	}

	public static TexturedModelData getTexturedModelData()
	{
		ModelData modelData = new ModelData();
		QuadrupedEntityModel.getModelData(6, Dilation.NONE);

		ModelPartData root = modelData.getRoot();

		root.addChild(EntityModelPartNames.HEAD,
				ModelPartBuilder.create()
						.uv(0, 0).cuboid(-4.0f, -4.0f, -8.0f, 8.0f, 8.0f, 8.0f)
						.uv(9, 18).cuboid(-2.0f, 0.0f, -9.0f, 4.0f, 3.0f, 1.0f),
				ModelTransform.pivot(0.0f, 12.0f, -6.0f));

		root.addChild(EntityModelPartNames.BODY,
				ModelPartBuilder.create()
						.uv(20, 8).cuboid(-5.0f, -10.0f, -7.0f, 10.0f, 16.0f, 8.0f, EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.EAST))
						.uv(56, 8).cuboid(-5.0f, -10.0f, -7.0f, 10.0f, 16.0f, 8.0f, EnumSet.of(Direction.WEST))
						.uv(0, 24).cuboid(-5.0f, -10.0f, -7.0f, 10.0f, 16.0f, 8.0f, EnumSet.of(Direction.UP, Direction.DOWN)),
				ModelTransform.of(0.0f, 11.0f, 2.0f, 1.5707964f, 0.0f, 0.0f));

		// TODO: separate legs
		ModelPartBuilder legBuilder = ModelPartBuilder.create()
				.uv(32, -4).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, EnumSet.of(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH))
				.uv(28, 12).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, EnumSet.of(Direction.UP, Direction.DOWN));
		root.addChild(EntityModelPartNames.RIGHT_HIND_LEG, legBuilder, ModelTransform.pivot(-3.0f, 18.0f, 7.0f));
		root.addChild(EntityModelPartNames.LEFT_HIND_LEG, legBuilder, ModelTransform.pivot(3.0f, 18.0f, 7.0f));
		root.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, legBuilder, ModelTransform.pivot(-3.0f, 18.0f, -5.0f));
		root.addChild(EntityModelPartNames.LEFT_FRONT_LEG, legBuilder, ModelTransform.pivot(3.0f, 18.0f, -5.0f));

		return TexturedModelData.of(modelData, 64, 32);
	}
}