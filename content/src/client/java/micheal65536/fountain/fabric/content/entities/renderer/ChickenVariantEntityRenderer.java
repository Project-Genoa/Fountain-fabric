package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ChickenVariantEntityRenderer extends ChickenEntityRenderer
{
	private final Identifier texture;

	public ChickenVariantEntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(ChickenEntity chickenEntity)
	{
		return this.texture;
	}
}