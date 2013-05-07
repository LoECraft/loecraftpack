package loecraftpack.common.logic;

import loecraftpack.common.blocks.ContainerProjectTable;
import loecraftpack.common.blocks.TileProjectTable;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.blocks.gui.GuiProjectTable;
import loecraftpack.common.blocks.gui.GuiProtectionMonolith;
import loecraftpack.common.gui.GuiDialog;
import loecraftpack.common.gui.GuiQuest;
import loecraftpack.common.gui.GuiShop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class HandlerGui implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
			case 1:
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if(tileEntity instanceof TileProjectTable)
					return new ContainerProjectTable(player.inventory, (TileProjectTable)tileEntity, world, x, y, z);
				break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
		case 0:
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity instanceof TileProtectionMonolith)
	            return new GuiProtectionMonolith((TileProtectionMonolith) tileEntity);
			break;
			
		case 1:
			tileEntity = world.getBlockTileEntity(x, y, z);
				if(tileEntity instanceof TileProjectTable)
					return new GuiProjectTable(player.inventory, world, x, y, z);
			break;
			
		case 2:
			return new GuiDialog();
	            
		case 3:
            return new GuiQuest();
            
		case 4:
            return new GuiShop();
		}
		return null;
	}
}
