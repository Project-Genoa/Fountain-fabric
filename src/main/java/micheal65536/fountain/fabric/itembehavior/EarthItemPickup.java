package micheal65536.fountain.fabric.itembehavior;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

public class EarthItemPickup
{
	public static void giveItemToEarthPlayer(ServerPlayerEntity player, ItemStack itemStack, Vec3d pos)
	{
		if (!((EarthModePlayer) player).isEarthMode())
		{
			throw new IllegalArgumentException();
		}
		if (itemStack == null || itemStack.isEmpty())
		{
			return;
		}

		if (!player.getInventory().insertStack(itemStack))
		{
			// TODO: player inventory is full, that shouldn't happen if the bridge is properly transferring items to the Genoa inventory
			return;
		}

		// TODO: send custom packet for Earth item pickup animation
	}
}