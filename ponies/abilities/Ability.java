package loecraftpack.ponies.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import loecraftpack.enums.Race;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class Ability
{
	public static HashMap<String, AbilityPlayerData> map = new HashMap<String, AbilityPlayerData>();
	//CAUTION: Make sure this is updated when you add abilities.
	private static Class[] abilityClasses = new Class[] {AbilityFireball.class, AbilityTeleport.class, AbilityBuckTree.class};
	public static Ability[] abilitiesClient = new Ability[abilityClasses.length];
	public static final int maxCharge = 1000;
	public static int maxEnergy = 1000;
	public static int energyClient;
	public static int chargeClient;
	public String name;
	public String icon;
	
	protected int Cooldown = 0;
	protected float cooldown = 0;
	protected int Casttime = 0;
	protected float casttime = 0;
	private boolean held;
	private boolean heldChanged;
	private long time;
	private long lastTime;
	protected Race race;
	
	public Ability(String name, Race race, int cooldown)
	{
		this.name = name;
		icon = name.toLowerCase().replace(" ", "");
		this.race = race;
        Cooldown = cooldown;
	}
	
	public Ability(String name, Race race, int cooldown, int casttime)
	{
		this.name = name;
		icon = name.toLowerCase().replace(" ", "");
		this.race = race;
        Cooldown = cooldown;
        Casttime = casttime;
	}
	
	public static void RegisterAbilities()
	{
		abilitiesClient = NewAbilityArray();
		for(Ability ability : abilitiesClient)
		{
			LanguageRegistry.instance().addStringLocalization("item.itemAbility." + ability.icon + ".name", ability.name);
		}
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
		
		if (cooldown <= 0)
		{
			if (casttime >= Casttime)
			{
				if ((world.isRemote && CastSpellClient(player, world)) ||
						(!world.isRemote && CastSpellServer(player, world)))
				{
					cooldown = Cooldown;
					casttime = 0;
					if(world.isRemote)
						zeroCharge(0);
					else
						Ability.map.get(player.username).zeroCharge();
				}
			}
			else
			{
				casttime += 0.25f;
				if(world.isRemote)
					charge(casttime, Casttime);
				else
					Ability.map.get(player.username).charge(casttime, Casttime);
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
						zeroCharge(0);
					else
						Ability.map.get(player.username).zeroCharge();
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
	
	public void zeroCharge (int newCharge)
	{
		chargeClient = 0;
	}
	
	public void charge(float partial, float full)
	{
		if (partial > full)
		{
			chargeClient = Ability.maxCharge;
		}
		else if(partial < 0)
		{
			chargeClient = 0;
		}
		else
		{
			chargeClient = (int)(partial/full*maxCharge);
		}
	}
	
	public void energy(int newEnergy)
	{
		if (newEnergy > maxEnergy)
		{
			energyClient = maxEnergy;
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
	
	protected abstract boolean CastSpellClient(EntityPlayer player, World world); //For client-only things like particles and raycasting
	protected abstract boolean CastSpellServer(EntityPlayer player, World world); //Ability logic
}
