package loecraftpack.ponies.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import loecraftpack.enums.Race;

public class StatHandlerClient extends StatHandlerServer
{
	//used by update packet
	public void updatePlayerData(String player, Race race)
	{
		if (stats.containsKey(player))
			//single-player load  OR  stat update
			((Stats)stats.get(player)).race = race;
		else
			//multi-player load
			stats.put(player, new Stats(race));
	}
	
	@Override
	public boolean isRace(EntityPlayer player, Race race)
	{
		if (Minecraft.getMinecraft().isSingleplayer())
			return super.isRace(player, race);
		else
			return isRace(player.username, race);
	}
	
	public boolean isRace(String player, Race race)
	{
		if (stats.containsKey(player))
		{
			Stats playerStats = (Stats)stats.get(player);
			
			return playerStats.race == race || playerStats.race == Race.ALICORN; //Alicorn is master race
		}
		
		return false;
	}
	
	@Override
	public Race getRace(EntityPlayer player)
	{
		if (Minecraft.getMinecraft().isSingleplayer())
			return super.getRace(player);
		else
			return getRace(player.username);
	}
	
	public Race getRace(String player)
	{
		System.out.println("do I get used?");
		if (stats.containsKey(player))
			return ((Stats)stats.get(player)).race;
		else
			return Race.NONE;
	}
	
	@Override
	public void setRace(EntityPlayer player, Race race)
	{
		if (Minecraft.getMinecraft().isSingleplayer())
			super.setRace(player, race);
		else
			setRace(player.username, race);
	}
	
	public void setRace(String player, Race race)
	{
		System.out.println("do I get used?");
		if (stats.containsKey(player))
			((Stats)stats.get(player)).race = race;
	}
}
