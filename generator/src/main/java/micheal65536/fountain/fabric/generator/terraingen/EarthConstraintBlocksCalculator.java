package micheal65536.fountain.fabric.generator.terraingen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class EarthConstraintBlocksCalculator
{
	public final int buildplateWidth;
	public final int buildplateDepth;
	public final int buildplateGroundLevel;
	public final int buildplateUndergroundHeight;

	private final BlockState airBlockState = Blocks.AIR.getDefaultState();
	private final BlockState bedrockBlockState = Blocks.BEDROCK.getDefaultState();
	private final BlockState invisibleConstraintBlockState = Registries.BLOCK.get(new Identifier("fountain", "invisible_constraint")).getDefaultState();
	private final BlockState blendConstraintBlockState = Registries.BLOCK.get(new Identifier("fountain", "blend_constraint")).getDefaultState();
	private final BlockState borderConstraintBlockState = Registries.BLOCK.get(new Identifier("fountain", "border_constraint")).getDefaultState();
	private final BlockState solidAirBlockState = Registries.BLOCK.get(new Identifier("fountain", "solid_air")).getDefaultState();
	private final BlockState nonReplaceableAirBlockState = Registries.BLOCK.get(new Identifier("fountain", "non_replaceable_air")).getDefaultState();

	public EarthConstraintBlocksCalculator(int buildplateWidth, int buildplateDepth, int buildplateGroundLevel, int buildplateUndergroundHeight)
	{
		this.buildplateWidth = buildplateWidth;
		this.buildplateDepth = buildplateDepth;
		this.buildplateGroundLevel = buildplateGroundLevel;
		this.buildplateUndergroundHeight = buildplateUndergroundHeight;
	}

	@Nullable
	public BlockState getEarthConstraintBlockAt(int x, int y, int z)
	{
		int minX = 0 - this.buildplateWidth / 2;
		int minZ = 0 - this.buildplateDepth / 2;
		int maxX = minX + this.buildplateWidth - 1;
		int maxZ = minZ + this.buildplateDepth - 1;
		int minY = this.buildplateGroundLevel - this.buildplateUndergroundHeight;
		int maxY = this.buildplateGroundLevel - 1;

		if (y < 0)
		{
			return this.solidAirBlockState;
		}
		else if (y >= 256)
		{
			return this.nonReplaceableAirBlockState;
		}
		else if (x >= minX - 1 && x <= maxX + 1 && y >= minY - 1 && y <= maxY && z >= minZ - 1 && z <= maxZ + 1)
		{
			if (y == minY - 1)
			{
				return this.bedrockBlockState;
			}
			else if (x == minX - 1 || x == maxX + 1 || z == minZ - 1 || z == maxZ + 1)
			{
				if (y == maxY)
				{
					return this.blendConstraintBlockState;
				}
				else
				{
					return this.bedrockBlockState;
				}
			}
			else
			{
				return null;
			}
		}
		else if (x >= minX - 2 && x <= maxX + 2 && y >= minY - 2 && y <= maxY && z >= minZ - 2 && z <= maxZ + 2)
		{
			if (x == minX - 2 || x == maxX + 2 || y == minY - 2 || z == minZ - 2 || z == maxZ + 2)
			{
				return this.invisibleConstraintBlockState;
			}
			else
			{
				throw new AssertionError();
			}
		}
		else if (y == maxY)
		{
			if (x >= minX - 3 && x <= maxX + 3 && z >= minZ - 3 && z <= maxZ + 3)
			{
				return this.invisibleConstraintBlockState;
			}
			else if (x >= minX - 4 && x <= maxX + 4 && z >= minZ - 4 && z <= maxZ + 4)
			{
				return this.borderConstraintBlockState;
			}
			else
			{
				return this.solidAirBlockState;
			}
		}
		else if (y < maxY)
		{
			return this.solidAirBlockState;
		}
		else if (y > maxY)
		{
			if (x <= minX - 4 || x >= maxX + 4 || z <= minZ - 4 || z >= maxZ + 4)
			{
				return this.nonReplaceableAirBlockState;
			}
			else if (x < minX || x > maxX || z < minZ || z > maxZ)
			{
				return this.airBlockState;
			}
			else
			{
				return null;
			}
		}
		else
		{
			throw new AssertionError();
		}
	}

	public boolean isChunkOutsideBuildplate(int x, int z)
	{
		int minX = 0 - this.buildplateWidth / 2;
		int minZ = 0 - this.buildplateDepth / 2;
		int maxX = minX + this.buildplateWidth - 1;
		int maxZ = minZ + this.buildplateDepth - 1;

		if (x + 15 < minX - 4 || x > maxX + 4 || z + 15 < minZ - 4 || z > maxZ + 4)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}