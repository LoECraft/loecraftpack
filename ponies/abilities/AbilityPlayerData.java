package loecraftpack.ponies.abilities;

public class AbilityPlayerData {
	
	public int energyRegenNatural = 10;//per 1/2 second
	public int energyMAX = 100;
	public int energy;
	
	
	public float chargeMAX = 100.0f;
	public float charge;
	
	public final Ability[] abilities;
	
	public AbilityPlayerData(String player, Ability[] abilities)
	{
		this.abilities = abilities;
		//read from NBT
	}
	
	public void setEnergy(int newEnergy)
	{
		if (newEnergy > energyMAX)
		{
			energy = energyMAX;
		}
		else if(newEnergy < 0)
		{
			energy = 0;
		}
		else
		{
			energy = newEnergy;
		}
	}
}
