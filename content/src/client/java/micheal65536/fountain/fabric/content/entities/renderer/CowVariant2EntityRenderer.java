package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.CowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import micheal65536.fountain.fabric.content.entities.model.CowVariant2EntityModel;

public class CowVariant2EntityRenderer extends CowEntityRenderer
{
	private final Identifier texture;

	public CowVariant2EntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		super.model = new CowVariant2EntityModel<>(context.getPart(CowVariant2EntityModel.ENTITY_MODEL_LAYER));

		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(CowEntity cowEntity)
	{
		return this.texture;
	}
}