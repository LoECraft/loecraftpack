package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Plain ring
 */
public class ItemRing extends ItemAccessory {
	
	public ItemRing(int par1) {
		super(par1);
		this.setCreativeTab(LoECraftPack.LoECraftTabItem);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:tools/ring");
	}
}
