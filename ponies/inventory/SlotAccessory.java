package loecraftpack.ponies.inventory;

import loecraftpack.common.items.ItemNecklace;
import loecraftpack.common.items.ItemRing;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotAccessory extends Slot
{
	int type;
	
	public SlotAccessory(IInventory inventory, int index, int xPos, int yPos, int type)
	{
		super(inventory, index, xPos, yPos);
		this.type = type;
	}
	
	public int getSlotStackLimit()
    {
        return 1;
    }
	
	public boolean isItemValid(ItemStack itemStack)
    {
		//is Accessory?
		return true;
    }
	
	@SideOnly(Side.CLIENT)
    public Icon getBackgroundIconIndex()
    {
		switch (type)
		{
		case 0:
			return ItemNecklace.slotIcon;
		case 1:
			return ItemRing.slotIcon;
		default:
			return null;
		}
        
    }
}
