package loecraftpack.ponies.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class CustomInventory implements IInventory {
	
	@SideOnly(Side.CLIENT)
	int loadCount = 0;
	@SideOnly(Side.CLIENT)
	boolean[] loaded;
	
	public boolean inventoryChanged = false;
	private boolean valid;
	
	public CustomInventory(boolean valid)
	{
		this.valid = valid;
	}

	public boolean validInventory()
	{
		return valid;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void onInventoryChanged()
	{
		this.inventoryChanged = true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
	
	protected abstract void readFromNBT(NBTTagCompound nbt);
	
	protected abstract void writeToNBT(NBTTagCompound nbt);
	
	@SideOnly(Side.CLIENT)
	public void loadSlot(int slot, ItemStack contents)
	{
		System.out.println("    LS    ");
		if (!valid)
		{
			if (loaded==null)
				loaded = new boolean[getSizeInventory()];
			if (!loaded[slot])
			{
				setInventorySlotContents(slot, contents);
				loadCount++;
				loaded[slot]=true;
			}
			if (loadCount == getSizeInventory())
			{
				valid = true;
				loaded = null;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public List<Integer> getUnloaded()
	{
		System.out.println("    GU    ");
		if (valid)
			return null;
		List<Integer> result = new ArrayList<Integer>();
		for(int i=0; i<getSizeInventory(); i++)
		{
			if (loaded==null)
				loaded = new boolean[getSizeInventory()];
			if(!loaded[i])
				result.add(i);
		}
		return result;
	}

}
