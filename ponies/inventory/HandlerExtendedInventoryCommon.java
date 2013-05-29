package loecraftpack.ponies.inventory;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.common.items.ItemAccessory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class HandlerExtendedInventoryCommon
{
	public static void AddPlayer(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
			HandlerExtendedInventoryClient.AddPlayer(player);
		else
		{
			HandlerExtendedInventoryServer.AddPlayer(player);
			//single player
			if (Minecraft.getMinecraft()!= null)
				if (Minecraft.getMinecraft().thePlayer != null)
					if (Minecraft.getMinecraft().thePlayer.entityId == player.entityId)
						HandlerExtendedInventoryClient.AddPlayer(player);
		}
	}
	
	public static void removePlayer(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
			HandlerExtendedInventoryClient.removePlayer(player);
		else
		{
			HandlerExtendedInventoryServer.SavePlayer(player);
			//single player
			HandlerExtendedInventoryClient.removePlayer(player);
		}
	}
	
	public static InventoryCustom getInventory(EntityPlayer player, InventoryId id)
	{
		if(player.worldObj.isRemote)
		{
			return HandlerExtendedInventoryClient.getInventory(player, id);
		}
		else
		{
			return HandlerExtendedInventoryServer.getInventory(player, id);
		}
	}
	
	/**
	 * get the inventory position of any existing Accessories
	 */
	public static List<Integer> getAccessorySlotIds(EntityPlayer player, InventoryCustom inv)
	{
		List<Integer> accessorySlotIds = new ArrayList<Integer>();
		ItemStack targetItem;
		for(int i=0; i<inv.getSizeInventory(); i++)
		{
			targetItem = inv.getStackInSlot(i);
			if (targetItem != null && (targetItem.getItem() instanceof ItemAccessory))
				accessorySlotIds.add(i);
		}
		if (accessorySlotIds.size()>0)
			return accessorySlotIds;
		return null;
	}
}
