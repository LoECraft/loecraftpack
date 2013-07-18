package loecraftpack.common.items;

import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRingLife extends ItemRing {

	public ItemRingLife(int par1) {
		super(par1);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:tools/ringLife");
	}
	
	public void onDeathPre(LivingDeathEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		if(player.getHealth()<1)
		{
			player.setEntityHealth(player.getMaxHealth()/4);
			player.isDead = false;
			itemStack.stackSize--;
			if (itemStack.stackSize < 1)
				inv.setInventorySlotContents(slot, null);
			event.setCanceled(true);
		}
	}

}
