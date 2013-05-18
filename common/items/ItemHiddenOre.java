package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.BlockAppleBloomLeaves;
import loecraftpack.common.blocks.BlockHiddenOre;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.Mod.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHiddenOre extends ItemBlock
{
	protected BlockHiddenOre ore;
	
	public ItemHiddenOre(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.ore = LoECraftPack.blockGemOre;
    }
	
	//This constructor needs to be usable in-order to have more than 1 blockID
	public ItemHiddenOre(int par1, Block ore)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.ore = (BlockHiddenOre)ore;
        System.out.println("ore passed: "+ this.ore);
    }
	
	@Override
    public int getMetadata(int par1)
    {
        return par1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
    	return ore.getHiddenBlockTextureFromSideAndMetadata(2, meta);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName()+"."+par1ItemStack.getItemDamage();
    }
}
