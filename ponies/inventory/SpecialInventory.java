package loecraftpack.ponies.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SpecialInventory extends CustomInventory {
	
	private ItemStack[] inventory = new ItemStack[6];
	
	public SpecialInventory()
	{
		super(false);
	}

	public SpecialInventory(NBTTagCompound nbt)
	{
		super(true);
		readFromNBT(nbt);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inventory[par1] != null)
        {
            ItemStack itemstack;

            if (this.inventory[par1].stackSize <= par2)
            {
                itemstack = this.inventory[par1];
                this.inventory[par1] = null;
                
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[par1].splitStack(par2);

                if (this.inventory[par1].stackSize == 0)
                {
                    this.inventory[par1] = null;
                }
                
                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		if (this.inventory[i] != null)
        {
            ItemStack itemstack = this.inventory[i];
            this.inventory[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventory[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
        	itemstack.stackSize = this.getInventoryStackLimit();
        }
        
        this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Additional Equipment";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}
	
	//TODO change this
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	protected void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("SpecialInv");
        
        inventory = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }
	
	protected void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("SpecialInv", nbttaglist);
    }

}
