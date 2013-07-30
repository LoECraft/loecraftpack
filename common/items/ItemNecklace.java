package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Plain necklace
 */
public class ItemNecklace extends ItemAccessory {

	public ItemNecklace(int par1) {
		super(par1);
		this.setCreativeTab(LoECraftPack.LoECraftTabItem);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:tools/necklace");
	}

}
