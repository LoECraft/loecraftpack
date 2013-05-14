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

public class ItemGemStones extends Item {
	
	public String[] gemDataNames =    {"saphire", "fireruby" , "2", "3", "4", "5", "6", "7", "tom", "shardHeart"    , "shardlaughter"    , "shardgenerosity"    , "shardkindness"    , "shardmagic"    , "shardloyalty"    , "shardhonesty"};
	public String[] gemDisplayNames = {"Saphire", "Fire Ruby", "2", "3", "4", "5", "6", "7", "Tom", "piece of Heart", "piece of Laughter", "piece of Generosity", "piece of Kindness", "piece of Magic", "piece of Loyalty", "piece of Honesty"};
	protected Icon[] icons;
	
	public ItemGemStones(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	public ItemGemStones(int par1, String[] gemDataNames, String[] gemDisplayNames)
	{
		this(par1);
		this.gemDataNames = gemDataNames;
		this.gemDisplayNames = gemDisplayNames;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return this.icons[MathHelper.clamp_int(index, 0, icons.length)];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack iconNamestack)
	{
		return super.getUnlocalizedName() + "." + gemDataNames[MathHelper.clamp_int(iconNamestack.getItemDamage(), 0, gemDataNames.length)];
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list)
	{
		for (int j = 0; j < 16; ++j)
    	{
    		list.add(new ItemStack(id, 1, j));
    	}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
	    icons = new Icon[gemDataNames.length];
	        
		for (int i = 0; i < gemDataNames.length; ++i)
		{
	    	icons[i] = iconRegister.registerIcon("loecraftpack:gems/" + gemDataNames[i]);
		}
	}

}
