package loecraftpack.ponies.stats;

import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.AbilityPlayerData;

public class Stats
{
	public AbilityPlayerData abilityData;
	public Race race = Race.NONE;
	//Do: Add the rest of the stats
	
	public Stats(AbilityPlayerData abilityData)
	{
		this.abilityData = abilityData;
	}
	
	public Stats(AbilityPlayerData abilityData, Race race, float energy)
	{
		this.abilityData = abilityData;
		this.race = race;
		this.abilityData.energy = energy;
	}
}
