package loecraftpack.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import loecraftpack.LoECraftPack;
import loecraftpack.packethandling.PacketHelper;
import loecraftpack.packethandling.PacketIds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockZapAppleLeaves extends BlockAppleBloomLeaves
{
	int[] adjacentTreeBlocks;
	protected int bloomStage = 2;
	
	public BlockZapAppleLeaves(int id)
    {
        super(id);
        this.setTickRandomly(true);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.setUnlocalizedName("leavesZap");
        this.apple = LoECraftPack.itemZapApple;
        this.appleType=0;
    }
	
	public void attemptGrow(World world, int xCoord, int yCoord, int zCoord, Random random)
	{
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		if( (meta & 4) == 0 )
        {
        	//chance to grow
        	if( random.nextInt(30) == 0 )
        	{
        		if(((meta&3) + 1 ) == 4)
        			zapGrow(world, xCoord, yCoord, zCoord);
        		else
        		{
        			if(world.setBlock(xCoord, yCoord, zCoord, this.blockID, meta + 1, 2))
        				tellClientOfChange(world, xCoord, yCoord, zCoord, this.blockID);
        		}
        	}
        }
	}
	
	public void zapGrow(World world, int xCoord, int yCoord, int zCoord)
	{
		boolean flag = false;
		int reach = -1;
		int idTemp;
		while(true)
		{
			reach += 1;
			if(!world.blockExists(xCoord, yCoord+reach, zCoord))break;
			idTemp = world.getBlockId(xCoord, yCoord+reach, zCoord);
			if(idTemp == LoECraftPack.blockZapAppleLeaves.blockID || idTemp == LoECraftPack.blockZapAppleLeavesCharged.blockID)
				continue;
			else if(world.canLightningStrikeAt(xCoord, yCoord+reach, zCoord))
			{
				flag = true;
				break;
			}
			else
			{
				break;
			}
		}
		if(flag)
		{
			world.addWeatherEffect(new EntityLightningBolt(world, xCoord, yCoord+reach, zCoord));
			if(world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID, 0, 2))
				tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID);
			
			for(int xMod = -3; xMod <4; xMod++)
			{
				for(int yMod = -3; yMod <4; yMod++)
				{
					for(int zMod = -3; zMod <4; zMod++)
					{
						if(world.blockExists(xCoord+xMod, yCoord+yMod, zCoord+zMod) 
								&& world.getBlockId(xCoord+xMod, yCoord+yMod, zCoord+zMod) == LoECraftPack.blockZapAppleLeaves.blockID)
						{
							int meta = world.getBlockMetadata(xCoord+xMod, yCoord+yMod, zCoord+zMod);
							if( (meta&3) >= bloomStage && (meta & 4) == 0)
							{
								if(world.setBlock(xCoord+xMod, yCoord+yMod, zCoord+zMod, LoECraftPack.blockZapAppleLeavesCharged.blockID, 0, 2))
									tellClientOfChange(world, xCoord+xMod, yCoord+yMod, zCoord+zMod, LoECraftPack.blockZapAppleLeavesCharged.blockID);
							}
						}
					}
				}
			}
		}
	}
	
	//TODO Move this to a bucking class
	public static void buckLeaf(World world, int xCoord, int yCoord, int zCoord)
	{
		Block hold = Block.blocksList[world.getBlockId(xCoord, yCoord, zCoord)];
		if(! (hold instanceof BlockZapAppleLeaves) ) return;// not a leaf
		BlockZapAppleLeaves leaf = (BlockZapAppleLeaves) hold;
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		
		if((meta&4)==1)return;//placed by player
		if(world.getBlockId(xCoord, yCoord, zCoord) == LoECraftPack.blockZapAppleLeaves.blockID && (meta&3) < leaf.bloomStage) return;//no apples
		
		leaf.dropAppleThruTree(world, xCoord, yCoord, zCoord, new ItemStack(leaf.apple, 1, leaf.appleType));
		if(world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID, 0, 2))
			leaf.tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID);
	}
	
	public void dropAppleThruTree(World world, int xCoord, int yCoord, int zCoord, ItemStack itemStack)
	{
		int id;
		while(true)
		{
			id = world.getBlockId(xCoord, yCoord, zCoord);
			if(  id == LoECraftPack.blockZapApplelog.blockID ||
			     id == LoECraftPack.blockZapAppleLeaves.blockID  ||
			     id == LoECraftPack.blockZapAppleLeavesCharged.blockID)
				yCoord--;
			else
			{
				yCoord++;
				break;
			}
			if(!world.blockExists(xCoord, yCoord, zCoord))
			{
				yCoord++;
				break;
			}
		}
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = -0.2D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)xCoord + d0, (double)yCoord + d1, (double)zCoord + d2, itemStack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
	}
	
	//TODO adjust drop rate appropriately
	@Override
	public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return LoECraftPack.blockZapAppleSapling.blockID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon[0] = iconRegister.registerIcon("loecraftpack:leaves_zapapple");
		icon[1] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_opaque");
		icon[2] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_bloom");
		icon[3] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_bloom_opaque");
	}
}
