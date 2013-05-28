package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import loecraftpack.common.logic.HandlerColoredBed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class ItemIronArrow extends Item {
	
	public static Icon slotIcon;

	public ItemIronArrow(int par1) {
		super(par1);
		
		this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		iconIndex = iconRegister.registerIcon("loecraftpack:tools/IronArrow");
        
	    slotIcon = iconRegister.registerIcon("loecraftpack:tools/slotAmmo");
	}

}
