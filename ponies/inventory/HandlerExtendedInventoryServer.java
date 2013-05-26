package loecraftpack.ponies.inventory;

import java.util.HashMap;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.StatHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.Player;

public class HandlerExtendedInventoryServer
{
	private static Map<String, SpecialInventory> playerSpecialInv = new HashMap<String, SpecialInventory>();
	private static Map<String, EarthInventory> playerEarthInv = new HashMap<String, EarthInventory>();
	
	//server code, has clientMP override
	public static void AddPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		playerSpecialInv.put(player.username, new SpecialInventory(nbt));
		
		if (StatHandlerServer.isRace(player, Race.Earth))
			playerEarthInv.put(player.username, new EarthInventory(nbt));
	}
	
	//server code
	public static void SavePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		SpecialInventory special = playerSpecialInv.get(player.username);
		if (special!= null)
		{
			special.writeToNBT(nbt);
		}
		
		if (StatHandlerServer.isRace(player, Race.Earth))
		{
			EarthInventory earth = playerEarthInv.get(player.username);
			if (earth!=null)
			{
				earth.writeToNBT(nbt);
			}
		}
	}
	
	//server code, has clientMP override
	public static CustomInventory getInventory(EntityPlayer player, InventoryId id)
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
			return result;
		case EarthPony:
			result = playerEarthInv.get(player.username);
			if (result == null)
			{
				result = new EarthInventory();
				playerEarthInv.put(player.username, (EarthInventory)result);
			}
				return result;
		default:
			return null;
		}
	}
}
