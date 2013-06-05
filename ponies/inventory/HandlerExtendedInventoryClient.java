package loecraftpack.ponies.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.network.PacketDispatcher;

import loecraftpack.LoECraftPack;
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
	
	/**
	 * this sets in motion, the events to cycle the player's inventory.
	 */
	public static void cycleInventory()
	{
		if (Minecraft.getMinecraft().currentScreen != null)
		{
			Class gui = Minecraft.getMinecraft().currentScreen.getClass();
			GuiIds id = Minecraft.getMinecraft().playerController.isInCreativeMode()? GuiIds.creativeInv : GuiIds.mainInv;
			boolean flag = false;
			if (gui == GuiInventory.class || gui == GuiContainerCreative.class)
			{
				id = GuiIds.SpecialInv;
				flag = true;
			}
			else if (gui == GuiSpecialEquipment.class)
			{
				if (LoECraftPack.StatHandler.isRace(Minecraft.getMinecraft().thePlayer, Race.Earth))
					id = GuiIds.EarthInv;
				flag = true;
			}
			else if (gui == GuiEarthPonyInventory.class)
			{
				flag = true;
			}
			if (flag)
			{
				if(id == GuiIds.creativeInv)
					Minecraft.getMinecraft().displayGuiScreen(new GuiContainerCreative(Minecraft.getMinecraft().thePlayer));
				
				setNewScreen(id);
			}
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
	
	/**
	 * confirms that the player is in inventory mode.
	 */
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
