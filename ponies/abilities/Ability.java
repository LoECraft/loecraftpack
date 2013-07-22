package loecraftpack.ponies.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import loecraftpack.common.logic.HandlerKey;
import loecraftpack.enums.Race;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class Ability
{
	public static HashMap<String, AbilityPlayerData> map = new HashMap<String, AbilityPlayerData>();
	//CAUTION: Make sure this is updated when you add abilities.
	private static Class[] abilityClasses = new Class[] {AbilityFireball.class, AbilityTeleport.class, AbilityOreVision.class, AbilityBuckTree.class};
	public static Ability[] abilitiesClient = new Ability[abilityClasses.length];
	public static int energyRegenNatural = 10;//per 1/2 second
	public static int energyClientMAX = 100;
	public static int energyClient=0;
	public static float chargeClientMAX = 100.0f;
	public static float chargeClient=0;
	public String name;
	public String icon;
	
	protected int energyCost = 0;
	protected int Cooldown = 0;
	protected float cooldown = 0;
	protected int Casttime = 0;
	protected float casttime = 0;
	private boolean held;
	private boolean heldChanged;
	private long time;
	private long lastTime;
	protected Race race;
	
	public Ability(String name, Race race, int cost, int cooldown)
	{
		this.name = name;
		icon = name.toLowerCase().replace(" ", "");
		this.race = race;
        Cooldown = cooldown;
	}
	
	public Ability(String name, Race race, int cost, int cooldown, int casttime)
	{
		this.name = name;
		icon = name.toLowerCase().replace(" ", "");
		this.race = race;
        Cooldown = cooldown;
        Casttime = casttime;
        energyCost = cost;
	}
	
	public static void RegisterAbilities()
	{
		abilitiesClient = NewAbilityArray();
		for(Ability ability : abilitiesClient)
			LanguageRegistry.instance().addStringLocalization("item.itemAbility." + ability.icon + ".name", ability.name);
	}
	
	public static Ability[] NewAbilityArray()
	{
		ArrayList<Ability> abilityList = new ArrayList<Ability>();
		for(Class c : abilityClasses)
		{
			try {
				Ability ability = (Ability)c.getConstructor().newInstance();
				abilityList.add(ability);
			} catch (Exception e) {e.printStackTrace();}
		}
		
		return abilityList.toArray(new Ability[0]);
	}
	
	public static void RegisterPlayer(String player)
	{
		map.put(player, new AbilityPlayerData(player, NewAbilityArray()));
	}
	
	public static void UnregisterPlayer(String player)
	{
		map.remove(player);
	}
	
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		held = true;
		time = System.currentTimeMillis();
		
		if (cooldown <= 0 && (player.capabilities.isCreativeMode || getEnergyCost(player)<=energyClient))
		{
			if (casttime >= Casttime)
			{
				if(world.isRemote)
				{
					if (CastSpellClient(player, world))
					{
						cooldown = Cooldown;
						casttime = 0;
						chargeClient = 0;
						chargeClientMAX = 100;
						if (!player.capabilities.isCreativeMode)
							setEnergy(energyClient - getEnergyCost(player));
					}
				}
				else
				{
					if (CastSpellServer(player, world))
					{
						AbilityPlayerData data = Ability.map.get(player.username);
						cooldown = Cooldown;
						casttime = 0;
						data.charge = 0;
						data.chargeMAX = 0;
						if (!player.capabilities.isCreativeMode)
							data.setEnergy(data.energy - getEnergyCost(player));
					}
				}
			}
			else
			{
				casttime += 0.25f;
				if(world.isRemote)
				{
					chargeClient = casttime;
					chargeClientMAX = Casttime;
				}
				else
				{
					AbilityPlayerData data = Ability.map.get(player.username);
					data.charge = casttime;
					data.chargeMAX = Casttime;
				}
			}
		}
		return itemStack;
	}
	
	public void onUpdate(EntityPlayer player)
	{
		if (cooldown > 0)
			cooldown -= 0.05f;
		
		if (time != lastTime || System.currentTimeMillis() - lastTime > 400)
		{
			lastTime = time;
			if (held)
			{
				heldChanged = true;
			}
			else
			{
				if (heldChanged)
				{
					casttime = 0;
					if(player.worldObj.isRemote)
					{
						chargeClient = 0;
						chargeClientMAX = 100;
					}
					else
					{
						AbilityPlayerData data = Ability.map.get(player.username);
						data.charge = 0;
						data.chargeMAX = 0;
					}
				}
				
				heldChanged = false;
			}
			held = false;
		}
	}
	
	public float GetCooldown()
	{
		if (Cooldown == 0)
			return 0;
		
		return cooldown / Cooldown;
	}
	
	public static float GetCooldown(int metadata)
	{
		return abilitiesClient[metadata].GetCooldown();
	}
	
	public Race GetRace()
	{
		return race;
	}
	
	public static void setEnergy(int newEnergy)
	{
		if (newEnergy > energyClientMAX)
		{
			energyClient = energyClientMAX;
		}
		else if(newEnergy < 0)
		{
			energyClient = 0;
		}
		else
		{
			energyClient = newEnergy;
		}
	}
	
	public static float getEnergyRatio()
	{
		if (energyClient >= energyClientMAX)
		{
			return 1;
		}
		else if(energyClient <= 0)
		{
			return 0;
		}
		else
		{
			return (float)energyClient/(float)energyClientMAX;
		}
	}
	
	public static float getCastTimeRatio()
	{
		if (chargeClient >= chargeClientMAX)
		{
			return 1;
		}
		else if(chargeClient <= 0)
		{
			return 0;
		}
		else
		{
			return chargeClient/chargeClientMAX;
		}
	}
	
	public int getEnergyCost(EntityPlayer player)
	{
		return energyCost;
	}
	
	protected abstract boolean CastSpellClient(EntityPlayer player, World world); //For client-only things like particles and raycasting
	protected abstract boolean CastSpellServer(EntityPlayer player, World world); //Ability logic
}
