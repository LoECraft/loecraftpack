package loecraftpack.ponies.stats;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.Stats;

public class ClientStatHandler extends ServerStatHandler
{
	public static void AddPlayer(String player, Race race)
	{
		stats.put(player, new Stats(race));
	}
	
	public static boolean isRace(String player, Race race)
	{
		if (stats.containsKey(player))
		{
			Stats playerStats = (Stats)stats.get(player);
			
			return playerStats.race == race || playerStats.race == Race.Alicorn; //Alicorn is master race
		}
		
		return false;
	}
	
	public static Race getRace(String player)
	{
		if (stats.containsKey(player))
			return ((Stats)stats.get(player)).race;
		else
			return Race.None;
	}
	
	public static void setRace(String player, Race race)
	{
		if (stats.containsKey(player))
			((Stats)stats.get(player)).race = race;
	}
}
