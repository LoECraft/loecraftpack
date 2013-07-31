package loecraftpack.ponies.abilities;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.active.AbilityBuckTree;
import loecraftpack.ponies.abilities.active.AbilityFireball;
import loecraftpack.ponies.abilities.active.AbilityOreVision;
import loecraftpack.ponies.abilities.active.AbilityTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ActiveAbility extends AbilityBase
{
	// CAUTION: Make sure this is updated when you add abilities.
	public static Class[] abilityClasses = new Class[] {AbilityFireball.class, AbilityTeleport.class, AbilityOreVision.class, AbilityBuckTree.class};

	protected boolean toggled = false;
	private boolean isToggleable = false;
	private float cycle = 0; // Used while ability is toggled to make cooldown
								// animation continually cycle
	protected int energyCost = 0;
	protected int Cooldown = 0;
	protected float cooldown = 0;
	protected int Casttime = 0;
	protected float casttime = 0;
	private boolean held;
	private boolean heldChanged;
	private long time;
	private long lastTime;

	public ActiveAbility(String name, Race race, int cost)
	{
		super(name, race);
		Cooldown = 1;
		energyCost = cost;
		isToggleable = true;
	}

	public ActiveAbility(String name, Race race, int cost, int cooldown)
	{
		super(name, race);
		Cooldown = cooldown;
		energyCost = cost;
	}

	public ActiveAbility(String name, Race race, int cost, int cooldown, int casttime)
	{
		super(name, race);
		Cooldown = cooldown;
		Casttime = casttime;
		energyCost = cost;
	}

	public static void RegisterAbilities()
	{
		for (ActiveAbility ability : NewAbilityArray())
			LanguageRegistry.instance().addStringLocalization("item.itemAbility." + ability.icon + ".name", ability.name);
	}

	public static ActiveAbility[] NewAbilityArray()
	{
		ArrayList<ActiveAbility> abilityList = new ArrayList<ActiveAbility>();
		for (Class c : abilityClasses)
		{
			try
			{
				ActiveAbility ability = (ActiveAbility) c.getConstructor().newInstance();
				abilityList.add(ability);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return abilityList.toArray(new ActiveAbility[0]);
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		held = true;
		time = System.currentTimeMillis();
		
		if (cooldown <= 0 && (toggled || player.capabilities.isCreativeMode || getEnergyCost(player) <= playerData.energy - (isClient()? playerData.energyAttemptOffset: 0)))
		{
			if (casttime >= Casttime)
			{
				if ((isClient() && CastSpellClient(player, world)) || (!isClient() && CastSpellServer(player, world)))
				{
					cooldown = Cooldown;
					casttime = 0;
					playerData.charge = 0;
					playerData.chargeMax = 100;
				}

				if (isToggleable)
				{
					toggled = !toggled;
					if (!toggled)
					{
						cycle = 0;
						
						if (isClient())
							CastSpellUntoggledClient(player);
						else
							CastSpellUntoggledServer(player);
					}
				}
			}
			else
			{
				casttime += 0.25f;
				
				playerData.charge = casttime;
				playerData.chargeMax = Casttime;
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
			if (!player.capabilities.isCreativeMode)
				playerData.addEnergy(-getEnergyCostToggled(player));

			if (playerData.energy  - (isClient()? playerData.energyAttemptOffset: 0)> getEnergyCostToggled(player))
				toggled = isClient() ? CastSpellToggledClient(player) : CastSpellToggledServer(player);
			else
				toggled = false;

			if (!toggled)
			{
				cycle = 0;
				
				if (isClient())
					CastSpellUntoggledClient(player);
				else
					CastSpellUntoggledServer(player);
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
					
					playerData.charge = 0;
					playerData.chargeMax = 100;
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
	
	@SideOnly(Side.CLIENT)
	public static boolean isToggled(int metadata)
	{
		return AbilityPlayerData.clientData.activeAbilities[metadata].isToggled();
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
		return AbilityPlayerData.clientData.activeAbilities[metadata].getCooldown();
	}

	public Race GetRace()
	{
		return race;
	}

	public float getEnergyCost(EntityPlayer player)
	{
		return energyCost;
	}

	public float getEnergyCostToggled(EntityPlayer player)
	{
		return 0;
	}

	protected abstract boolean CastSpellClient(EntityPlayer player, World world); // For client-only things like particles and raycasting

	protected boolean CastSpellServer(EntityPlayer player, World world){return true;}; // Ability logic  (not sure if this will get used anymore)
	
	public void CastSpellServer(Player player, AbilityPlayerData abilityData, DataInputStream data) throws IOException{};// packet triggered version of the above method

	protected boolean CastSpellToggledClient(EntityPlayer player)
	{
		return true;
	}

	protected boolean CastSpellToggledServer(EntityPlayer player)
	{
		return true;
	}

	protected void CastSpellUntoggledClient(EntityPlayer player) {}

	protected void CastSpellUntoggledServer(EntityPlayer player) {}
}
