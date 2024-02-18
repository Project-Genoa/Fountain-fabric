package micheal65536.fountain.fabric.core.inventory;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import micheal65536.fountain.fabric.core.Main;
import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

import java.util.LinkedList;

// provides network packets to flush the player's inventory to the bridge and to set the player's hotbar from the bridge
// TODO: it might be nicer to use a push system rather than a polling system for inventory sync, but that requires hooking all the places where items are added to the player's inventory
public class InventorySync
{
	public static final Identifier FOUNTAIN_INVENTORY_SYNC_REQUEST_PACKET_ID = new Identifier("fountain", "inventory_sync_request");
	public static final Identifier FOUNTAIN_INVENTORY_SYNC_RESPONSE_PACKET_ID = new Identifier("fountain", "inventory_sync_response");
	public static final Identifier FOUNTAIN_SET_HOTBAR_REQUEST_PACKET_ID = new Identifier("fountain", "set_hotbar_request");
	public static final Identifier FOUNTAIN_SET_HOTBAR_RESPONSE_PACKET_ID = new Identifier("fountain", "set_hotbar_response");

	public static final ServerPlayNetworking.PlayChannelHandler INVENTORY_SYNC_REQUEST_CHANNEL_HANDLER = (server, player, handler, buf, responseSender) ->
	{
		server.submit(() ->
		{
			if (!((EarthModePlayer) player).isEarthMode())
			{
				Main.LOGGER.warn("Inventory sync request for player not in Earth mode");
				return;
			}

			boolean clearHotbar = buf.readBoolean();

			PlayerInventory playerInventory = player.getInventory();

			LinkedList<ItemStack> itemStacks = new LinkedList<>();
			for (int slotIndex = 7; slotIndex < 36; slotIndex++)
			{
				ItemStack itemStack = playerInventory.getStack(slotIndex);
				if (!itemStack.isEmpty())
				{
					itemStacks.add(itemStack);
					playerInventory.setStack(slotIndex, ItemStack.EMPTY);
				}
			}

			PacketByteBuf sendBuf = PacketByteBufs.create();
			sendBuf.writeInt(itemStacks.size());
			for (ItemStack itemStack : itemStacks)
			{
				sendBuf.writeItemStack(itemStack);
			}
			for (int slotIndex = 0; slotIndex < 7; slotIndex++)
			{
				ItemStack itemStack = playerInventory.getStack(slotIndex);
				sendBuf.writeItemStack(itemStack);
				if (clearHotbar)
				{
					playerInventory.setStack(slotIndex, ItemStack.EMPTY);
				}
			}
			ServerPlayNetworking.send(player, FOUNTAIN_INVENTORY_SYNC_RESPONSE_PACKET_ID, sendBuf);
		});
	};

	public static final ServerPlayNetworking.PlayChannelHandler SET_HOTBAR_REQUEST_CHANNEL_HANDLER = (server, player, handler, buf, responseSender) ->
	{
		server.submit(() ->
		{
			if (!((EarthModePlayer) player).isEarthMode())
			{
				Main.LOGGER.warn("Set hotbar request for player not in Earth mode");
				return;
			}

			PlayerInventory playerInventory = player.getInventory();

			boolean initial = buf.readBoolean();
			if (initial)
			{
				for (int slotIndex = 0; slotIndex < 36; slotIndex++)
				{
					playerInventory.setStack(slotIndex, ItemStack.EMPTY);
				}

				for (int slotIndex = 0; slotIndex < 7; slotIndex++)
				{
					ItemStack itemStack = buf.readItemStack();
					playerInventory.setStack(slotIndex, itemStack);
				}
			}
			else
			{
				boolean isEmpty = true;
				for (int slotIndex = 0; slotIndex < 7; slotIndex++)
				{
					if (!playerInventory.getStack(slotIndex).isEmpty())
					{
						Main.LOGGER.debug("Rejecting set hotbar request because hotbar is not empty");
						isEmpty = false;
						break;
					}
				}

				if (isEmpty)
				{
					for (int slotIndex = 0; slotIndex < 7; slotIndex++)
					{
						ItemStack itemStack = buf.readItemStack();
						playerInventory.setStack(slotIndex, itemStack);
					}
				}

				boolean success = isEmpty;
				PacketByteBuf sendBuf = PacketByteBufs.create();
				sendBuf.writeBoolean(success);
				ServerPlayNetworking.send(player, FOUNTAIN_SET_HOTBAR_RESPONSE_PACKET_ID, sendBuf);
			}
		});
	};
}