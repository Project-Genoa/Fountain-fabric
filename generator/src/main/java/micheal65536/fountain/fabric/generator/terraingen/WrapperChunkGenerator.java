package micheal65536.fountain.fabric.generator.terraingen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.NotNull;

import micheal65536.fountain.fabric.generator.blocks.NonReplaceableAirBlock;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class WrapperChunkGenerator extends ChunkGenerator implements NoiseConfigChunkGenerator
{
	public static final Codec<WrapperChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ChunkGenerator.CODEC.fieldOf("inner").forGetter(chunkGenerator -> chunkGenerator.chunkGenerator),
			RecordCodecBuilder.<BuildplateSize>create(instance1 -> instance1.group(
					Codec.INT.optionalFieldOf("size", 32).forGetter(buildplateSize -> buildplateSize.size),
					Codec.INT.optionalFieldOf("ground_level").forGetter(buildplateSize -> buildplateSize.groundLevel),
					Codec.INT.optionalFieldOf("underground_height", 16).forGetter(buildplateSize -> buildplateSize.undergroundHeight)
			).apply(instance1, instance1.stable(BuildplateSize::new))).optionalFieldOf("buildplate", new BuildplateSize(32, Optional.empty(), 16)).forGetter(chunkGenerator -> new BuildplateSize(chunkGenerator.earthConstraintBlocksCalculator.buildplateWidth, Optional.of(chunkGenerator.earthConstraintBlocksCalculator.buildplateGroundLevel), chunkGenerator.earthConstraintBlocksCalculator.buildplateUndergroundHeight))
	).apply(instance, instance.stable(WrapperChunkGenerator::new)));

	private static final class BuildplateSize
	{
		public final int size;
		public final Optional<Integer> groundLevel;
		public final int undergroundHeight;

		public BuildplateSize(int size, Optional<Integer> groundLevel, int undergroundHeight)
		{
			this.size = size;
			this.groundLevel = groundLevel;
			this.undergroundHeight = undergroundHeight;
		}
	}

	private final ChunkGenerator chunkGenerator;
	private final EarthConstraintBlocksCalculator earthConstraintBlocksCalculator;

	private WrapperChunkGenerator(@NotNull ChunkGenerator chunkGenerator, @NotNull BuildplateSize buildplateSize)
	{
		super(chunkGenerator.getBiomeSource());
		this.chunkGenerator = chunkGenerator;
		this.earthConstraintBlocksCalculator = new EarthConstraintBlocksCalculator(buildplateSize.size, buildplateSize.size, buildplateSize.groundLevel.orElse(this.chunkGenerator.getSeaLevel()), buildplateSize.undergroundHeight);
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec()
	{
		return CODEC;
	}

	@Override
	public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos)
	{
		this.chunkGenerator.getDebugHudText(text, noiseConfig, pos);
		text.add("Buildplate size: %d %d %d %d".formatted(this.earthConstraintBlocksCalculator.buildplateWidth, this.earthConstraintBlocksCalculator.buildplateDepth, this.earthConstraintBlocksCalculator.buildplateGroundLevel, this.earthConstraintBlocksCalculator.buildplateUndergroundHeight));
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk)
	{
		if (this.earthConstraintBlocksCalculator.isChunkOutsideBuildplate(chunk.getPos().getStartX(), chunk.getPos().getStartZ()))
		{
			this.setEarthConstraintBlocksInChunk(chunk);
			CompletableFuture<Chunk> future = new CompletableFuture<>();
			future.complete(chunk);
			return future;
		}
		CompletableFuture<Chunk> future = this.chunkGenerator.populateNoise(executor, blender, noiseConfig, structureAccessor, chunk);
		return future.thenApply(chunk1 ->
		{
			this.setEarthConstraintBlocksInChunk(chunk1);
			return chunk1;
		});
	}

	@Override
	public CompletableFuture<Chunk> populateBiomes(Executor executor, NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk)
	{
		return this.chunkGenerator.populateBiomes(executor, noiseConfig, blender, structureAccessor, chunk);
	}

	@Override
	public void populateEntities(ChunkRegion region)
	{
		// don't seem to need to limit anything here because entities can't spawn on the constraint blocks anyway
		this.chunkGenerator.populateEntities(region);
	}

	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk)
	{
		if (!this.earthConstraintBlocksCalculator.isChunkOutsideBuildplate(chunk.getPos().getStartX(), chunk.getPos().getStartZ()))
		{
			this.chunkGenerator.buildSurface(region, structures, noiseConfig, chunk);
			this.setEarthConstraintBlocksInChunk(chunk);
		}
	}

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep)
	{
		if (!this.earthConstraintBlocksCalculator.isChunkOutsideBuildplate(chunk.getPos().getStartX(), chunk.getPos().getStartZ()))
		{
			this.chunkGenerator.carve(chunkRegion, seed, noiseConfig, biomeAccess, structureAccessor, chunk, carverStep);
			this.setEarthConstraintBlocksInChunk(chunk);
		}
	}

	@Override
	public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor)
	{
		// it's not possible to force the constraint blocks here because feature generation can spread into adjacent chunks, so either turn features off or be selective about what features you use
		// features *shouldn't* overwrite the constraint blocks anyway because they're in the minecraft:features_cannot_replace tag
		this.chunkGenerator.generateFeatures(world, chunk, structureAccessor);
	}

	private void setEarthConstraintBlocksInChunk(@NotNull Chunk chunk)
	{
		int startX = chunk.getPos().getStartX();
		int startY = chunk.getBottomY();
		int startZ = chunk.getPos().getStartZ();
		int endX = chunk.getPos().getEndX();
		int endY = chunk.getTopY();
		int endZ = chunk.getPos().getEndZ();
		for (int y = startY; y < endY; y++)
		{
			for (int x = startX; x <= endX; x++)
			{
				for (int z = startZ; z <= endZ; z++)
				{
					BlockState blockState = this.earthConstraintBlocksCalculator.getEarthConstraintBlockAt(x, y, z);
					if (blockState != null)
					{
						chunk.setBlockState(new BlockPos(x, y, z), blockState, false);
						chunk.removeBlockEntity(new BlockPos(x, y, z));
						for (Heightmap.Type heightmapType : Heightmap.Type.values())
						{
							chunk.getHeightmap(heightmapType).trackUpdate(x - startX, y, z - startZ, blockState);
						}
					}
				}
			}
		}
	}

	@Override
	public int getWorldHeight()
	{
		return this.chunkGenerator.getWorldHeight();
	}

	@Override
	public int getMinimumY()
	{
		return this.chunkGenerator.getMinimumY();
	}

	@Override
	public int getSeaLevel()
	{
		return this.chunkGenerator.getSeaLevel();
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig)
	{
		int height = this.chunkGenerator.getHeight(x, z, heightmap, world, noiseConfig);
		for (int y = this.getMinimumY() + this.getWorldHeight() - 1; y >= this.getMinimumY(); y--)
		{
			BlockState blockState = this.earthConstraintBlocksCalculator.getEarthConstraintBlockAt(x, y, z);
			Block block = blockState != null ? blockState.getBlock() : null;
			if (block instanceof AirBlock || block instanceof NonReplaceableAirBlock)
			{
				if (height > y)
				{
					height = y;
				}
			}
			else if (block != null)
			{
				if (height < y)
				{
					height = y;
				}
				break;
			}
			else
			{
				if (y <= height)
				{
					break;
				}
			}
		}
		return height;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig)
	{
		VerticalBlockSample verticalBlockSample = this.chunkGenerator.getColumnSample(x, z, world, noiseConfig);
		for (int y = this.getMinimumY(); y < this.getMinimumY() + this.getWorldHeight(); y++)
		{
			BlockState blockState = this.earthConstraintBlocksCalculator.getEarthConstraintBlockAt(x, y, z);
			if (blockState != null)
			{
				verticalBlockSample.setState(y, blockState);
			}
		}
		return verticalBlockSample;
	}

	@Override
	public int getSpawnHeight(HeightLimitView world)
	{
		return this.chunkGenerator.getSpawnHeight(world);
	}

	@Override
	public BiomeSource getBiomeSource()
	{
		return this.chunkGenerator.getBiomeSource();
	}

	@Override
	public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(RegistryEntry<Biome> biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos)
	{
		return this.chunkGenerator.getEntitySpawnList(biome, accessor, group, pos);
	}

	@Override
	public StructurePlacementCalculator createStructurePlacementCalculator(RegistryWrapper<StructureSet> structureSetRegistry, NoiseConfig noiseConfig, long seed)
	{
		return this.chunkGenerator.createStructurePlacementCalculator(structureSetRegistry, noiseConfig, seed);
	}

	@Override
	public GenerationSettings getGenerationSettings(RegistryEntry<Biome> biomeEntry)
	{
		return this.chunkGenerator.getGenerationSettings(biomeEntry);
	}

	@Override
	public NoiseConfig getNoiseConfig(@NotNull DynamicRegistryManager dynamicRegistryManager, long seed)
	{
		if (this.chunkGenerator instanceof NoiseChunkGenerator noiseChunkGenerator)
		{
			return NoiseConfig.create(noiseChunkGenerator.getSettings().value(), dynamicRegistryManager.getWrapperOrThrow(RegistryKeys.NOISE_PARAMETERS), seed);
		}
		else
		{
			return NoiseConfig.create(ChunkGeneratorSettings.createMissingSettings(), dynamicRegistryManager.getWrapperOrThrow(RegistryKeys.NOISE_PARAMETERS), seed);
		}
	}
}