package loecraftpack.common.items;

import java.util.List;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBits extends Item
{
	public static final String[] names = {"1 Bit", "5 Bits", "10 Bits", "25 Bits", "100 Bits"};
	public static final String[] iconNames = {"1bit", "5bits", "10bits", "25bits", "100bits"};
	public static final int num = names.length-1;
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	public ItemBits(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.setUnlocalizedName("itemBits");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return this.icons[MathHelper.clamp_int(index, 0, num)];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack iconNamestack)
	{
		return super.getUnlocalizedName() + "." + iconNames[MathHelper.clamp_int(iconNamestack.getItemDamage(), 0, num)];
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list)
	{
		for (int j = 0; j < num+1; ++j)
    	{
    		list.add(new ItemStack(id, 1, j));
    	}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
	    icons = new Icon[iconNames.length];
	        
		for (int i = 0; i < iconNames.length; ++i)
		{
	    	icons[i] = iconRegister.registerIcon("loecraftpack:" + iconNames[i]);
	    	iconIndex = icons[i];
		}
	}
}
