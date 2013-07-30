package loecraftpack.ponies.inventory;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotAmmo extends Slot
{
	public static Icon slotIcon;
	
	public SlotAmmo(IInventory inventory, int index, int xPos, int yPos)
	{
		super(inventory, index, xPos, yPos);
	}
	
	public boolean isItemValid(ItemStack itemStack)
    {
		//is ammo?
		return true;
    }
	
	@SideOnly(Side.CLIENT)
    public Icon getBackgroundIconIndex()
    {
		return slotIcon;
    }
	
	public static void registerSlotIcons(IconRegister iconRegister)
	{
		slotIcon = iconRegister.registerIcon("loecraftpack:tools/slotAmmo");
	}
}
