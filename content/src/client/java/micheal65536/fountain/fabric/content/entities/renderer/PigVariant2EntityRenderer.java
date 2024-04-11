package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import micheal65536.fountain.fabric.content.entities.model.PigVariant2EntityModel;

public class PigVariant2EntityRenderer extends PigEntityRenderer
{
	private final Identifier texture;

	public PigVariant2EntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		this.model = new PigEntityModel<>(context.getPart(PigVariant2EntityModel.ENTITY_MODEL_LAYER));

		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(PigEntity pigEntity)
	{
		return this.texture;
	}
}