package micheal65536.fountain.fabric.itembehavior.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;
import micheal65536.fountain.fabric.itembehavior.EarthItemPickup;

// allows Earth players to "pick up" mobs/animals in creative mode
@Mixin(ServerPlayerEntity.class)
public class EntityPickupMixin
{
	@Inject(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
	private void attack(Entity entity, CallbackInfo callbackInfo)
	{
		if (((EarthModePlayer) this).isEarthMode() && ((ServerPlayerEntity) (Object) this).isCreative())
		{
			if (((ServerPlayerEntity) (Object) this).getMainHandStack().isEmpty())    // TODO: need a better way to determine that the held item is not a weapon
			{
				// TODO: should probably save NBT data like sheep color, but I don't know how to map that in the bridge anyway (client app treats mobs/animals in the inventory as stackable and doesn't show stuff like their wool color?)
				ItemStack itemStack = entity.getPickBlockStack();
				if (itemStack != null && !itemStack.isEmpty())
				{
					Vec3d pos = entity.getPos();
					entity.discard();
					EarthItemPickup.giveItemToEarthPlayer((ServerPlayerEntity) (Object) this, itemStack, pos);

					callbackInfo.cancel();
				}
			}
		}
	}
}