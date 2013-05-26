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

public class HandlerExtendedInventoryCommon
{
	Map<String, SpecialInventory> playerSpecialInv = new HashMap<String, SpecialInventory>();
	Map<String, EarthInventory> playerEarthInv = new HashMap<String, EarthInventory>();
	
	//server code, has clientMP override
	public void AddPlayer(EntityPlayer player)
	{
		System.out.println("Common inv");
		
		NBTTagCompound nbt = player.getEntityData();
		
		playerSpecialInv.put(player.username, new SpecialInventory(nbt));
		
		if (StatHandlerServer.isRace(player, Race.Earth))
			playerEarthInv.put(player.username, new EarthInventory(nbt));
	}
	
	//server code
	public void SavePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		SpecialInventory special = playerSpecialInv.get(player.username);
		special.writeToNBT(nbt);
		
		if (StatHandlerServer.isRace(player, Race.Earth))
		{
			EarthInventory earth = playerEarthInv.get(player.username);
			earth.writeToNBT(nbt);
		}
	}
	
	//server code, has clientMP override
	public CustomInventory getInventory(EntityPlayer player, InventoryGui id)
	{
		CustomInventory result;
		switch (id)
		{
		case Equipment:
			result = playerSpecialInv.get(player.username);
			if (result == null)
				return new SpecialInventory();
			else
				return result;
		case EarthPony:
			result = playerEarthInv.get(player.username);
			if (result == null)
				return new EarthInventory();
			else
				return result;
		default:
			return null;
		}
	}
}
