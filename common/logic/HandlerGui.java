package loecraftpack.common.logic;

import loecraftpack.common.blocks.ContainerProjectTable;
import loecraftpack.common.blocks.TileProjectTable;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.blocks.gui.*;
import loecraftpack.common.gui.*;
import loecraftpack.ponies.inventory.ContainerEarthInventory;
import loecraftpack.ponies.inventory.ContainerSpecialEquipment;
import loecraftpack.ponies.inventory.GuiEarthPonyInventory;
import loecraftpack.ponies.inventory.GuiSpecialEquipment;
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
				case PROJECT_TABLE:
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileProjectTable)
						return new ContainerProjectTable(player.inventory, (TileProjectTable)tileEntity, world, x, y, z);
					break;
					
				case MAIN_INV:
					return player.inventoryContainer;
					
				case EQUIPMENT_INV:
					return new ContainerSpecialEquipment(player);
					
				case EARTH_INV:
					return new ContainerEarthInventory(player);
				default:
					break;
			}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID < GuiIds.values().length)
			switch(GuiIds.values()[ID])
			{
				case MONOLITH:
					TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
					if(tileEntity instanceof TileProtectionMonolith)
			            return new GuiProtectionMonolith((TileProtectionMonolith) tileEntity);
					break;
					
				case PROJECT_TABLE:
					tileEntity = world.getBlockTileEntity(x, y, z);
						if(tileEntity instanceof TileProjectTable)
							return new GuiProjectTable(player.inventory, world, x, y, z);
					break;
					
				case DIALOG:
					return new GuiDialog();
			            
				case QUEST:
		            return new GuiQuest();
		            
				case SHOP:
		            return new GuiShop();
		            
				case MAIN_INV:
					return new GuiInventory(player);
					
				case EQUIPMENT_INV:
					return new GuiSpecialEquipment(player);
					
				case EARTH_INV:
					return new GuiEarthPonyInventory(player);
					
				case BANK:
					return new GuiBank();
					
				case CREATIVE_INV:
					break;
			}
		return null;
	}
}
