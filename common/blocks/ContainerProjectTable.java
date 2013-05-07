package loecraftpack.common.blocks;

import loecraftpack.LoECraftPack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerProjectTable extends Container
{
	public TileProjectTable inventory;
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
    private int posX;
    private int posY;
    private int posZ;
    private boolean ignoreMatrixChange;
    
	public ContainerProjectTable(InventoryPlayer par1InventoryPlayer, TileProjectTable inv, World par2World, int par3, int par4, int par5)
	{
		inventory = inv;
		inv.eventHandler = this;
		inv.openChest();
		this.worldObj = par2World;
        this.posX = par3;
        this.posY = par4;
        this.posZ = par5;
        
        this.addSlotToContainer(new ResultSlotProjectTable(par1InventoryPlayer.player, this.inventory, this.craftResult, 0, 124, 35));
        int l;
        int i1;

        for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                this.addSlotToContainer(new Slot(inventory, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
            }
        }

        for (l = 0; l < 5; ++l)
        {
            for (i1 = 0; i1 < 9; ++i1)
            {
            	if (l <= 1)
            		this.addSlotToContainer(new Slot(inventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            	else
            		this.addSlotToContainer(new Slot(par1InventoryPlayer, i1 + (l-2) * 9 + 9, 8 + i1 * 18, 133 + (l-2) * 18));
            }
        }

        for (l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, l, 8 + l * 18, 191));
        }

        this.onCraftMatrixChanged(inventory);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
    {
		InventoryCrafting invCraft = new InventoryCrafting(new ContainerDummy(), 3, 3);
		for(int i = 0; i < 9; i++)
			invCraft.setInventorySlotContents(i, inventory.getStackInSlot(i));
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(invCraft, this.worldObj));
    }

	public boolean canInteractWith(EntityPlayer player)
    {
        return this.worldObj.getBlockId(this.posX, this.posY, this.posZ) != LoECraftPack.table.blockID ? false : player.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
    }
	
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < 10)
            {
                if (!this.mergeItemStack(itemstack1, 10, 64, false))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 >= 10 && par2 < 28)
            {
            	if (!this.mergeItemStack(itemstack1, 28, 64, false))
            	{
                return null;
            	}
            }
            else if (par2 >= 28 && par2 < 55)
            {
                if (!this.mergeItemStack(itemstack1, 10, 28, false))
                {
                    return null;
                }
            }
            else if (par2 >= 55 && par2 < 64)
            {
                if (!this.mergeItemStack(itemstack1, 10, 55, true))
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
	
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
		super.onCraftGuiClosed(par1EntityPlayer);
		inventory.closeChest();
    }
}
