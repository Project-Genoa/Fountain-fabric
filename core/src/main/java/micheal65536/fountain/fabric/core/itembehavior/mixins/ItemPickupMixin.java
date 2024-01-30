package micheal65536.fountain.fabric.core.itembehavior.mixins;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

// allows Earth players to pick up item entities by tapping on them
@Mixin(ItemEntity.class)
public class ItemPickupMixin
{
	public ActionResult interact(PlayerEntity player, Hand hand)
	{
		if (((EarthModePlayer) player).isEarthMode())
		{
			((ItemEntity) (Object) this).onPlayerCollision(player);
			return ActionResult.CONSUME;
		}
		else
		{
			return ActionResult.PASS;
		}
	}
}