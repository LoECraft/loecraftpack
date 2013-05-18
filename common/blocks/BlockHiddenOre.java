package loecraftpack.common.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.LoECraftPack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockHiddenOre extends Block {
	
	int types = 16;
	Icon[] hiddenIcons;
	public int renderID = 0;

	public BlockHiddenOre(int id) {
		super(id, Material.rock);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        hiddenIcons = new Icon[types];
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getHiddenBlockTextureFromSideAndMetadata(int side, int meta)
    {
		return hiddenIcons[meta];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("stone");
        for(int i=0; i<types; i++)
        {
        	hiddenIcons[i] = par1IconRegister.registerIcon("loecraftpack:ores/hiddenore"+i);
        }
    }
	
	@Override
	public int getRenderType()
    {
        return renderID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for(int i=0; i<types; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.cobblestone.blockID;
    }
	
	/**
	 * Called when the player destroys a block with an item that can harvest it.
	 */
	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, int xCoord, int yCoord, int zCoord, int meta)
	{
		System.out.println("harvest");
		ItemStack results;
		
		if(entityPlayer.getHeldItem().itemID == LoECraftPack.itemPickaxeGem.itemID)
		{
			int fortune = EnchantmentHelper.getFortuneModifier(entityPlayer);
			if (fortune > 0)
			{
				fortune = world.rand.nextInt(fortune + 2);
				if (fortune < 1)
					fortune = 1;
			}
			else
				fortune = 1;
			results = new ItemStack(LoECraftPack.itemGemStones.itemID, fortune, meta);
		}
		else if (this.canSilkHarvest(world, entityPlayer, xCoord, yCoord, zCoord, meta) && EnchantmentHelper.getSilkTouchModifier(entityPlayer))
			results = new ItemStack(Block.stone, 1, 0);
		else
			results = new ItemStack(Block.cobblestone, 1, 0);
		
		this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, results);
	}

}
