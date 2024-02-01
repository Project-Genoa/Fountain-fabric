package micheal65536.fountain.fabric.generator.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class EarthConstraintBlock extends Block implements OperatorBlock
{
	public EarthConstraintBlock()
	{
		super(AbstractBlock.Settings.create()
				.nonOpaque()
				.suffocates(((state, world, pos) -> false))
				.strength(-1.0f, 3600000.0f)
				.pistonBehavior(PistonBehavior.BLOCK)
		);
	}

	protected EarthConstraintBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos)
	{
		return true;
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos)
	{
		return 1.0f;
	}
}