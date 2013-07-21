package loecraftpack.ponies.abilities;

public class AbilityPlayerData {
	
	protected int charge;
	protected int energy;
	protected int maxEnergy = 1000;
	protected Ability[] abilities;
	
	public AbilityPlayerData(String player, Ability[] abilities)
	{
		this.abilities = abilities;
		//read from NBT
	}
	
	public int getCharge()
	{
		return charge;
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
	public Ability[] getAbilities()
	{
		return abilities;
	}
	
	public void zeroCharge ()
	{
		charge = 0;
	}
	
	public void charge(float partial, float full)
	{
		if (partial > full)
		{
			charge = Ability.maxCharge;
		}
		else if(partial < 0)
		{
			charge = 0;
		}
		else
		{
			charge = (int)(partial/full);
		}
	}
	
	public void energy(int newEnergy)
	{
		if (newEnergy > maxEnergy)
		{
			energy = maxEnergy;
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
