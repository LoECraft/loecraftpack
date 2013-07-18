package loecraftpack.common.items;


import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.accessors.FieldAccessor;
import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class ItemNecklaceOfBling extends ItemNecklace {
	
	FieldAccessor<Random> rand = new FieldAccessor<Random>(Entity.class, "rand");

	public ItemNecklaceOfBling(int par1) {
		super(par1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:tools/necklaceBling");
	}
	
	public void applyWornEffect(EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		System.out.println("Bling Bling");
        
		//TODO REMOVE THIS LINE, and update code once ASM works!
		Random rand = this.rand.get(player);
		
		player.worldObj.spawnParticle("fireworksSpark", player.posX, player.posY + 0.3D, player.posZ, rand.nextGaussian() * 0.05D, 1 * 0.5D, rand.nextGaussian() * 0.05D);
		
	}
	
	//Flashy Entrance

}
