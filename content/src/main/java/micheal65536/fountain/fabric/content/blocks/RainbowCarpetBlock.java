package micheal65536.fountain.fabric.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class RainbowCarpetBlock extends CarpetBlock
{
	public static final MapCodec<RainbowCarpetBlock> CODEC = RainbowCarpetBlock.createCodec(RainbowCarpetBlock::new);

	public RainbowCarpetBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(super.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public MapCodec<? extends CarpetBlock> getCodec()
	{
		return CODEC;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing());
	}
}