package loecraftpack.items;

import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.logic.handlers.ColoredBedHandler;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemZapApple extends ItemFood
{
	
	public ItemZapApple(int id, int heal, float saturation, boolean wolf)
    {
        super(id, heal, saturation, wolf);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack)
    {
        return itemStack.getItemDamage() > 0;
    }
    

    @SideOnly(Side.CLIENT)

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack itemStack)
    {
        return itemStack.getItemDamage() == 0 ? EnumRarity.rare : EnumRarity.epic;
    }

    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if (itemStack.getItemDamage() > 0)
        {
            if (!world.isRemote)
            {
            	entityPlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 3000, 0));
            	entityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 3000, 0));
            }
        }
        else
        {
            super.onFoodEaten(itemStack, world, entityPlayer);
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int id, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(id, 1, 0));
        list.add(new ItemStack(id, 1, 1));
    }
    
    
    @Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
	    iconIndex = iconRegister.registerIcon("loecraftpack:zapApple");
	}
    
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = par1ItemStack.getItemDamage();
        
        if (i == 0)return super.getUnlocalizedName()+".normal";
        else return super.getUnlocalizedName()+".charged";

    }

}
