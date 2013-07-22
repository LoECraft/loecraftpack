package loecraftpack.common.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.LoECraftPack;
import loecraftpack.ponies.abilities.Ability;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemRestorative extends Item
{
	public static List<ItemRestorative> fullList = new ArrayList<ItemRestorative>();
	public List<Integer> restoreIDs = new ArrayList<Integer>();
	public String name;
	public String iconName;
	
	public ItemRestorative(int itemID, int uses, String name)
    {
        super(itemID);
        this.setMaxStackSize(1);
        if (uses>1) 
        	this.setMaxDamage(uses);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.name = name;
        this.iconName = name.toLowerCase().replace(" ", "");
        fullList.add(this);
    }
	
	/**
	 * add a potion effect that this can restore
	 */
	public ItemRestorative addR(int effectID)
	{
		restoreIDs.add(effectID);
		return this;
	}
	
	public static void RegisterRestoratives()
	{
		for(ItemRestorative restorative : fullList)
		{
			LanguageRegistry.addName(restorative, restorative.name);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("loecraftpack:restorative/" + iconName);
	}

    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
        	if (getMaxDamage()==0)
        		--itemStack.stackSize;
        	else
        		itemStack.damageItem(1, player);
        }

        if (!world.isRemote)
        {
            curePotionEffects(player);
        }

        return itemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        return itemStack;
    }
    
    /**
     * example from entity living
     */
    public void curePotionEffects(EntityPlayer player)
    {
    	if (player.worldObj.isRemote)
        {
            return;
        }
    	
    	Iterator<PotionEffect> effects = player.getActivePotionEffects().iterator();
    	
    	while (effects.hasNext())
    	{
    		PotionEffect effect = effects.next();
    		if (isCurable(effect.getPotionID()))
    		{
    			player.removePotionEffect(effect.getPotionID());
    		}
    	}
    }
    
    /**
     * can this item cures this effect
     */
    public boolean isCurable(int potionID)
    {
    	for (int restoreID : restoreIDs)
    	{
    		if (restoreID == potionID)
    			return true;
    	}
    	return false;
    }
}
