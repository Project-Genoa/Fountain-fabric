package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.CowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class CowVariantEntityRenderer extends CowEntityRenderer
{
	private final Identifier texture;

	public CowVariantEntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(CowEntity cowEntity)
	{
		return this.texture;
	}
}