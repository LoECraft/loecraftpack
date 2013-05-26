package loecraftpack.ponies.inventory;

import loecraftpack.LoECraftPack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHeart extends Slot {

	public SlotHeart(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	public int getSlotStackLimit()
    {
        return 1;
    }
	
	public boolean isItemValid(ItemStack itemStack)
    {
        return itemStack != null ? itemStack.itemID == LoECraftPack.itemCrystalHeart.itemID : false;
    }

}
