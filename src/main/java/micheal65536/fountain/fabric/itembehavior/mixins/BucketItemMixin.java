package micheal65536.fountain.fabric.itembehavior.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

// makes Java buckets behave like Bedrock for Earth players
// Bedrock uses use-on-block action for buckets whereas Java uses item-use action
@Mixin(BucketItem.class)
public class BucketItemMixin
{
	private ItemUsageContext lastItemUsageContext = null;

	public ActionResult useOnBlock(ItemUsageContext itemUsageContext)
	{
		PlayerEntity player = itemUsageContext.getPlayer();
		if (player != null && ((EarthModePlayer) player).isEarthMode())
		{
			this.lastItemUsageContext = itemUsageContext;
			TypedActionResult<ItemStack> typedActionResult = ((BucketItem) (Object) this).use(itemUsageContext.getWorld(), itemUsageContext.getPlayer(), itemUsageContext.getHand());
			if (typedActionResult.getResult() != ActionResult.FAIL)
			{
				itemUsageContext.getPlayer().setStackInHand(itemUsageContext.getHand(), typedActionResult.getValue().isEmpty() ? ItemStack.EMPTY : typedActionResult.getValue());
			}
			this.lastItemUsageContext = null;
			return typedActionResult.getResult();
		}
		else
		{
			return ActionResult.PASS;
		}
	}

	@Redirect(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BucketItem;raycast(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;"))
	public BlockHitResult raycastHook(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling)
	{
		if (this.lastItemUsageContext != null)
		{
			Vec3d start = player.getPos();
			Vec3d end = this.lastItemUsageContext.getHitPos();
			double distance = start.distanceTo(end);
			end = start.lerp(end, (distance + 2.0) / distance);
			return world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
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