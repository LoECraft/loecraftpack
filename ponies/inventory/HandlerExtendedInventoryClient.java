package loecraftpack.ponies.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.minecraft.nbt.NBTTagCompound;

public class HandlerExtendedInventoryClient
{
	static Map<String, InventorySpecial> playerSpecialInv = new HashMap<String, InventorySpecial>();
	static Map<String, InventoryEarth> playerEarthInv = new HashMap<String, InventoryEarth>();
	static List<String> players = new ArrayList<String>();
	
	
	/**
	 * Called by Common Class
	 */
	public static void AddPlayer(EntityPlayer player)
	{
		if (!players.contains(player.username))
		{
			if (player.entityId == Minecraft.getMinecraft().thePlayer.entityId)
			{
				//special equipment
				if(!playerSpecialInv.containsKey(player.username))
				{
					System.out.println("added equip");
					playerSpecialInv.put(player.username, new InventorySpecial());
				}
				
				//extended earth inventory
				if (StatHandlerServer.isRace(player, Race.Earth))
				{
					if(!playerEarthInv.containsKey(player.username))
						playerEarthInv.put(player.username, new InventoryEarth());
				}
			}
			else
			{
				//NOTE: only add foreign inventories that ThePlayer can interact with.
				
				//special equipment
				if(!playerSpecialInv.containsKey(player.username))
				{
					System.out.println("added equip");
					playerSpecialInv.put(player.username, new InventorySpecial());
				}
				
				//mark as loaded
				players.add(player.username);
			}
		}
	}
	
	/**
	 * Called by Common Class
	 */
	public static void removePlayer(EntityPlayer player)
	{
		playerSpecialInv.remove(player.username);
		playerEarthInv.remove(player.username);
		players.remove(player.username);
	}
	
	/**
	 * Called by Common Class
	 */
	public static InventoryCustom getInventory(EntityPlayer player, InventoryId id)
	{
		InventoryCustom result;
		switch (id)
		{
		case Equipment:
			result = playerSpecialInv.get(player.username);
			if (result == null)
			{
				result = new InventorySpecial();
				playerSpecialInv.put(player.username, (InventorySpecial)result);
			}
			return result;
			
		case EarthPony:
			result = playerEarthInv.get(player.username);
			if (result == null)
			{
				result = new InventoryEarth();
				playerEarthInv.put(player.username, (InventoryEarth)result);
			}
			return result;
			
		default:
			return null;
		}
	}
	
	/**
	 * this tells the server to set the screen
	 */
	public static void setNewScreen(GuiIds id)
	{
		if (!inventoryMode())
			return;
		PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.subInventory, id.ordinal()));
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
