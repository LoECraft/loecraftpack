package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.mechanics.AbilityModeHandler;
import loecraftpack.ponies.abilities.mechanics.MechanicTreeBucking;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryClient;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryCommon;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryServer;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class HandlerPlayer implements IPlayerTracker
{

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		System.out.println(player.username+" says HI"+player.worldObj.isRemote);
		//load Stats
		LoECraftPack.statHandler.addPlayer(player);
		LoECraftPack.statHandler.sendStatsToPlayer(player);
		//load inventory
		HandlerExtendedInventoryServer.AddPlayer(player);
		//sync ability modes
		AbilityModeHandler.sync(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		//save Stats
		LoECraftPack.statHandler.savePlayer(player);
		//save inventory
		HandlerExtendedInventoryServer.SavePlayer(player);
		//handler player logout for ability modes
		AbilityModeHandler.logout(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
	}

}
