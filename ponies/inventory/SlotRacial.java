package loecraftpack.ponies.inventory;

import loecraftpack.LoECraftPack;
import loecraftpack.common.items.IRacialItem;
import loecraftpack.common.items.ItemRacial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotRacial extends Slot {
	
	EntityPlayer player;

	public SlotRacial(IInventory inventory, int index, int xPos, int yPos, EntityPlayer player) {
		super(inventory, index, xPos, yPos);
		this.player = player;
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
	
	@SideOnly(Side.CLIENT)
    public Icon getBackgroundIconIndex()
    {
		return ItemRacial.slotIcon;
    }
}
