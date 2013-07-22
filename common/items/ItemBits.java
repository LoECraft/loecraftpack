package loecraftpack.common.items;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBits extends Item
{
	public static final String[] names = {"1 Bit", "5 Bits", "10 Bits", "25 Bits", "100 Bits"};
	public static final String[] iconNames = new String[names.length];
	public static final int num = names.length-1;
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	public ItemBits(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
        this.setCreativeTab(LoECraftPack.LoECraftTabItem);
        this.setUnlocalizedName("itemBits");
        
        for(int i = 0; i < iconNames.length; i++)
        	iconNames[i] = names[i].replace(" ", "").toLowerCase();
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
	public void registerIcons(IconRegister iconRegister)
	{
	    icons = new Icon[iconNames.length];
	        
		for (int i = 0; i < iconNames.length; i++)
		{
	    	icons[i] = iconRegister.registerIcon("loecraftpack:bits/" + iconNames[i]);
	    	itemIcon = icons[i];
		}
	}
	
	private static ItemStack[] GetItemstack(int amount)
	{
		ArrayList<ItemStack> bits = new ArrayList<ItemStack>();
		
		int count = (int)(amount / 100);
		if (count > 0)
		{
			bits.add(new ItemStack(LoECraftPack.bits, count, 4));
			amount -= count * 100;
		}
		
		count = (int)(amount / 25);
		if (count > 0)
		{
			bits.add(new ItemStack(LoECraftPack.bits, count, 3));
			amount -= count * 25;
		}
		
		count = (int)(amount / 10);
		if (count > 0)
		{
			bits.add(new ItemStack(LoECraftPack.bits, count, 2));
			amount -= count * 10;
		}
		
		count = (int)(amount / 5);
		if (count > 0)
		{
			bits.add(new ItemStack(LoECraftPack.bits, count, 1));
			amount -= count * 5;
		}
		
		if (amount > 0)
			bits.add(new ItemStack(LoECraftPack.bits, amount, 0));
		
		return bits.toArray(new ItemStack[0]);
	}
	
	public static void DropBits(int amount, Entity entity)
	{
		for(ItemStack item : ItemBits.GetItemstack(amount))
			entity.entityDropItem(item, 0);
	}
	
	public static void DropBits(int amount, World world, Vec3 pos)
	{
		for(ItemStack item : ItemBits.GetItemstack(amount))
			world.spawnEntityInWorld(new EntityItem(world, pos.xCoord, pos.yCoord, pos.zCoord, item));
	}
}
