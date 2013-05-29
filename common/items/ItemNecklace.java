package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Plain necklace
 */
public class ItemNecklace extends ItemAccessory {
	
	public static Icon slotIcon;

	public ItemNecklace(int par1) {
		super(par1);
		
		this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		iconIndex = iconRegister.registerIcon("loecraftpack:tools/necklace");
        
	    slotIcon = iconRegister.registerIcon("loecraftpack:tools/slotNecklace");
	}

}
