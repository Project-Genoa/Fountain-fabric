package micheal65536.fountain.fabric.itembehavior;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import micheal65536.fountain.fabric.earthmode.EarthModePlayer;

public class EarthItemPickup
{
	public static final Identifier FOUNTAIN_ITEM_PARTICLE_PACKET_ID = new Identifier("fountain", "item_particle");

	public static void giveItemToEarthPlayer(ServerPlayerEntity player, ItemStack itemStack, Vec3d pos, boolean showItemParticle)
	{
		if (!((EarthModePlayer) player).isEarthMode())
		{
			throw new IllegalArgumentException();
		}
		if (itemStack == null || itemStack.isEmpty())
		{
			return;
		}

		ItemStack itemStack1 = itemStack.copy();
		if (!player.getInventory().insertStack(itemStack))
		{
			// TODO: player inventory is full, that shouldn't happen if the bridge is properly transferring items to the Genoa inventory
			return;
		}

		if (showItemParticle)
		{
			// TODO: send item particles to all Earth players
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeItemStack(itemStack1);
			buf.writeVec3d(pos);
			ServerPlayNetworking.send(player, FOUNTAIN_ITEM_PARTICLE_PACKET_ID, buf);
		}
	}
}