package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.RabbitEntityRenderer;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RabbitVariantEntityRenderer extends RabbitEntityRenderer
{
	private final Identifier texture;

	public RabbitVariantEntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(RabbitEntity rabbitEntity)
	{
		return this.texture;
	}
}