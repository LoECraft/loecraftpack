package loecraftpack.common.blocks;

import java.util.Random;

import loecraftpack.LoECraftPack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockGemOre extends Block {

	public BlockGemOre(int id) {
		super(id, Material.rock);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("stone");
    }
	
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.cobblestone.blockID;
    }
	
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
