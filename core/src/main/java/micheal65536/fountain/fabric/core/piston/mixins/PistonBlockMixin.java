package micheal65536.fountain.fabric.core.piston.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.core.piston.BedrockCompatiblePistonBlockEntity;

import java.util.HashMap;

// send moving piston updates to clients
// (it would be nice if we could send them only to Earth clients, but here we send them to all clients, it doesn't seem to outright break regular clients even though it is not the standard behavior)
// also adds the piston base position to the block entity data for all associated moving_piston blocks
@Mixin(PistonBlock.class)
public class PistonBlockMixin
{
	private final ThreadLocal<HashMap<BlockPos, Pair<BlockState, Integer>>> withheldMovingPistonBlockUpdates = ThreadLocal.withInitial(HashMap::new);

	@Redirect(method = {"onSyncedBlockEvent(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z", "move(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean setBlockState(World world, BlockPos blockPos, BlockState blockState, int flags)
	{
		if (blockState.isOf(Blocks.MOVING_PISTON))
		{
			// this is so that we can filter out "source" moving_piston instances (which refer to the head/body of the piston itself) from being sent to the client, which has to be done when the corresponding block entity is ready to be added because the "source" field is in the block entity data
			if (this.withheldMovingPistonBlockUpdates.get().put(blockPos, new Pair<>(blockState, flags)) != null)
			{
				throw new AssertionError();
			}
			return true;    // the code that we're intercepting doesn't check the return value anyway
		}
		else
		{
			return world.setBlockState(blockPos, blockState, flags | Block.NOTIFY_LISTENERS);
		}
	}

	@Redirect(method = {"onSyncedBlockEvent(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z", "move(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)V"))
	private void addBlockEntity(World world, BlockEntity blockEntity, @Local(ordinal = 0) BlockPos basePos)
	{
		if (!(blockEntity instanceof PistonBlockEntity))
		{
			throw new AssertionError();
		}

		BlockPos pos = blockEntity.getPos();
		Pair<BlockState, Integer> withheldMovingPistonBlockUpdate = this.withheldMovingPistonBlockUpdates.get().remove(pos);
		if (withheldMovingPistonBlockUpdate == null)
		{
			throw new AssertionError();
		}
		if (!this.withheldMovingPistonBlockUpdates.get().isEmpty())
		{
			throw new AssertionError();
		}
		world.setBlockState(pos, withheldMovingPistonBlockUpdate.getLeft(), !((PistonBlockEntity) blockEntity).isSource() ? (withheldMovingPistonBlockUpdate.getRight() | Block.NOTIFY_LISTENERS) : withheldMovingPistonBlockUpdate.getRight());

		((BedrockCompatiblePistonBlockEntity) blockEntity).setBasePos(basePos);

		world.addBlockEntity(blockEntity);
	}
}