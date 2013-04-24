package loecraftpack.logic.handlers;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerHandler implements IPlayerTracker
{

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		LoECraftPack.StatHandler.AddPlayer(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		LoECraftPack.StatHandler.SavePlayer(player);
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
