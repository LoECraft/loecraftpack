package loecraftpack.items;

import java.util.List;

import loecraftpack.LoECraftPack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerFoliage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLeavesZapApple extends ItemBlock
{
	public ItemLeavesZapApple(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
	
    public int getMetadata(int par1)
    {
        return par1 | 4;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int meta)
    {
    	return LoECraftPack.blockZapAppleLeaves.getBlockTextureFromSideAndMetadata(0, meta);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        int meta = par1ItemStack.getItemDamage();
        return LoECraftPack.blockZapAppleLeaves.getcolor(meta);
    }
    
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int meta = par1ItemStack.getItemDamage() & 3;
        
        if(this.getBlockID() == LoECraftPack.blockZapAppleLeavesCharged.blockID)
        	return super.getUnlocalizedName();
        else if (meta == 0)return super.getUnlocalizedName()+".normal";
        else return super.getUnlocalizedName()+".blooming";

    }
}
