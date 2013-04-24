package loecraftpack.ponies.stats;

import java.util.HashMap;
import java.util.Map;

import loecraftpack.enums.Race;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ServerStatHandler
{
	static Map stats = new HashMap<String, Stats>();
	
	public static void AddPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		stats.put(player.username, new Stats(Race.values()[nbt.getByte("Race")]));
	}
	
	public static void SavePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		Stats playerStats = (Stats)stats.get(player.username);
		nbt.setByte("Race", (byte)playerStats.race.ordinal());
	}
	
	public static boolean isRace(EntityPlayer player, Race race)
	{
		if (!stats.containsKey(player.username))
			AddPlayer(player);
		
		Stats playerStats = (Stats)stats.get(player.username);
		
		return playerStats.race == race || playerStats.race == Race.Alicorn; //Alicorn is master race
	}
	
	public static Race getRace(EntityPlayer player)
	{
		if (!stats.containsKey(player.username))
			AddPlayer(player);
		
		return ((Stats)stats.get(player.username)).race;
	}
	
	public static void setRace(EntityPlayer player, Race race)
	{
		if (!stats.containsKey(player.username))
			AddPlayer(player);
		
		((Stats)stats.get(player.username)).race = race;
	}
}
