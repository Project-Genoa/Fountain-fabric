package micheal65536.fountain.fabric.content;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import micheal65536.fountain.fabric.content.entities.model.CowVariant2EntityModel;
import micheal65536.fountain.fabric.content.entities.model.PigVariant2EntityModel;
import micheal65536.fountain.fabric.content.entities.model.SheepVariantEntityModel;
import micheal65536.fountain.fabric.content.entities.model.SheepVariantWoolEntityModel;
import micheal65536.fountain.fabric.content.entities.renderer.ChickenVariantEntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.CowVariant2EntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.CowVariantEntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.PigVariant2EntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.PigVariantEntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.RabbitVariantEntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.SheepVariantEntityRenderer;
import micheal65536.fountain.fabric.content.entities.renderer.SlimeVariantEntityRenderer;

public class ClientMain implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		CowVariant2EntityModel.register();
		PigVariant2EntityModel.register();
		SheepVariantEntityModel.register();
		SheepVariantWoolEntityModel.register();

		this.registerEarthMobVariantRenderer("genoa:amber_chicken", "chicken/amber_chicken.png", ChickenVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:bronzed_chicken", "chicken/bronzed_chicken.png", ChickenVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:gold_crested_chicken", "chicken/gold_crested_chicken.png", ChickenVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:midnight_chicken", "chicken/midnight_chicken.png", ChickenVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:skewbald_chicken", "chicken/skewbald_chicken.png", ChickenVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:stormy_chicken", "chicken/stormy_chicken.png", ChickenVariantEntityRenderer::new);

		this.registerEarthMobVariantRenderer("genoa:albino_cow", "cow/albino_cow.png", CowVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:ashen_cow", "cow/ashen_cow.png", CowVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:cookie_cow", "cow/cookie_cow.png", CowVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:cream_cow", "cow/cream_cow.png", CowVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:dairy_cow", "cow/dairy_cow.png", CowVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:pinto_cow", "cow/pinto_cow.png", CowVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:sunset_cow", "cow/sunset_cow.png", CowVariant2EntityRenderer::new);

		this.registerEarthMobVariantRenderer("genoa:mottled_pig", "pig/pig_mottled.png", PigVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:pale_pig", "pig/pale_pig.png", PigVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:piebald_pig", "pig/piebald_pig.png", PigVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:pink_footed_pig", "pig/pink_footed_pig.png", PigVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:sooty_pig", "pig/sooty_pig.png", PigVariant2EntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:spotted_pig", "pig/spotted_pig.png", PigVariant2EntityRenderer::new);

		this.registerEarthMobVariantRenderer("genoa:bold_striped_rabbit", "rabbit/bold_striped_rabbit.png", RabbitVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:freckled_rabbit", "rabbit/freckled_rabbit.png", RabbitVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:harelequin_rabbit", "rabbit/harelequin_rabbit.png", RabbitVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:muddy_foot_rabbit", "rabbit/muddy_foot_rabbit.png", RabbitVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:vested_rabbit", "rabbit/vested_rabbit.png", RabbitVariantEntityRenderer::new);

		this.registerEarthMobVariantRenderer("genoa:flecked_sheep", "sheep/flecked_sheep.png", SheepVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:inky_sheep", "sheep/inky_sheep.png", SheepVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:long_nosed_sheep", "sheep/long_nosed_sheep.png", SheepVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:patched_sheep", "sheep/patched_sheep.png", SheepVariantEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:rainbow_sheep", "sheep/rainbow_sheep.png", SheepVariantEntityRenderer::new);    // TODO: fix model
		this.registerEarthMobVariantRenderer("genoa:rocky_sheep", "sheep/rocky_sheep.png", SheepVariantEntityRenderer::new);

		this.registerEarthMobVariantRenderer("genoa:genoa_slime", (EntityRendererFactory<SlimeEntity>) SlimeEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:genoa_slime_half", (EntityRendererFactory<SlimeEntity>) SlimeEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:genoa_slime_quarter", (EntityRendererFactory<SlimeEntity>) SlimeEntityRenderer::new);
		this.registerEarthMobVariantRenderer("genoa:tropical_slime", "slime/tropical_slime.png", SlimeVariantEntityRenderer::new);
	}

	private void registerEarthMobVariantRenderer(@NotNull String type, @NotNull EntityRendererFactory<?> entityRendererFactory)
	{
		EntityRendererRegistry.register(Registries.ENTITY_TYPE.get(new Identifier(type)), (EntityRendererFactory) entityRendererFactory);
	}

	private void registerEarthMobVariantRenderer(@NotNull String type, @NotNull String textureName, @NotNull VariantEntityRendererFactory<?> entityRendererFactory)
	{
		EntityRendererRegistry.register(Registries.ENTITY_TYPE.get(new Identifier(type)), context -> (EntityRenderer<Entity>) entityRendererFactory.getInstance(context, "fountain:textures/entity/" + textureName));
	}

	private interface VariantEntityRendererFactory<T extends EntityRenderer<?>>
	{
		T getInstance(@NotNull EntityRendererFactory.Context context, @NotNull String textureName);
	}
}