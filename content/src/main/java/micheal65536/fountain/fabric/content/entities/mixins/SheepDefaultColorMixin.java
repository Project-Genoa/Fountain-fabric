package micheal65536.fountain.fabric.content.entities.mixins;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// make vanilla/non-fixed-color sheep always spawn as white
@Mixin(SheepEntity.class)
public class SheepDefaultColorMixin
{
	@Inject(method = "generateDefaultColor(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/util/DyeColor;", at = @At(value = "HEAD"), cancellable = true)
	private static void generateDefaultColor(Random random, CallbackInfoReturnable<DyeColor> callbackInfo)
	{
		callbackInfo.setReturnValue(DyeColor.WHITE);
		callbackInfo.cancel();
	}
}