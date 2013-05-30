package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
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
		LoECraftPack.StatHandler.AddPlayer(player);
		LoECraftPack.StatHandler.setRace(player, Race.Alicorn);//for testing
		HandlerExtendedInventoryServer.AddPlayer(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		LoECraftPack.StatHandler.SavePlayer(player);
		HandlerExtendedInventoryServer.SavePlayer(player);
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
