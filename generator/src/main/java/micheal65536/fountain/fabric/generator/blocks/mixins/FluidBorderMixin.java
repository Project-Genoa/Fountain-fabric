package micheal65536.fountain.fabric.generator.blocks.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.fabric.generator.blocks.NonReplaceableAirBlock;

// prevents fluids from flowing into the non-replaceable air blocks
@Mixin(FlowableFluid.class)
public class FluidBorderMixin
{
	@Inject(method = "canFill(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/Fluid;)Z", at = @At(value = "RETURN"), cancellable = true)
	private void canFill(BlockView blockView, BlockPos blockPos, BlockState blockState, Fluid fluid, CallbackInfoReturnable<Boolean> callbackInfo)
	{
		if (blockState.getBlock() instanceof NonReplaceableAirBlock)
		{
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
		}
	}
}