package loecraftpack.common.logic;

import loecraftpack.common.blocks.ContainerProjectTable;
import loecraftpack.common.blocks.TileProjectTable;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.blocks.gui.GuiProjectTable;
import loecraftpack.common.blocks.gui.GuiProtectionMonolith;
import loecraftpack.common.gui.GuiDialog;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.common.gui.GuiQuest;
import loecraftpack.common.gui.GuiShop;
import loecraftpack.ponies.inventory.ContainerEarthInventory;
import loecraftpack.ponies.inventory.ContainerSpecialEquipment;
import loecraftpack.ponies.inventory.GuiEarthPonyInventory;
import loecraftpack.ponies.inventory.GuiSpecialEquipment;
import net.minecraft.client.gui.inventory.ContainerCreative;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class HandlerGui implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID < GuiIds.values().length)
			switch(GuiIds.values()[ID])
			{
				case ProjectTable:
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileProjectTable)
						return new ContainerProjectTable(player.inventory, (TileProjectTable)tileEntity, world, x, y, z);
					break;
					
				case mainInv:
					return player.inventoryContainer;
					
				case creativeInv:
					return new ContainerCreative(player);
					
				case SpecialInv:
					return new ContainerSpecialEquipment(player);
					
				case EarthInv:
					return new ContainerEarthInventory(player);
			}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID < GuiIds.values().length)
			switch(GuiIds.values()[ID])
			{
			case Monolith:
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if(tileEntity instanceof TileProtectionMonolith)
		            return new GuiProtectionMonolith((TileProtectionMonolith) tileEntity);
				break;
				
			case ProjectTable:
				tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileProjectTable)
						return new GuiProjectTable(player.inventory, world, x, y, z);
				break;
				
			case Dialog:
				return new GuiDialog();
		            
			case Quest:
	            return new GuiQuest();
	            
			case Shop:
	            return new GuiShop();
	            
			case mainInv:
				return new GuiInventory(player);
				
			case creativeInv:
				return new GuiContainerCreative(player);
				
			case SpecialInv:
				return new GuiSpecialEquipment(player);
				
			case EarthInv:
				return new GuiEarthPonyInventory(player);
				
			}
		return null;
	}
}
