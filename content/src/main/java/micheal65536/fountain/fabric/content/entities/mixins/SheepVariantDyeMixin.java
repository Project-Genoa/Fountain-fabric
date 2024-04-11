package micheal65536.fountain.fabric.content.entities.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.fabric.content.entities.SheepVariantEntity;

@Mixin(DyeItem.class)
public class SheepVariantDyeMixin
{
	@Inject(method = "useOnEntity(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult", at = @At(value = "HEAD"), cancellable = true)
	private void useOnEntity(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo)
	{
		if (livingEntity instanceof SheepVariantEntity && !((SheepVariantEntity) livingEntity).dyeable)
		{
			callbackInfo.setReturnValue(ActionResult.PASS);
			callbackInfo.cancel();
		}
	}
}