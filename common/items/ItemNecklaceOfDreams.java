package loecraftpack.common.items;

import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class ItemNecklaceOfDreams extends ItemNecklace {

	public ItemNecklaceOfDreams(int par1) {
		super(par1);
	}
	
	public void onSleep(PlayerSleepInBedEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		//Teleport to dreamWorld
	}

}
