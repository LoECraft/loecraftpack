package loecraftpack.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class ItemNecklaceOfBling extends ItemNecklace {

	public ItemNecklaceOfBling(int par1) {
		super(par1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		iconIndex = iconRegister.registerIcon("loecraftpack:tools/necklaceBling");
	}
	
	public void applyWornEffect(EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		System.out.println("Bling Bling");
		
	}
	
	public void onSpawn(LivingSpawnEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		//Flashy Entrance
	}

}
