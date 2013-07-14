package loecraftpack.ponies.inventory;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.StatHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class HandlerExtendedInventoryServer
{
	static Map<String, InventoryEquipment> playerEquipmentInv = new HashMap<String, InventoryEquipment>();
	static Map<String, InventoryEarth> playerEarthInv = new HashMap<String, InventoryEarth>();
	
	/**
	 * reads the player's NBT data
	 */
	public static void AddPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		playerEquipmentInv.put(player.username, new InventoryEquipment(nbt));
		
		if (LoECraftPack.statHandler.isRace(player, Race.EARTH))
			playerEarthInv.put(player.username, new InventoryEarth(nbt));
	}
	
	/**
	 * save the player's NBT data
	 */
	public static void SavePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		
		InventoryEquipment special = playerEquipmentInv.get(player.username);
		if (special!= null)
		{
			special.writeToNBT(nbt);
		}
		
		if (LoECraftPack.statHandler.isRace(player, Race.EARTH))
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
		case EQUIPMENT:
			result = playerEquipmentInv.get(player.username);
			if (result == null)
			{   
				System.out.println("load");
				result = new InventoryEquipment();
				playerEquipmentInv.put(player.username, (InventoryEquipment)result);
			}
			return result;
		case EARTH_PONY:
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
	
	public static GuiIds getNextInv(EntityPlayer player, GuiIds oldId)
	{
		switch (oldId)
		{
		case MAIN_INV:
		case CREATIVE_INV:
			return GuiIds.EQUIPMENT_INV;
			
		case EQUIPMENT_INV:
			if (LoECraftPack.statHandler.isRace(player, Race.EARTH))
				return GuiIds.EARTH_INV;
			else
				return GuiIds.MAIN_INV;
			
		case EARTH_INV:
			return GuiIds.MAIN_INV;
		
		default:
			return null;
		}
	}
	
	public static boolean compareInvId(EntityPlayer player, GuiIds clientId)
	{
		GuiIds current = getCurrentInvId(player);
		if (clientId==GuiIds.CREATIVE_INV && current==GuiIds.MAIN_INV)
			return true;
		else
			return current == clientId;
	}
	
	public static GuiIds getCurrentInvId(EntityPlayer player)
	{
		Class current = player.openContainer.getClass();
		if (current == ContainerPlayer.class)
			return GuiIds.MAIN_INV;
		if (current == ContainerSpecialEquipment.class)
			return GuiIds.EQUIPMENT_INV;
		if (current == ContainerEarthInventory.class)
			return GuiIds.EARTH_INV;
		return null;
	}
}
