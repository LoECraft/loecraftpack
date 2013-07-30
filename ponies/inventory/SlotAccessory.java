package loecraftpack.ponies.inventory;

import loecraftpack.LoECraftPack;
import loecraftpack.common.items.IRacialItem;
import loecraftpack.common.items.ItemAccessory;
import loecraftpack.common.items.ItemNecklace;
import loecraftpack.common.items.ItemRacial;
import loecraftpack.common.items.ItemRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotAccessory extends Slot
{
	int type;
	EntityPlayer player;
	
	public SlotAccessory(IInventory inventory, int index, int xPos, int yPos, int type, EntityPlayer player)
	{
		super(inventory, index, xPos, yPos);
		this.type = type;
		this.player = player;
	}
	
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	public boolean isItemValid(ItemStack itemStack)
	{
		if (itemStack == null || itemStack.getItem() == null)
			return false;
		if (itemStack.getItem() instanceof IRacialItem &&
			!((IRacialItem)itemStack.getItem()).canBeUsedBy(LoECraftPack.statHandler.getRace(player)))
			return false;
		
		switch (type)
		{
		case 0:
			return itemStack.getItem() instanceof ItemRacial;
		case 1:
			return itemStack.getItem() instanceof ItemNecklace;
		case 2:
			return itemStack.getItem() instanceof ItemRing;
		default:
			return true;
		}
	}
	
	@SideOnly(Side.CLIENT)
    public Icon getBackgroundIconIndex()
    {
		switch (type)
		{
		case 0:
			return ItemAccessory.slotIconRacial;
		case 1:
			return ItemAccessory.slotIconNecklace;
		case 2:
			return ItemAccessory.slotIconRing;
		default:
			return null;
		}
    }
}
