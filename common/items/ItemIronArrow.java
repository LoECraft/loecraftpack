package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIronArrow extends Item
{
	
	public ItemIronArrow(int par1) {
		super(par1);
		
		this.setCreativeTab(LoECraftPack.LoECraftTabItem);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:tools/IronArrow");
	}
	
}
