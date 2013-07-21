package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.mechanics.ModeHandler;
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
		//load Stats
		LoECraftPack.statHandler.addPlayer(player);
		LoECraftPack.statHandler.sendStatsToPlayer(player);
		//load inventory
		HandlerExtendedInventoryServer.AddPlayer(player);
		//sync ability modes
		ModeHandler.sync(player);
		//Register abilities
		
		Ability.RegisterPlayer(player.username);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		//save Stats
		LoECraftPack.statHandler.savePlayer(player);
		//save inventory
		HandlerExtendedInventoryServer.SavePlayer(player);
		//handler player logout for ability modes
		ModeHandler.logout(player);
		//Unregister abilities
		
		Ability.UnregisterPlayer(player.username);
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
