package micheal65536.fountain.fabric.content.entities.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SlimeVariantEntityRenderer extends SlimeEntityRenderer
{
	private final Identifier texture;

	public SlimeVariantEntityRenderer(EntityRendererFactory.Context context, @NotNull String texture)
	{
		super(context);
		this.texture = new Identifier(texture);
	}

	@Override
	public Identifier getTexture(SlimeEntity slimeEntity)
	{
		return this.texture;
	}
}