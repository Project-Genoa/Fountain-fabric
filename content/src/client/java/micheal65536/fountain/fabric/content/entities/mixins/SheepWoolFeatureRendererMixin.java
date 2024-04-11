package micheal65536.fountain.fabric.content.entities.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.content.entities.SheepVariantEntity;

@Mixin(SheepWoolFeatureRenderer.class)
public class SheepWoolFeatureRendererMixin
{
	private static final Identifier SKIN = new Identifier("textures/entity/sheep/sheep_fur.png");

	@Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer;SKIN:Lnet/minecraft/util/Identifier;"))
	private Identifier skin(@Local(ordinal = 0) SheepEntity sheepEntity)
	{
		if (sheepEntity instanceof SheepVariantEntity)
		{
			return ((FeatureRendererAccessor<SheepEntity>) this).callGetTexture(sheepEntity);
		}
		else
		{
			return SKIN;
		}
	}

	@Mixin(FeatureRenderer.class)
	public interface FeatureRendererAccessor<T extends Entity>
	{
		@Invoker("getTexture")
		Identifier callGetTexture(T entity);
	}
}