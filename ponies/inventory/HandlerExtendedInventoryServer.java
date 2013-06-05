package loecraftpack.ponies.inventory;

import java.util.HashMap;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.StatHandlerServer;
import net.minecraft.client.gui.inventory.ContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class HandlerExtendedInventoryServer
{
	static Map<String, InventorySpecial> playerSpecialInv = new HashMap<String, InventorySpecial>();
	static Map<String, InventoryEarth> playerEarthInv = new HashMap<String, InventoryEarth>();
	
	/**
	 * reads the player's NBT data
	 */
	public static void AddPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		playerSpecialInv.put(player.username, new InventorySpecial(nbt));
		
		if (StatHandlerServer.isRace(player, Race.Earth))
			playerEarthInv.put(player.username, new InventoryEarth(nbt));
	}
	
	/**
	 * save the player's NBT data
	 */
	public static void SavePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		InventorySpecial special = playerSpecialInv.get(player.username);
		if (special!= null)
		{
			special.writeToNBT(nbt);
		}
		
		if (StatHandlerServer.isRace(player, Race.Earth))
		{
			InventoryEarth earth = playerEarthInv.get(player.username);
			if (earth!=null)
			{
				earth.writeToNBT(nbt);
			}
		}
	}
	
	/**
	 * Called by Common Class, gets the player's custom inventory
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
	
	public static boolean canUseNextInv(EntityPlayer player, GuiIds newId)
	{		
		GuiIds oldId = getCurrentInvId(player);
		
		switch (oldId)
		{
		case mainInv:
		case creativeInv:
			if (newId == GuiIds.SpecialInv)
				return true;
			return false;
		case SpecialInv:
			if (LoECraftPack.StatHandler.isRace(player, Race.Earth))
			{
				if (newId == GuiIds.EarthInv)
					return true;
				return false;
			}
			else if(newId == GuiIds.mainInv || newId == GuiIds.creativeInv)
				return true;
			return false;
		case EarthInv:
			if (newId == GuiIds.mainInv || newId == GuiIds.creativeInv)
				return true;
			return false;
		
		default:
			return false;
		}
	}
	
	public static GuiIds getCurrentInvId(EntityPlayer player)
	{
		Class current = player.openContainer.getClass();
		if (current == ContainerPlayer.class)
			return GuiIds.mainInv;
		if (current == ContainerCreative.class)
			return GuiIds.creativeInv;
		if (current == ContainerSpecialEquipment.class)
			return GuiIds.SpecialInv;
		if (current == ContainerEarthInventory.class)
			return GuiIds.EarthInv;
		return null;
	}
}
