package loecraftpack.ponies.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSpecialEquipment extends Container {
	
	final static int slotCount = 8;

	IInventory normalInv;
	InventorySpecial specialInv;
	
	public ContainerSpecialEquipment(EntityPlayer entityPlayer)
	{
		normalInv = entityPlayer.inventory;
		specialInv = (InventorySpecial)HandlerExtendedInventoryCommon.getInventory(entityPlayer, InventoryId.Equipment);
		
		int v;
        int h;
        
        //Race item
        this.addSlotToContainer(new SlotRacial(specialInv, 0, 8, 8, entityPlayer));
        //Necklace
        this.addSlotToContainer(new SlotAccessory(specialInv, 1, 8, 26, 0));
        //Rings
        for (v = 0; v < 2; v++)
        {
        	this.addSlotToContainer(new SlotAccessory(specialInv, v+2, 8, 8+(v+2)*18, 1));
        }
        //Ammo
        this.addSlotToContainer(new SlotAmmo(specialInv, 4, 80, 8));
        
        //other
        for (v = 0; v < 3; v++)
        {
        	this.addSlotToContainer(new Slot(specialInv, v+5, 80, 8+(v+1)*18));
        }
        
        //main inv.
		for (v = 0; v < 3; v++)
        {
            for (h = 0; h < 9; h++)
            {
            	this.addSlotToContainer(new Slot(normalInv, h+v*9+9, 8+h*18, 84+v*18));
            }
        }
		
		//main toolbar
		for (h = 0; h < 9; h++)
        {
            this.addSlotToContainer(new Slot(normalInv, h, 8+h*18, 142));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		/*if (specialInv!=null && !specialInv.validInventory())
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
