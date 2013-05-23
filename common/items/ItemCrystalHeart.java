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
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return icons[index];
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
	    icons = new Icon[3];
	        
		for (int i = 0; i < 3; ++i)
		{
	    	icons[i] = iconRegister.registerIcon("loecraftpack:tools/crystalheart"+i);
		}
	}
    
    @Override
	public void getSubItems(int id, CreativeTabs tab, List list)
	{
		for (int j = 0; j < 3; ++j)
    	{
    		list.add(new ItemStack(id, 1, j));
    	}
	}
}
