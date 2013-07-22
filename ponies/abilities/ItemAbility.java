package loecraftpack.ponies.abilities;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemAbility extends Item
{
	private static int num = Ability.abilitiesClient.length - 1;
	private static Icon[] icons;
	
	public ItemAbility(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("itemAbility");
        this.setCreativeTab(LoECraftPack.LoECraftTabAbility);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return icons[MathHelper.clamp_int(index, 0, num)];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack iconNamestack)
	{
		return super.getUnlocalizedName() + "." + Ability.abilitiesClient[MathHelper.clamp_int(iconNamestack.getItemDamage(), 0, num)].icon;
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
	public void registerIcons(IconRegister iconRegister)
	{
	    icons = new Icon[Ability.abilitiesClient.length];
	        
		for (int i = 0; i < Ability.abilitiesClient.length; i++)
		{
			if (Ability.abilitiesClient[i] == null)
				continue;
			
	    	icons[i] = iconRegister.registerIcon("loecraftpack:abilities/" + Ability.abilitiesClient[i].icon);
	    	itemIcon = icons[i];
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (!world.isRemote && !Ability.map.containsKey(player.username))
			return itemStack;
		
		Ability ability;
		if (world.isRemote)
			ability = Ability.abilitiesClient[itemStack.getItemDamage()];
		else
			ability = Ability.map.get(player.username).abilities[itemStack.getItemDamage()];
		
		if (!LoECraftPack.statHandler.isRace(player, ability.race))
		{
			itemStack.stackSize = 0;
			return itemStack;
		}
		
		return ability.onItemRightClick(itemStack, world, player);
	}
}
