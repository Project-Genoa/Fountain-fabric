package micheal65536.fountain.fabric.generator.blocks.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.generator.blocks.NonReplaceableAirBlock;

@Mixin(Heightmap.class)
public class HeightmapAirMixin
{
	@Redirect(method = "method_16682(Lnet/minecraft/block/BlockState;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isAir()Z"))
	private static boolean isAir(BlockState blockState)
	{
		if (blockState.getBlock() instanceof NonReplaceableAirBlock)
		{
			return true;
		}
		else
		{
			return blockState.isAir();
		}
	}
}