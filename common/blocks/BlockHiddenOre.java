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
	
	Icon[] hiddenIcons = new Icon[4];
	public int renderID = 0;

	public BlockHiddenOre(int id) {
		super(id, Material.rock);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getHiddenBlockTextureFromSideAndMetadata(int side, int meta)
    {
		return hiddenIcons[meta&3];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("stone");
        hiddenIcons[0] = par1IconRegister.registerIcon("loecraftpack:textures/blocks/ores/hiddenore1");
        hiddenIcons[1] = par1IconRegister.registerIcon("loecraftpack:textures/blocks/ores/hiddenore2");
        hiddenIcons[2] = par1IconRegister.registerIcon("loecraftpack:textures/blocks/ores/hiddenore3");
        hiddenIcons[3] = par1IconRegister.registerIcon("loecraftpack:textures/blocks/ores/hiddenore4");
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
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.cobblestone.blockID;
    }
	
	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, int xCoord, int yCoord, int zCoord, int meta)
	{
		System.out.println("harvest");
		ItemStack results;
		
		if(entityPlayer.getHeldItem().itemID == LoECraftPack.itemPickaxeGem.itemID)
		{
			results = new ItemStack(this.blockID, 1, meta);
		}
		else if (this.canSilkHarvest(world, entityPlayer, xCoord, yCoord, zCoord, meta) && EnchantmentHelper.getSilkTouchModifier(entityPlayer))
			results = new ItemStack(Block.stone, 1, 0);
		else
			results = new ItemStack(Block.cobblestone, 1, 0);
		
		this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, results);
	}

}
