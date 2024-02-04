package micheal65536.fountain.fabric.generator.terraingen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
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
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.NotNull;

import micheal65536.fountain.fabric.generator.blocks.NonReplaceableAirBlock;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

public class EmptyChunkGenerator extends ChunkGenerator
{
	public static final Codec<EmptyChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.optionalFieldOf("size", 32).forGetter(chunkGenerator -> chunkGenerator.earthConstraintBlocksCalculator.buildplateWidth),
			Codec.INT.optionalFieldOf("ground_level", 34).forGetter(chunkGenerator -> chunkGenerator.earthConstraintBlocksCalculator.buildplateGroundLevel),
			Codec.INT.optionalFieldOf("underground_height", 16).forGetter(chunkGenerator -> chunkGenerator.earthConstraintBlocksCalculator.buildplateUndergroundHeight),
			Biome.REGISTRY_CODEC.fieldOf("biome").forGetter(chunkGenerator -> chunkGenerator.biome)
	).apply(instance, instance.stable(EmptyChunkGenerator::new)));

	private final EarthConstraintBlocksCalculator earthConstraintBlocksCalculator;
	private final RegistryEntry<Biome> biome;
	private final BlockState airBlockState = Blocks.AIR.getDefaultState();

	private EmptyChunkGenerator(int size, int groundLevel, int undergroundHeight, @NotNull RegistryEntry<Biome> biome)
	{
		super(new FixedBiomeSource(biome));
		this.earthConstraintBlocksCalculator = new EarthConstraintBlocksCalculator(size, size, groundLevel, undergroundHeight);
		this.biome = biome;
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec()
	{
		return CODEC;
	}

	@Override
	public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos)
	{
		text.add("Buildplate size: %d %d %d %d".formatted(this.earthConstraintBlocksCalculator.buildplateWidth, this.earthConstraintBlocksCalculator.buildplateDepth, this.earthConstraintBlocksCalculator.buildplateGroundLevel, this.earthConstraintBlocksCalculator.buildplateUndergroundHeight));
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk)
	{
		return CompletableFuture.supplyAsync(() ->
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
						BlockState blockState = this.getBlockAt(x, y, z);
						chunk.setBlockState(new BlockPos(x, y, z), blockState, false);
						for (Heightmap.Type heightmapType : Heightmap.Type.values())
						{
							chunk.getHeightmap(heightmapType).trackUpdate(x - startX, y, z - startZ, blockState);
						}
					}
				}
			}
			return chunk;
		}, executor);
	}

	@Override
	public CompletableFuture<Chunk> populateBiomes(Executor executor, NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk)
	{
		return CompletableFuture.supplyAsync(() ->
		{
			chunk.populateBiomes(this.biomeSource, noiseConfig.getMultiNoiseSampler());
			return chunk;
		}, executor);
	}

	@Override
	public void populateEntities(ChunkRegion region)
	{
		// empty
	}

	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk)
	{
		// empty
	}

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep)
	{
		// empty
	}

	@Override
	public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor)
	{
		// empty
	}

	@Override
	public int getWorldHeight()
	{
		return 384;
	}

	@Override
	public int getMinimumY()
	{
		return -64;
	}

	@Override
	public int getSeaLevel()
	{
		return this.earthConstraintBlocksCalculator.buildplateGroundLevel;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig)
	{
		for (int y = this.getMinimumY() + this.getWorldHeight() - 1; y >= this.getMinimumY(); y--)
		{
			BlockState blockState = this.getBlockAt(x, y, z);
			Block block = blockState.getBlock();
			if (!(block instanceof AirBlock || block instanceof NonReplaceableAirBlock))
			{
				return y;
			}
		}
		return this.getMinimumY();
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig)
	{
		BlockState[] blockStates = new BlockState[this.getWorldHeight()];
		VerticalBlockSample verticalBlockSample = new VerticalBlockSample(this.getMinimumY(), blockStates);
		for (int y = this.getMinimumY(); y < this.getMinimumY() + this.getWorldHeight(); y++)
		{
			BlockState blockState = this.getBlockAt(x, y, z);
			verticalBlockSample.setState(y, blockState);
		}
		return verticalBlockSample;
	}

	@Override
	public int getSpawnHeight(HeightLimitView world)
	{
		return this.earthConstraintBlocksCalculator.buildplateGroundLevel + 10;
	}

	@Override
	public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(RegistryEntry<Biome> biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos)
	{
		return Pool.empty();
	}

	@Override
	public StructurePlacementCalculator createStructurePlacementCalculator(RegistryWrapper<StructureSet> structureSetRegistry, NoiseConfig noiseConfig, long seed)
	{
		return StructurePlacementCalculator.create(noiseConfig, seed, this.biomeSource, Stream.empty());
	}

	private BlockState getBlockAt(int x, int y, int z)
	{
		BlockState blockState = this.earthConstraintBlocksCalculator.getEarthConstraintBlockAt(x, y, z);
		if (blockState == null)
		{
			blockState = this.airBlockState;
		}
		return blockState;
	}
}