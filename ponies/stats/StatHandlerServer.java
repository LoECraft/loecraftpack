package loecraftpack.ponies.stats;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class StatHandlerServer
{
	static Map stats = new HashMap<String, Stats>();
	
	public static void addPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		stats.put(player.username, new Stats(Race.values()[nbt.getByte("Race")]));
	}
	
	public static void savePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		Stats playerStats = (Stats)stats.get(player.username);
		nbt.setByte("Race", (byte)playerStats.race.ordinal());
	}
	
	public static boolean sendStatsToPlayer(EntityPlayer player)
	{
		if (stats.containsKey(player.username))
		{
			Stats stat = (Stats)stats.get(player.username);
			PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.applyStats, stat.race.ordinal(), player.username), (Player)player);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//used by client side
	public void updatePlayerData(String player, Race race){}
	
	public boolean isRace(EntityPlayer player, Race race)
	{
		if (!stats.containsKey(player.username))
			addPlayer(player);
		
		Stats playerStats = (Stats)stats.get(player.username);
		
		return playerStats.race == race || playerStats.race == Race.ALICORN; //Alicorn is master race
	}
	
	public Race getRace(EntityPlayer player)
	{
		if (!stats.containsKey(player.username))
			addPlayer(player);
		
		return ((Stats)stats.get(player.username)).race;
	}
	
	public void setRace(EntityPlayer player, Race race)
	{
		if (!stats.containsKey(player.username))
			addPlayer(player);
		
		((Stats)stats.get(player.username)).race = race;
	}
}
