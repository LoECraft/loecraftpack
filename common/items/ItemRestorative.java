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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemRestorative extends Item
{
	/****************/
	/**Registration**/
	/****************/
	
	public final List<ItemRestorativeSubType> subTypes = new ArrayList<ItemRestorativeSubType>();
	
	public ItemRestorative(int itemID)
    {
        super(itemID);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
		this.setUnlocalizedName("restorative");
        this.setCreativeTab(LoECraftPack.LoECraftTab);
    }
	
	public ItemRestorativeSubType addSubType(String name)
	{
		ItemRestorativeSubType subtype = new ItemRestorativeSubType(name);
		subTypes.add(subtype);
		return subtype;
	}
	
	public void RegisterRestoratives()
	{
		for(ItemRestorativeSubType subType : subTypes)
		{
			LanguageRegistry.instance().addStringLocalization("item.restorative." + subType.iconName + ".name", subType.name);
		}
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list)
	{
		for (int j = 0; j < subTypes.size(); ++j)
    	{
    		list.add(new ItemStack(id, 1, j));
    	}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack iconNamestack)
	{
		return super.getUnlocalizedName() + "." + subTypes.get(MathHelper.clamp_int(iconNamestack.getItemDamage(), 0, subTypes.size()-1)).iconName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return subTypes.get(MathHelper.clamp_int(index, 0, subTypes.size()-1)).icon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		for (ItemRestorativeSubType subType : subTypes)
		{
			subType.icon = iconRegister.registerIcon("loecraftpack:restorative/" + subType.iconName);
		}
		
	}
	
	
	/************/
	/**Behavior**/
	/************/
	
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
        	--itemStack.stackSize;
        }

        if (!world.isRemote)
        {
            curePotionEffects(player, itemStack.getItemDamage());
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
        return EnumAction.block;
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
    public void curePotionEffects(EntityPlayer player, int metaData)
    {
    	if (player.worldObj.isRemote)
        {
            return;
        }
    	
    	Iterator<PotionEffect> effects = player.getActivePotionEffects().iterator();
    	
    	while (effects.hasNext())
    	{
    		PotionEffect effect = effects.next();
    		if (isCurable(effect.getPotionID(), metaData))
    		{
    			player.removePotionEffect(effect.getPotionID());
    		}
    	}
    }
    
    /**
     * can this item cures this effect
     */
    public boolean isCurable(int potionID, int metaData)
    {
    	for (int restoreID : subTypes.get(metaData).restoreIDs)
    	{
    		if (restoreID == potionID)
    			return true;
    	}
    	return false;
    }
}
