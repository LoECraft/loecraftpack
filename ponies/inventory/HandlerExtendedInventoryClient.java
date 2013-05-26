package loecraftpack.ponies.inventory;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;

import loecraftpack.common.gui.GuiIds;
import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.stats.StatHandlerServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class HandlerExtendedInventoryClient extends HandlerExtendedInventoryCommon
{
	List<String> players = new ArrayList<String>();
	
	//TODO only add inventories that ThePlayer can interact with.
	public void AddPlayer(EntityPlayer player)
	{
		System.out.println("Client inv");
		
		if (!players.contains(player.username))
		{
			//special equipment
			if(!playerSpecialInv.containsKey(player.username))
			{
				System.out.println("added equip");
				playerSpecialInv.put(player.username, new SpecialInventory());
			}
			
			//extended earth inventory
			if (StatHandlerServer.isRace(player, Race.Earth))
			{
				if(!playerEarthInv.containsKey(player.username))
					playerEarthInv.put(player.username, new EarthInventory());
			}
			
			//mark as loaded
			players.add(player.username);
		}
	}
	
	/**
	 * adds custom-inventory information collected from a packet
	 */
	public void AddInventoryPiece(EntityPlayer player, InventoryGui id, int slot, ItemStack contents)
	{
		//add player if needed
		AddPlayer(player);
		
		switch (id)
		{
		case Equipment:
			playerSpecialInv.get(player.username).loadSlot(slot, contents);
			break;
			
		case EarthPony:
			if(StatHandlerServer.isRace(player, Race.Earth))
				playerEarthInv.get(player.username).loadSlot(slot, contents);
			break;
		}
	}
	
	//client version: handles lagging packets
	public CustomInventory getInventory(EntityPlayer player, InventoryGui id)
	{
		CustomInventory result;
		switch (id)
		{
		case Equipment:
			result = playerSpecialInv.get(player.username);
			if (result == null)
			{
				result = new SpecialInventory();
				playerSpecialInv.put(player.username, (SpecialInventory)result);
			}
			if (!result.validInventory())
				requestInventory(InventoryGui.Equipment, result);
			return result;
			
		case EarthPony:
			result = playerEarthInv.get(player.username);
			if (result == null)
			{
				result = new EarthInventory();
				playerEarthInv.put(player.username, (EarthInventory)result);
			}
			if (!result.validInventory())
				requestInventory(InventoryGui.EarthPony, result);
			return result;
			
		default:
			return null;
		}
	}
	
	public static void requestInventory(InventoryGui id, CustomInventory inventory)
	{
		List<Integer> slotsToRequest = inventory.getUnloaded();
		
		//tell server to re-send these packets
	}
	
	/**
	 * this tells the server to set the screen
	 */
	public static void setNewScreen(InventoryGui id)
	{
		if (!inventoryMode())
			return;
		GuiIds guiId;
		switch (id)
		{
		case Equipment:
			guiId = GuiIds.SpecialInv;
			break;
		case EarthPony:
			guiId = GuiIds.EarthInv;
			break;
		default:
			guiId = GuiIds.mainInv;
			break;
		}
		PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.subInventory, guiId.ordinal()));
	}
	
	public static boolean inventoryMode()
	{
		if(Minecraft.getMinecraft().currentScreen==null)
			return false;
		Class currentScreen = Minecraft.getMinecraft().currentScreen.getClass();
		return currentScreen == GuiInventory.class ? true :
			   currentScreen == GuiContainerCreative.class ? true :
			   currentScreen == GuiSpecialEquipment.class ? true :
			   currentScreen == GuiEarthPonyInventory.class ? true :
			   false;
	}
}
