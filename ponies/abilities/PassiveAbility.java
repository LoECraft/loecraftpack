package loecraftpack.ponies.abilities;

import java.util.ArrayList;

import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.passive.AbilityHighJump;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class PassiveAbility extends AbilityBase
{
	//CAUTION: Make sure this is updated when you add abilities.
	public static Class[] abilityClasses = new Class[] {AbilityHighJump.class};
	protected int period = 0; //tick delay before the ability performs logic
	private float count = 0; //count to keep track of when to perform logic
	
	public PassiveAbility(String name, Race race)
	{
		super(name, race);
	}
	
	public PassiveAbility(String name, Race race, int period)
	{
		super(name, race);
		this.period = period;
	}
	
	public static void RegisterAbilities()
	{
		for(PassiveAbility ability : NewAbilityArray())
		{
			LanguageRegistry.instance().addStringLocalization("item.itemAbility." + ability.icon + ".name", ability.name);
			ability.SetID();
		}
	}
	
	public static PassiveAbility[] NewAbilityArray()
	{
		ArrayList<PassiveAbility> abilityList = new ArrayList<PassiveAbility>();
		for(Class c : abilityClasses)
		{
			try {
				PassiveAbility ability = (PassiveAbility)c.getConstructor().newInstance();
				abilityList.add(ability);
			} catch (Exception e) {e.printStackTrace();}
		}
		
		return abilityList.toArray(new PassiveAbility[0]);
	}
	
	public void onTick(EntityPlayer player)
	{
		if (period > 0 && playerStats != null && (playerStats.race == Race.ALICORN || playerStats.race == race) && count++ >= period)
		{
			if (isClient())
				onUpdateClient(player);
			else
				onUpdateServer(player);
			count = 0;
		}
	}
	
	public void onUpdateClient(EntityPlayer player) {}
	public void onUpdateServer(EntityPlayer player) {}
}
