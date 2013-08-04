package loecraftpack.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemEnergyDrink extends ItemFoodWithSubTypes {
	
	protected Icon[] icons;

	public ItemEnergyDrink(int id) {
		super(id, 5, false);
		setAlwaysEdible();
		this.setHasSubtypes(true);
        this.setMaxDamage(0);
		this.setCreativeTab(LoECraftPack.LoECraftTabItem);
		
		int potionID = LoECraftPack.potionEnergy.id;
		potionId                =   new int[]{potionID, potionID, potionID, potionID};
	    potionDuration          =   new int[]{       1,        1,        1,        1};
	    potionAmplifier         =   new int[]{       1,        2,        3,        4};
	    potionEffectProbability = new float[]{   0.99f,        1,        1,     0.9f};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(id, 1, 0));
        list.add(new ItemStack(id, 1, 1));
        list.add(new ItemStack(id, 1, 2));
        list.add(new ItemStack(id, 1, 3));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return icons[index];
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
    	icons = new Icon[4];
	    icons[0] = iconRegister.registerIcon("loecraftpack:food/energydrink_diet");
	    icons[1] = iconRegister.registerIcon("loecraftpack:food/energydrink_apple");
	    icons[2] = iconRegister.registerIcon("loecraftpack:food/energydrink_fusion");
	    icons[3] = iconRegister.registerIcon("loecraftpack:food/energydrink_max");
	}
    
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        switch (par1ItemStack.getItemDamage())
        {
        case 0:
        	return super.getUnlocalizedName()+".diet";
        case 1:
        	return super.getUnlocalizedName()+".apple";
        case 2:
        	return super.getUnlocalizedName()+".fusion";
        case 3:
        	return super.getUnlocalizedName()+".max";
        }
        return null;
    }
    
    
    
}
