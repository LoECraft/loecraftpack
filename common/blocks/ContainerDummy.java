package loecraftpack.common.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

//The sole purpose of this is to make the Project Table work with vanilla code...
public class ContainerDummy extends Container
{
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return false;
	}
}
