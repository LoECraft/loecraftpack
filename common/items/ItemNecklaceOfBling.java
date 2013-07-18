package loecraftpack.common.items;


import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNecklaceOfBling extends ItemNecklace {

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
		
		player.worldObj.spawnParticle("fireworksSpark", player.posX, player.posY + 0.3D, player.posZ, player.rand.nextGaussian() * 0.05D, 1 * 0.5D, player.rand.nextGaussian() * 0.05D);
		
	}
	
	//Flashy Entrance

}
