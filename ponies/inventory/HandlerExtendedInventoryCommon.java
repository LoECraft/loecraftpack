package loecraftpack.ponies.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.client.FMLClientHandler;
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
import net.minecraft.src.ModLoader;

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
	
	public static CustomInventory getInventory(EntityPlayer player, InventoryId id)
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
}
