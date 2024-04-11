package micheal65536.fountain.fabric.content;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import micheal65536.fountain.fabric.content.entities.ChickenVariantEntity;
import micheal65536.fountain.fabric.content.entities.SheepVariantEntity;
import micheal65536.fountain.fabric.content.entities.mixins.DefaultAttributeRegistryAccessor;
import micheal65536.fountain.fabric.content.entities.mixins.EntityTypeAccessor;

public class Main implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("fountain-content");

	@Override
	public void onInitialize()
	{
		// entities

		this.registerEarthMobVariant("minecraft:chicken", "genoa:amber_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:bronzed_chicken", ChickenVariantEntity::new);
		// TODO: cluck_shroom
		// TODO: fancy_chicken
		this.registerEarthMobVariant("minecraft:chicken", "genoa:gold_crested_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:midnight_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:skewbald_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:stormy_chicken", ChickenVariantEntity::new);

		this.registerEarthMobVariant("minecraft:cow", "genoa:albino_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:ashen_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:cookie_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:cream_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:dairy_cow");
		// TODO: moo_bloom
		// TODO: moolip
		this.registerEarthMobVariant("minecraft:cow", "genoa:pinto_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:sunset_cow");
		// TODO: umbra_cow
		// TODO: wooly_cow

		// TODO: furnace_golem

		// TODO: jolly_llama

		this.registerEarthMobVariant("minecraft:pig", "genoa:mottled_pig");
		// TODO: mud_pig
		this.registerEarthMobVariant("minecraft:pig", "genoa:pale_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:piebald_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:pink_footed_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:sooty_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:spotted_pig");

		this.registerEarthMobVariant("minecraft:rabbit", "genoa:bold_striped_rabbit");
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:freckled_rabbit");
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:harelequin_rabbit");
		// TODO: jumbo_rabbit
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:muddy_foot_rabbit");
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:vested_rabbit");

		// TODO: find out what the correct behavior is for shearing/dying sheep variants
		this.registerEarthMobVariant("minecraft:sheep", "genoa:flecked_sheep", SheepVariantEntity.createFactory(true, DyeColor.BROWN));
		// TODO: fuzzy_sheep
		// TODO: horned_sheep
		this.registerEarthMobVariant("minecraft:sheep", "genoa:inky_sheep", SheepVariantEntity.createFactory(true, DyeColor.GRAY));
		this.registerEarthMobVariant("minecraft:sheep", "genoa:long_nosed_sheep", SheepVariantEntity.createFactory(true, DyeColor.BROWN));
		this.registerEarthMobVariant("minecraft:sheep", "genoa:patched_sheep", SheepVariantEntity.createFactory(true, DyeColor.WHITE));
		// TODO: rainbow_sheep
		this.registerEarthMobVariant("minecraft:sheep", "genoa:rocky_sheep", SheepVariantEntity.createFactory(true, DyeColor.GRAY));

		// TODO: melon_golem

		// TODO
	}

	private <T extends MobEntity> void registerEarthMobVariant(@NotNull String baseType, @NotNull String variantType)
	{
		this.registerEarthMobVariant(baseType, variantType, null);
	}

	private <T extends U, U extends MobEntity> void registerEarthMobVariant(@NotNull String baseType, @NotNull String variantType, @Nullable EntityType.EntityFactory<T> factory)
	{
		EntityType<U> entityType = (EntityType<U>) Registries.ENTITY_TYPE.get(new Identifier(baseType));
		EntityType<T> newEntityType = new EntityType<T>(factory != null ? factory : ((EntityTypeAccessor<T>) entityType).getFactory(), entityType.getSpawnGroup(), entityType.isSaveable(), entityType.isSummonable(), entityType.isFireImmune(), entityType.isSpawnableFarFromPlayer(), ((EntityTypeAccessor<?>) entityType).getCanSpawnInside(), entityType.getDimensions(), entityType.getMaxTrackDistance(), entityType.getTrackTickInterval(), entityType.getRequiredFeatures());
		((EntityTypeAccessor<?>) newEntityType).setLootTableId(entityType.getLootTableId());
		SpawnEggItem spawnEggItem = ((SpawnEggItem) Registries.ITEM.get(new Identifier(baseType + "_spawn_egg")));
		SpawnEggItem newSpawnEggItem = new SpawnEggItem(newEntityType, spawnEggItem.getColor(0), spawnEggItem.getColor(1), new Item.Settings());

		Registry.register(Registries.ENTITY_TYPE, new Identifier(variantType), newEntityType);
		DefaultAttributeRegistryAccessor.getMap().put(newEntityType, DefaultAttributeRegistryAccessor.getMap().get(entityType));
		Registry.register(Registries.ITEM, new Identifier(variantType + "_spawn_egg"), newSpawnEggItem);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(newSpawnEggItem));
	}
}