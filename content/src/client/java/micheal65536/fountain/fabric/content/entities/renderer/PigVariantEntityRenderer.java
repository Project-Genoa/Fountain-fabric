package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.RabbitEntityRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class PigVariantEntityRenderer extends PigEntityRenderer
{
	private final Identifier texture;

	public PigVariantEntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(PigEntity pigEntity)
	{
		return this.texture;
	}
}