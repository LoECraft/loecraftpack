package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class HandlerPlayer implements IPlayerTracker
{

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		System.out.println(player.username+" says HI");
		LoECraftPack.StatHandler.AddPlayer(player);
		LoECraftPack.StatHandler.setRace(player, Race.Alicorn);//for testing
		LoECraftPack.inventoryHandler.AddPlayer(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		LoECraftPack.StatHandler.SavePlayer(player);
		LoECraftPack.inventoryHandler.SavePlayer(player);
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
