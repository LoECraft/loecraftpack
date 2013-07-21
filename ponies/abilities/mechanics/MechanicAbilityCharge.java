package loecraftpack.ponies.abilities.mechanics;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

public class MechanicAbilityCharge 
{
	public final static int maxCharge = 1000;
	protected static Map<String, Integer> chargers = new HashMap<String, Integer>();
	public static int chargeClient;
	
	public static int getChargeState(EntityPlayer player)
	{
		if (!chargers.containsKey(player.username))
			chargers.put(player.username, 0);
		return chargers.get(player.username);
	}
	
	public static void charge(EntityPlayer player, int change)
	{
		int charge = getChargeState(player) + change;
		setCharge(player, charge);
	}
	
	public static void charge(EntityPlayer player, float part, float total)
	{
		int charge = (int) (maxCharge*(part/total));
		setCharge(player, charge);
	}
	
	public static void setCharge(EntityPlayer player, int charge)
	{
		if (charge > maxCharge)charge = maxCharge;
		else if (charge < 0)charge = 0;
		chargers.put(player.username, charge);
		AbilityModeHandler.abilityModeChange(player, Modes.CHARGE, charge);
	}
	
	/**
	 * used by AbilityModeHandler, mainly during login
	 */
	public static void sync(EntityPlayer player)
	{
		AbilityModeHandler.abilityModeChange(player, Modes.CHARGE, getChargeState(player));
	}
	
	/**
	 * used by AbilityModeHandler, during logout
	 */
	public static void logout(EntityPlayer player)
	{
		chargers.remove(player.username);
	}
	
	

}
