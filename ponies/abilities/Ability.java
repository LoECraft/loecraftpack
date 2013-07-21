package loecraftpack.ponies.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.mechanics.MechanicAbilityCharge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class Ability
{
	public static HashMap<String, Ability[]> map = new HashMap<String, Ability[]>();
	//CAUTION: Make sure this is updated when you add abilities.
	private static Class[] abilityClasses = new Class[] {AbilityFireball.class, AbilityTeleport.class, AbilityBuckTree.class};
	public static Ability[] abilities = new Ability[abilityClasses.length];
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
		icon = name.toLowerCase().replace(' ', '\0');
		this.race = race;
        Cooldown = cooldown;
	}
	
	public Ability(String name, Race race, int cooldown, int casttime)
	{
		this.name = name;
		icon = name.toLowerCase().replace(' ', '\0');
		this.race = race;
        Cooldown = cooldown;
        Casttime = casttime;
	}
	
	public static void RegisterAbilities()
	{
		abilities = NewAbilityArray();
		for(Ability ability : abilities)
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
		map.put(player, NewAbilityArray());
	}
	
	public static void UnregisterPlayer(String player)
	{
		map.remove(player);
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		held = true;
		time = System.currentTimeMillis();
		
		if (cooldown <= 0)
		{
			if (casttime >= Casttime)
			{
				if ((par2World.isRemote && CastSpellClient(par3EntityPlayer, par2World)) ||
						(!par2World.isRemote && CastSpellServer(par3EntityPlayer, par2World)))
				{
					cooldown = Cooldown;
					casttime = 0;
					MechanicAbilityCharge.setCharge(par3EntityPlayer, 0);
				}
			}
			else
			{
				casttime += 0.25f;
				MechanicAbilityCharge.charge(par3EntityPlayer, casttime, Casttime);
			}
		}
		return par1ItemStack;
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
					MechanicAbilityCharge.setCharge((EntityPlayer)player, 0);
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
		return abilities[metadata].GetCooldown();
	}
	
	public static Ability getAbility(String name)
	{
		for (int i=0; i<abilities.length; i++)
		{
			if (abilities[i].name.contentEquals(name))
				return abilities[i];
		}
		return null;
	}
	
	public void wasCast(EntityPlayer entityPlayer)
	{
		cooldown = Cooldown;
		casttime = 0;
		MechanicAbilityCharge.setCharge(entityPlayer, 0);
	}
	
	protected abstract boolean CastSpellClient(EntityPlayer player, World world); //For client-only things like particles and raycasting
	protected abstract boolean CastSpellServer(EntityPlayer player, World world); //Ability logic
}
