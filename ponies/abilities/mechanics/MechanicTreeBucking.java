package loecraftpack.ponies.abilities.mechanics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.BlockAppleBloomLeaves;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * This class handles code regarding the interactions with custom apple trees
 */
public class MechanicTreeBucking
{	
	/**
	 * handles gentle Tree bucking
	 */
	public static boolean buckTree(World world, int xCoord, int yCoord, int zCoord, int fortune)
	{
		System.out.println("BUCK "+world.isRemote);
		if ((world.getBlockMetadata(xCoord, yCoord, zCoord)&2) == 1)
			return false;//not a natural tree
		
		int woodID = world.getBlockId(xCoord, yCoord, zCoord);
		int leafIDs[];
		if (woodID == LoECraftPack.blockAppleBloomLog.blockID)
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
			if (world.blockExists(xCoord, --yCoord, zCoord) &&
			    world.getBlockId(xCoord, yCoord, zCoord) == woodID &&
			    (world.getBlockMetadata(xCoord, yCoord, zCoord)&2) == 0)
				continue;
			yCoord++;
			break;
		}
		
		//buck possible leaves
		for (int xi = xCoord-2; xi <= xCoord+2; xi++)
		{
			for (int yi = yCoord+3 ; yi <= yCoord+8; yi++)
			{
				for (int zi = zCoord-2; zi <= zCoord+2; zi++)
				{
					buckLeaf(world, xi, yi, zi, fortune);
				}
			}
		}
		
		
		return true;
	}
	
	/**
	 * handles gentle leaf bucking
	 */
	public static void buckLeaf(World world, int xCoord, int yCoord, int zCoord, int fortune)
	{
		if (!world.blockExists(xCoord, yCoord, zCoord))
			return;//out of bounds
		Block hold = Block.blocksList[world.getBlockId(xCoord, yCoord, zCoord)];
		if (hold == null || !(hold instanceof BlockAppleBloomLeaves) )
			return;// not a leaf
		BlockAppleBloomLeaves leaf = (BlockAppleBloomLeaves) hold;
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		if ((meta&4)==1)
			return;//placed by player
		
		if (world.getBlockId(xCoord, yCoord, zCoord) == LoECraftPack.blockZapAppleLeavesCharged.blockID)
		{
			//charged Zap-Apple leaves scenario
			leaf.dropBlockAsItemWithChance(world, xCoord, yCoord, zCoord, meta, 1,fortune);
			if (world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID, 0, 2))
				leaf.tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID);
			System.out.println("ZAP-APPLES!!!");
		}
		
		if ((meta&3) < leaf.bloomStage)
			return;//no apples
		
		System.out.println("APPLES!!!");
		leaf.dropBlockAsItemWithChance(world, xCoord, yCoord, zCoord, meta, 1, fortune);
		if (world.setBlock(xCoord, yCoord, zCoord, leaf.blockID, 0, 2))
			leaf.tellClientOfChange(world, xCoord, yCoord, zCoord, leaf.blockID);
	}

}
