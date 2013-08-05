package loecraftpack.ponies.stats;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.AbilityBase;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class StatHandlerClient extends StatHandlerServer
{
	@Override
	public void addPlayer(String player)
	{
		AbilityPlayerData data = AbilityPlayerData.RegisterPlayer(player);
		if (!LoECraftPack.isSinglePlayer())
			stats.put(player, new Stats(data));
		
		for(AbilityBase ability : data.activeAbilities)
			ability.SetPlayer(player, data);
		
		for(AbilityBase ability : data.passiveAbilities)
			ability.SetPlayer(player, data);
		
		data.bindPlayerStats(player);
	}
	
	//used by update packet
	public void updatePlayerData(String player, Race race, float energy)
	{
		if (stats.containsKey(player))
		{
			//single-player load  OR  stat update
			Stats playerStats = (Stats)stats.get(player);
			playerStats.race = race;
			playerStats.abilityData.energy = energy;
		}
		else
			//multi-player load
			stats.put(player, new Stats(AbilityPlayerData.RegisterPlayer(player), race, energy));
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
	
	public Race getRace(String player)
	{
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
		if (stats.containsKey(player))
			((Stats)stats.get(player)).race = race;
	}
}
