package micheal65536.fountain.fabric.itembehavior.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

// makes Earth players use items like food or bows instantly
@Mixin(ServerPlayerInteractionManager.class)
public class InstantItemUseMixin
{
	private static final int ITEM_USE_TIME_CAP = 1200;    // cap item usage duration to 1 minute (items like bows have high usage durations after which they are consumed instead of used in the normal way)

	@Inject(method = "interactItem(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At(value = "HEAD"), cancellable = true)
	private void interactItem(ServerPlayerEntity player, World world, ItemStack itemStack, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo)
	{
		if (((EarthModePlayer) player).isEarthMode())
		{
			ServerPlayerInteractionManager serverPlayerInteractionManager = (ServerPlayerInteractionManager) (Object) this;
			callbackInfo.setReturnValue(this.interactItemEarthMode(serverPlayerInteractionManager, player, world, itemStack, hand));
			callbackInfo.cancel();
		}
	}

	private ActionResult interactItemEarthMode(ServerPlayerInteractionManager interactionManager, ServerPlayerEntity player, World world, ItemStack itemStack, Hand hand)
	{
		if (interactionManager.getGameMode() != GameMode.CREATIVE && interactionManager.getGameMode() != GameMode.SURVIVAL)
		{
			return ActionResult.PASS;
		}

		TypedActionResult<ItemStack> typedActionResult = itemStack.use(world, player, hand);
		ItemStack newItemStack = typedActionResult.getValue();
		ActionResult actionResult = typedActionResult.getResult();

		if (actionResult != ActionResult.FAIL)
		{
			player.setStackInHand(hand, newItemStack.isEmpty() ? ItemStack.EMPTY : newItemStack);

			if (itemStack.getMaxUseTime() > 0)
			{
				while (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getItemUseTime() < ITEM_USE_TIME_CAP)
				{
					((LivingEntityAccessor) player).callTickActiveItemStack();
				}
				player.stopUsingItem();
			}
		}

		return actionResult;
	}

	@Mixin(LivingEntity.class)
	public interface LivingEntityAccessor
	{
		@Invoker("tickActiveItemStack")
		void callTickActiveItemStack();
	}
}