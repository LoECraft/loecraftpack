package loecraftpack.common.items;

import loecraftpack.accessors.FieldAccessor;
import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRingLife extends ItemRing {
	
	static FieldAccessor<Integer> health = new FieldAccessor<Integer>(EntityLiving.class, "health");
	static FieldAccessor<Boolean> isDead = new FieldAccessor<Boolean>(Entity.class, "isDead");

	public ItemRingLife(int par1) {
		super(par1);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:tools/ringLife");
	}
	
	public void onDeath(LivingDeathEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		if(health.get(player)<1)
		{
			health.set(player, player.getMaxHealth()/4);
			isDead.set(player, false);
			itemStack.stackSize--;
			if (itemStack.stackSize < 1)
				inv.setInventorySlotContents(slot, null);
			event.setCanceled(true);
		}
	}

}
