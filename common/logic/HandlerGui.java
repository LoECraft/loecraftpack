package loecraftpack.common.logic;

import loecraftpack.common.blocks.TileProtectionMonolith;
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
			{
	            return new GuiProtectionMonolith((TileProtectionMonolith) tileEntity);
			}
			
		case 1:
			return new GuiDialog();
	            
		case 2:
            return new GuiQuest();
            
		case 3:
            return new GuiShop();
		}
		return null;
	}
}
