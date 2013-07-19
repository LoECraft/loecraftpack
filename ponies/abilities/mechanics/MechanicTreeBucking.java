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
import loecraftpack.ponies.abilities.Ability;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * This class handles code regarding the interactions with custom apple trees
 */
public class MechanicTreeBucking
{
	protected static List<String> buckers = new ArrayList<String>();
	protected static boolean clientBuck = false;
	
	public static boolean canBuck(EntityPlayer player)
	{
		if (!player.worldObj.isRemote)
		{
			String[] bucks = buckers.toArray(new String[buckers.size()]);
			for (int i=0; i< bucks.length; i++)
			{
				if (bucks[i].matches(player.username))
				{
					return true;
				}
			}
			return false;//server
		}
		else
			return false;//client
	}
	
	/**
	 * used by AbilityModeHandler, mainly during login
	 */
	public static void sync(EntityPlayer player)
	{
		AbilityModeHandler.abilityModeChange(player, Ability.TreeBuck, getBucker(player)==null?0:1);
	}
	
	/**
	 * used by AbilityModeHandler, during logout
	 */
	public static void logout(EntityPlayer player)
	{
		buckers.remove(getBucker(player));
	}
	
	/**
	 * sync's the clients mode variable
	 */
	public static void setBuckClient(boolean set)
	{
		clientBuck = set;
	}

	/**
	 * toggle between buck modes (off, on)
	 */
	public static void switchBuckServer(EntityPlayer player)
	{
		setBuckServer(player, getBucker(player));
	}
	
	/**
	 * set buck mode.  Currently not in use
	 */
	public static void setBuckServer(EntityPlayer player, boolean on)
	{
		String bucker = getBucker(player);
		if (on)
		{
			if (bucker==null)
				setBuckServer(player, null);
		}
		else
		{
			if (bucker!=null)
				setBuckServer(player, bucker);
		}
	}
	
	/**
	 * Server adds or remove buckers from list based on name value (NULL = add, username = remove)
	 */
	protected static void setBuckServer(EntityPlayer player, String name)
	{
		if (name == null)
		{
			buckers.add(player.username);
			AbilityModeHandler.abilityModeChange(player, Ability.TreeBuck, 1);
			//PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.modeAbility, 0, 1),(Player)player);
		}
		else
		{
			buckers.remove(name);
			AbilityModeHandler.abilityModeChange(player, Ability.TreeBuck, 0);
			//PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.modeAbility, 0, 0),(Player)player);
		}
	}
	
	/**
	 * Finds out if the player is on the active buck list. (server side)
	 */
	protected static String getBucker(EntityPlayer player)
	{
		String[] bucks = buckers.toArray(new String[buckers.size()]);
		for (int i=0; i< bucks.length; i++)
		{
			if (bucks[i].matches(player.username))
			{
				return bucks[i];
			}
		}
		return null;
	}
	
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
