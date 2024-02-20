package micheal65536.fountain.fabric.core.noteblock.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public class NoteBlockMixin
{
	// Ignore the block above and determine instrument using only the block below as in older versions of Minecraft
	@Inject(method = "getStateWithInstrument(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;", at = @At(value = "HEAD"), cancellable = true)
	private void getStateWithInstrument(WorldAccess worldAccess, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<BlockState> callbackInfo)
	{
		Instrument instrument = worldAccess.getBlockState(blockPos.down()).getInstrument();
		if (instrument.isNotBaseBlock())
		{
			instrument = Instrument.HARP;
		}
		callbackInfo.setReturnValue(blockState.with(Properties.INSTRUMENT, instrument));
		callbackInfo.cancel();
	}
}