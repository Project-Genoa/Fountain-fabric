package micheal65536.fountain.fabric.core.itembehavior.mixins;

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

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

// makes Java buckets behave like Bedrock for Earth players
// Bedrock uses use-on-block action for buckets whereas Java uses item-use action
@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item
{
	private BucketItemMixin(Settings settings)
	{
		super(settings);
	}

	private ItemUsageContext lastItemUsageContext = null;
	private boolean doRaycast;

	@Override
	public ActionResult useOnBlock(ItemUsageContext itemUsageContext)
	{
		PlayerEntity player = itemUsageContext.getPlayer();
		if (player != null && ((EarthModePlayer) player).isEarthMode())
		{
			this.lastItemUsageContext = itemUsageContext;

			// we first try to perform the item use using the block provided by the client, if that fails we try again using a raycast
			// the reason for this is because Earth sometimes sends the water block that the bucket is used on and other times seems to send the solid block behind it
			// when it sends the water block then the client will also predict it and so in that case we want to use the block sent by the client because the raycast result doesn't always exactly match the client-side prediction and this will cause a mismatch between client and server
			// when it sends the block behind then no client-side prediction takes place and on the server side the first attempt to use the bucket will fail so we try again using the raycast result and it doesn't matter what the raycast returns because the client doesn't predict it anyway in this case
			this.doRaycast = false;
			TypedActionResult<ItemStack> typedActionResult = ((BucketItem) (Object) this).use(itemUsageContext.getWorld(), itemUsageContext.getPlayer(), itemUsageContext.getHand());
			if (typedActionResult.getResult() == ActionResult.FAIL)
			{
				this.doRaycast = true;
				typedActionResult = ((BucketItem) (Object) this).use(itemUsageContext.getWorld(), itemUsageContext.getPlayer(), itemUsageContext.getHand());
			}

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