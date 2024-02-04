package micheal65536.fountain.fabric.generator.worldborder;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderStage;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

import micheal65536.fountain.fabric.generator.Main;
import micheal65536.fountain.fabric.generator.blocks.NonReplaceableAirBlock;

public class BuildplateWorldBorder extends WorldBorder
{
	private final World world;

	public BuildplateWorldBorder(World world)
	{
		this.world = world;
	}

	@Override
	public boolean contains(BlockPos pos)
	{
		Chunk chunk = this.world.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()), ChunkStatus.NOISE, false);
		if (chunk == null)
		{
			Main.LOGGER.warn("Chunk containing %s was not generated when checking world border".formatted(pos.toShortString()));
			return true;
		}
		Block block = chunk.getBlockState(pos).getBlock();
		return !(block instanceof NonReplaceableAirBlock);
	}

	@Override
	public boolean contains(ChunkPos pos)
	{
		// this is only used for entity spawning and is eventually followed by a contains(BlockPos) check
		return true;
	}

	@Override
	public boolean contains(Box box)
	{
		BlockPos minPos = BlockPos.ofFloored(box.minX, box.minY, box.minZ);
		BlockPos maxPos = BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ);
		return BlockPos.stream(minPos, maxPos).anyMatch(blockPos -> this.contains(blockPos));
	}

	@Override
	public boolean contains(double x, double z)
	{
		// never used
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(double x, double z, double margin)
	{
		// never used
		throw new UnsupportedOperationException();
	}

	@Override
	public BlockPos clamp(double x, double y, double z)
	{
		// this is only used for dimension switching
		// TODO: hackerdude will probably want to support that later so we should test if portals behave acceptably with this
		return BlockPos.ofFloored(x, y, z);
	}

	@Override
	public double getDistanceInsideBorder(Entity entity)
	{
		// this is only used for applying damage and warning visuals
		return Double.MAX_VALUE;
	}

	@Override
	public double getDistanceInsideBorder(double x, double z)
	{
		// this is used for determining player spawn position and applying warning visuals
		return Double.MAX_VALUE;
	}

	// canCollide and asVoxelShape are always used together
	// first, canCollide is called, then asVoxelShape is used to get the actual shape for handling the collisions
	// because of the way that these are used, we only need to return the voxels for the blocks that intersect with the bounding box from the preceding call to canCollide

	private ThreadLocal<Box> lastCanCollideBox = new ThreadLocal<>();

	{
		this.lastCanCollideBox.set(null);
	}

	@Override
	public boolean canCollide(Entity entity, Box box)
	{
		if (this.lastCanCollideBox.get() != null)
		{
			Main.LOGGER.warn("BuildplateWorldBorder.canCollide called again with no inbetween call to asVoxelShape");
			this.lastCanCollideBox.set(null);
		}

		BlockPos minPos = BlockPos.ofFloored(box.minX, box.minY, box.minZ);
		BlockPos maxPos = BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ);
		boolean collides = BlockPos.stream(minPos, maxPos).anyMatch(blockPos -> !this.contains(blockPos));

		if (collides)
		{
			this.lastCanCollideBox.set(box);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public VoxelShape asVoxelShape()
	{
		if (this.lastCanCollideBox.get() == null)
		{
			Main.LOGGER.warn("BuildplateWorldBorder.asVoxelShape called with no preceding call to canCollide");
			return VoxelShapes.empty();
		}

		Box box = this.lastCanCollideBox.get();
		this.lastCanCollideBox.set(null);
		BlockPos minPos = BlockPos.ofFloored(box.minX, box.minY, box.minZ);
		BlockPos maxPos = BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ);
		return VoxelShapes.union(VoxelShapes.empty(), BlockPos.stream(minPos, maxPos).filter(blockPos -> !this.contains(blockPos)).map(blockPos -> VoxelShapes.fullCube().offset(blockPos.getX(), blockPos.getY(), blockPos.getZ())).toArray(VoxelShape[]::new));
	}

	@Override
	public WorldBorderStage getStage()
	{
		return WorldBorderStage.STATIONARY;
	}

	@Override
	public double getBoundWest()
	{
		// only used for rendering
		return -29999984.0;
	}

	@Override
	public double getBoundNorth()
	{
		// only used for rendering
		return -29999984.0;
	}

	@Override
	public double getBoundEast()
	{
		// only used for rendering
		return 29999984.0;
	}

	@Override
	public double getBoundSouth()
	{
		// only used for rendering
		return 29999984.0;
	}

	@Override
	public double getCenterX()
	{
		return 0.0;
	}

	@Override
	public double getCenterZ()
	{
		return 0.0;
	}

	@Override
	public void setCenter(double x, double z)
	{
		// empty
	}

	@Override
	public double getSize()
	{
		// only used for rendering and packet stuff
		return 59999968.0;
	}

	@Override
	public long getSizeLerpTime()
	{
		return 0;
	}

	@Override
	public double getSizeLerpTarget()
	{
		return 59999968.0;
	}

	@Override
	public void setSize(double size)
	{
		// empty
	}

	@Override
	public void interpolateSize(double fromSize, double toSize, long time)
	{
		// empty
	}

	@Override
	public void setMaxRadius(int maxRadius)
	{
		// empty
	}

	@Override
	public int getMaxRadius()
	{
		return 29999984;
	}

	@Override
	public double getSafeZone()
	{
		return 0.0;
	}

	@Override
	public void setSafeZone(double safeZone)
	{
		// empty
	}

	@Override
	public double getDamagePerBlock()
	{
		return 0.0;
	}

	@Override
	public void setDamagePerBlock(double damagePerBlock)
	{
		// empty
	}

	@Override
	public double getShrinkingSpeed()
	{
		return 0.0;
	}

	@Override
	public int getWarningTime()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public void setWarningTime(int warningTime)
	{
		// empty
	}

	@Override
	public int getWarningBlocks()
	{
		return 0;
	}

	@Override
	public void setWarningBlocks(int warningBlocks)
	{
		// empty
	}

	@Override
	public void tick()
	{
		// empty
	}
}