package micheal65536.fountain.fabric.core.itembehavior.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

// Bedrock uses use-on-block action whereas Java uses item-use action
// see BucketItemMixin for explanation
@Mixin(PlaceableOnWaterItem.class)
public class PlaceableOnWaterItemMixin
{
	private ItemUsageContext lastItemUsageContext = null;
	private boolean doRaycast;

	@Inject(method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "HEAD"), cancellable = true)
	public void useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> callbackInfo)
	{
		PlayerEntity player = itemUsageContext.getPlayer();
		if (player != null && ((EarthModePlayer) player).isEarthMode())
		{
			this.lastItemUsageContext = itemUsageContext;

			this.doRaycast = false;
			TypedActionResult<ItemStack> typedActionResult = ((PlaceableOnWaterItem) (Object) this).use(itemUsageContext.getWorld(), itemUsageContext.getPlayer(), itemUsageContext.getHand());
			if (typedActionResult.getResult() == ActionResult.FAIL)
			{
				this.doRaycast = true;
				typedActionResult = ((PlaceableOnWaterItem) (Object) this).use(itemUsageContext.getWorld(), itemUsageContext.getPlayer(), itemUsageContext.getHand());
			}

			if (typedActionResult.getResult() != ActionResult.FAIL)
			{
				itemUsageContext.getPlayer().setStackInHand(itemUsageContext.getHand(), typedActionResult.getValue().isEmpty() ? ItemStack.EMPTY : typedActionResult.getValue());
			}

			this.lastItemUsageContext = null;

			callbackInfo.setReturnValue(typedActionResult.getResult());
		}
		else
		{
			callbackInfo.setReturnValue(ActionResult.PASS);
		}
		callbackInfo.cancel();
	}

	@Redirect(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/PlaceableOnWaterItem;raycast(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;"))
	public BlockHitResult raycastHook(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling)
	{
		if (this.lastItemUsageContext != null)
		{
			if (this.doRaycast)
			{
				Vec3d start = player.getPos().add(0.0f, 1.62f, 0.0f);
				Vec3d end = this.lastItemUsageContext.getHitPos();
				double distance = start.distanceTo(end);
				end = start.lerp(end, (distance + 2.0) / distance);
				return world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
			}
			else
			{
				return new BlockHitResult(this.lastItemUsageContext.getHitPos(), this.lastItemUsageContext.getSide(), this.lastItemUsageContext.getBlockPos(), this.lastItemUsageContext.hitsInsideBlock());
			}
		}
		else
		{
			return ItemAccessor.raycast(world, player, fluidHandling);
		}
	}

	@Mixin(Item.class)
	public interface ItemAccessor
	{
		@Invoker("raycast")
		static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling)
		{
			throw new AssertionError();
		}
	}
}