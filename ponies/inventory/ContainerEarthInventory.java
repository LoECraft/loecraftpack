package loecraftpack.ponies.inventory;

import loecraftpack.LoECraftPack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEarthInventory extends Container {
	
	final static int slotCount = 27;
	
	IInventory normalInv;
	InventoryEarth earthInv;
	
	public ContainerEarthInventory(EntityPlayer entityPlayer)
	{
		normalInv = entityPlayer.inventory;
		earthInv = (InventoryEarth)HandlerExtendedInventoryCommon.getInventory(entityPlayer, InventoryId.EarthPony);
		
		int v;
        int h;
        
		for (v = 0; v < 3; v++)
        {
            for (h = 0; h < 9; h++)
            {
            	this.addSlotToContainer(new Slot(earthInv, h + v * 9, 8 + h * 18, 18 + v * 18));
            }
        }
		
		for (v = 0; v < 3; ++v)
        {
            for (h = 0; h < 9; h++)
            {
            	this.addSlotToContainer(new Slot(normalInv, h + v * 9 + 9, 8 + h * 18, 84 + v * 18));
            }
        }
		
		for (h = 0; h < 9; h++)
        {
            this.addSlotToContainer(new Slot(normalInv, h, 8 + h * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		/*if(earthInv != null && !earthInv.validInventory())
			return false;*/
		return true;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < slotCount)
            {
            	if (!this.mergeItemStack(itemstack1, slotCount, 36+slotCount, false))
            	{
                return null;
            	}
            }
            else if (par2 >= slotCount && par2 < 27+slotCount)
            {
                if (!this.mergeItemStack(itemstack1, 0, slotCount, false))
                {
                    return null;
                }
            }
            else if (par2 >= 27+slotCount && par2 < 36+slotCount)
            {
                if (!this.mergeItemStack(itemstack1, 0, 27+slotCount, true))
                {
                    return null;
                }
            }
            

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

}
