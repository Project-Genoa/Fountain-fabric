package micheal65536.fountain.fabric.generator.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class SolidAirBlock extends EarthConstraintBlock
{
	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}
}