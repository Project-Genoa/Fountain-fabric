package micheal65536.fountain.fabric.itembehavior.mixins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

// consumes the item when an item is used on a block by Earth players in creative mode the same as in survival mode
@Mixin(ServerPlayerInteractionManager.class)
public class InteractBlockConsumeItemMixin
{
	@Redirect(method = "interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;isCreative()Z"))
	private boolean isCreative(ServerPlayerInteractionManager serverPlayerInteractionManager)
	{
		if (((EarthModePlayer) ((ServerPlayerInteractionManagerAccessor) serverPlayerInteractionManager).getPlayer()).isEarthMode())
		{
			return false;
		}
		else
		{
			return serverPlayerInteractionManager.isCreative();
		}
	}

	@Mixin(ServerPlayerInteractionManager.class)
	public interface ServerPlayerInteractionManagerAccessor
	{
		@Accessor("player")
		ServerPlayerEntity getPlayer();
	}
}