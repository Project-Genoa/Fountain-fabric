package micheal65536.fountain.fabric.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class RainbowWoolBlock extends Block
{
	public static final MapCodec<RainbowWoolBlock> CODEC = RainbowWoolBlock.createCodec(RainbowWoolBlock::new);

	public RainbowWoolBlock(AbstractBlock.Settings settings)
	{
		super(settings);
		this.setDefaultState(super.getDefaultState().with(Properties.FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends Block> getCodec()
	{
		return CODEC;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(Properties.FACING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		return super.getPlacementState(context).with(Properties.FACING, context.getSide());
	}
}