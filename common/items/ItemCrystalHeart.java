package loecraftpack.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.LoECraftPack;
import loecraftpack.common.logic.HandlerColoredBed;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemCrystalHeart extends Item {

	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	public ItemCrystalHeart(int id) {
		super(id);
		this.maxStackSize = 1;
		this.setMaxDamage(0);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
    	itemIcon = iconRegister.registerIcon("loecraftpack:tools/crystalheart");
	}
}
