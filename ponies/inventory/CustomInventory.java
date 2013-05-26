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
	
	public boolean inventoryChanged = false;
	
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

}
