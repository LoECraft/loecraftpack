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
	public static float energyClient = 0;
	public static float chargeClientMAX = 100.0f;
	public static float chargeClient = 0;
	public String name;
	public String icon;
	
	protected boolean toggled = false;
	private boolean isToggleable = false;
	private float cycle = 0; //Used while ability is toggled to make cooldown animation continually cycle
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
	
	public Ability(String name, Race race, int cost)
	{
		this.name = name;
		icon = name.toLowerCase().replace(" ", "");
		this.race = race;
        Cooldown = 1;
        energyCost = cost;
        isToggleable = true;
	}
	
	public Ability(String name, Race race, int cost, int cooldown)
	{
		this.name = name;
		icon = name.toLowerCase().replace(" ", "");
		this.race = race;
        Cooldown = cooldown;
        energyCost = cost;
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
		
		if (cooldown <= 0 && (player.capabilities.isCreativeMode || getEnergyCost(player)<=energyClient || toggled))
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
						if (!player.capabilities.isCreativeMode && !toggled)
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
						if (!player.capabilities.isCreativeMode && !toggled)
							data.setEnergy(data.energy - getEnergyCost(player));
					}
				}
				
				if (isToggleable)
					toggled = !toggled;
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
		
		if (toggled)
		{
			if (player.worldObj.isRemote)
			{
				if (!player.capabilities.isCreativeMode)
					setEnergy(energyClient - getEnergyCostToggled(player));
				
				if (energyClient > getEnergyCostToggled(player))
					toggled = CastSpellToggledClient(player);
				else
					toggled = false;
				
				if (!toggled)
				{
					cycle = 0;
					CastSpellUntoggledClient(player);
				}
				else
				{
					if (cycle > 1)
						cycle = 0;
					else
						cycle += 0.05f;
				}
			}
			else
			{
				AbilityPlayerData data = Ability.map.get(player.username);
				if (!player.capabilities.isCreativeMode)
					data.setEnergy(data.energy - getEnergyCostToggled(player));
				
				if (energyClient > getEnergyCostToggled(player))
					toggled = CastSpellToggledServer(player);
				else
					toggled = false;
				
				if (!toggled)
				{
					cycle = 0;
					CastSpellUntoggledServer(player);
				}
			}
		}
		
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
	
	public boolean isToggled()
	{
		return toggled;
	}
	
	public static boolean isToggled(int metadata)
	{
		return abilitiesClient[metadata].isToggled();
	}
	
	public float getCooldown()
	{
		if (!toggled)
		{
			if (Cooldown == 0)
				return 0;
			
			return cooldown / Cooldown;
		}
		else
			return cycle;
	}
	
	public static float getCooldown(int metadata)
	{
		return abilitiesClient[metadata].getCooldown();
	}
	
	public Race GetRace()
	{
		return race;
	}
	
	public static void setEnergy(float newEnergy)
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
	
	public float getEnergyCost(EntityPlayer player)
	{
		return energyCost;
	}
	
	public float getEnergyCostToggled(EntityPlayer player)
	{
		return 0;
	}
	
	protected abstract boolean CastSpellClient(EntityPlayer player, World world); //For client-only things like particles and raycasting
	protected abstract boolean CastSpellServer(EntityPlayer player, World world); //Ability logic
	protected boolean CastSpellToggledClient(EntityPlayer player) {return true;}
	protected boolean CastSpellToggledServer(EntityPlayer player) {return true;}
	protected void CastSpellUntoggledClient(EntityPlayer player) {}
	protected void CastSpellUntoggledServer(EntityPlayer player) {}
}
