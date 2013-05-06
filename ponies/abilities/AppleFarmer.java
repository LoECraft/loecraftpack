package loecraftpack.ponies.abilities;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.BlockAppleBloomLeaves;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/*************************************************/
/*****This class handles code regarding the*******/
/*****interactions with custom apple trees********/
/*************************************************/

public class AppleFarmer
{
	/*************************************/
	/*****handles gentle Tree bucking*****/
	/*************************************/
	public static boolean buckTree(World world, int xCoord, int yCoord, int zCoord)
	{
		System.out.println("BUCK");
		if( (world.getBlockMetadata(xCoord, yCoord, zCoord)&2) == 1)return false;//not a natural tree
		
		int woodID = world.getBlockId(xCoord, yCoord, zCoord);
		int leafIDs[];
		if		(woodID == LoECraftPack.blockAppleBloomLog.blockID)
		{
			leafIDs = new int[1];
			leafIDs[0] = LoECraftPack.blockAppleBloomLeaves.blockID;
		}
		else if	(woodID == LoECraftPack.blockZapAppleLog.blockID)
		{
			leafIDs = new int[2];
			leafIDs[0] = LoECraftPack.blockZapAppleLeaves.blockID;
			leafIDs[0] = LoECraftPack.blockZapAppleLeavesCharged.blockID;
		}
		else
			return false;//not a buck-able tree
		
		//find base of tree
		while(true)
		{
			if(world.blockExists(xCoord, --yCoord, zCoord) &&
			   world.getBlockId(xCoord, yCoord, zCoord) == woodID &&
			   (world.getBlockMetadata(xCoord, yCoord, zCoord)&2) == 0)
				continue;
			yCoord++;
			break;
		}
		
		//buck possible leaves
		for(int xi = xCoord-2; xi <= xCoord+2; xi++)
		{
			for(int yi = yCoord+3 ; yi <= yCoord+8; yi++)
			{
				for(int zi = zCoord-2; zi <= zCoord+2; zi++)
				{
					buckLeaf(world, xi, yi, zi);
				}
			}
		}
		
		
		return true;
	}
	
	/*************************************/
	/*****handles gentle leaf bucking*****/
	/*************************************/
	public static void buckLeaf(World world, int xCoord, int yCoord, int zCoord)
	{
		if(!world.blockExists(xCoord, yCoord, zCoord))return;//out of bounds
		Block hold = Block.blocksList[world.getBlockId(xCoord, yCoord, zCoord)];
		if(hold == null || !(hold instanceof BlockAppleBloomLeaves) ) return;// not a leaf
		BlockAppleBloomLeaves leaf = (BlockAppleBloomLeaves) hold;
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		if((meta&4)==1)return;//placed by player
		
		if(world.getBlockId(xCoord, yCoord, zCoord) == LoECraftPack.blockZapAppleLeavesCharged.blockID)
		{
			//charged Zap-Apple leaves scenario
			leaf.dropAppleThruTree(world, xCoord, yCoord, zCoord, new ItemStack(leaf.apple, 1, leaf.appleType));
			if(world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID, 0, 2))
				leaf.tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID);
			System.out.println("ZAP-APPLES!!!");
		}
		
		if((meta&3) < leaf.bloomStage)return;//no apples
		
		System.out.println("APPLES!!!");
		leaf.dropAppleThruTree(world, xCoord, yCoord, zCoord, new ItemStack(leaf.apple, 1, leaf.appleType));
		if(world.setBlock(xCoord, yCoord, zCoord, leaf.blockID, 0, 2))
			leaf.tellClientOfChange(world, xCoord, yCoord, zCoord, leaf.blockID);
	}

}
