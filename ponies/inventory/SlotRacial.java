package loecraftpack.ponies.inventory;

import loecraftpack.LoECraftPack;
import loecraftpack.common.items.IRacialItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRacial extends Slot {
	
	EntityPlayer player;

	public SlotRacial(IInventory par1iInventory, int par2, int par3, int par4, EntityPlayer entityPlayer) {
		super(par1iInventory, par2, par3, par4);
		player = entityPlayer;
	}

	public int getSlotStackLimit()
    {
        return 1;
    }
	
	public boolean isItemValid(ItemStack itemStack)
    {
		if (itemStack == null)
			return false;
		if (itemStack.getItem() == null)
			return false;
		if (itemStack.getItem() instanceof IRacialItem &&
		    ((IRacialItem)itemStack.getItem()).canBeUsedBy(LoECraftPack.StatHandler.getRace(player)))
			return true;
        return false;
    }
}
