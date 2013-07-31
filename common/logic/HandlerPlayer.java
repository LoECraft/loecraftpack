package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.mechanics.ModeHandler;
import loecraftpack.ponies.abilities.mechanics.MechanicTreeBucking;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryClient;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryCommon;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryServer;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;

public class HandlerPlayer implements IPlayerTracker
{
	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		//load Stats
		LoECraftPack.statHandler.addPlayer(player);
		//load inventory
		HandlerExtendedInventoryServer.AddPlayer(player);
		//sync ability modes
		ModeHandler.sync(player);
		//send clients packets
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		//save Stats
		System.out.println(Race.values()[player.getEntityData().getByte("Race")]);
		LoECraftPack.statHandler.savePlayer(player);
		System.out.println(Race.values()[player.getEntityData().getByte("Race")]);
		//save inventory
		HandlerExtendedInventoryServer.SavePlayer(player);
		//handler player logout for ability modes
		ModeHandler.logout(player);
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
