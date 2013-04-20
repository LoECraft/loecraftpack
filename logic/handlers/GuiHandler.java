package tekner.loecraftpack.logic.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tekner.loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import tekner.loecraftpack.gui.DialogGUI;
import tekner.loecraftpack.gui.ProtectionMonolithGUI;
import tekner.loecraftpack.gui.QuestGUI;
import tekner.loecraftpack.gui.ShopGUI;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
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
			if(tileEntity instanceof ProtectionMonolithTileEntity)
			{
	            return new ProtectionMonolithGUI((ProtectionMonolithTileEntity) tileEntity);
			}
			
		case 1:
			return new DialogGUI();
	            
		case 2:
            return new QuestGUI();
            
		case 3:
            return new ShopGUI();
		}
		return null;
	}
}
