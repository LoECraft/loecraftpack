package loecraftpack.ponies.inventory;

import java.util.HashMap;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.StatHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class HandlerExtendedInventoryServer
{
	static Map<String, InventorySpecial> playerSpecialInv = new HashMap<String, InventorySpecial>();
	static Map<String, InventoryEarth> playerEarthInv = new HashMap<String, InventoryEarth>();
	
	/**
	 * Called by Common Class
	 */
	public static void AddPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		playerSpecialInv.put(player.username, new InventorySpecial(nbt));
		
		if (StatHandlerServer.isRace(player, Race.Earth))
			playerEarthInv.put(player.username, new InventoryEarth(nbt));
	}
	
	/**
	 * Called by Common Class
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
	
	public static boolean canUseInv(EntityPlayer player, GuiIds id)
	{
		switch (id)
		{
		case EarthInv:
			if (LoECraftPack.StatHandler.isRace(player, Race.Earth))
				return true;
			return false;
		default:
			return true;
		}
		
	}
}
