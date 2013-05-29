package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Plain ring
 */
public class ItemRing extends ItemAccessory {
	
	public static Icon slotIcon;

	public ItemRing(int par1) {
		super(par1);
		
		this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		iconIndex = iconRegister.registerIcon("loecraftpack:tools/ring");
        
	    slotIcon = iconRegister.registerIcon("loecraftpack:tools/slotRing");
	}
}
