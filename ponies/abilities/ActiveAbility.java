package loecraftpack.ponies.abilities;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.active.AbilityBuckTree;
import loecraftpack.ponies.abilities.active.AbilityFireball;
import loecraftpack.ponies.abilities.active.AbilityOreVision;
import loecraftpack.ponies.abilities.active.AbilityTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ActiveAbility extends AbilityBase
{
	// CAUTION: Make sure this is updated when you add abilities.
	public static Class[] abilityClasses = new Class[] {AbilityFireball.class, AbilityTeleport.class, AbilityOreVision.class, AbilityBuckTree.class};
	public static String[] abilityNames = new String[abilityClasses.length];

	protected int activeID = -1;
	
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
		ActiveAbility[] templateAbilities = NewAbilityArray();
		for (int i = 0; i < templateAbilities.length; i++)
		{
			ActiveAbility ability = templateAbilities[i];
			LanguageRegistry.instance().addStringLocalization("item.itemAbility." + ability.icon + ".name", ability.name);
			abilityNames[i] = ability.name;
		}
	}

	public static ActiveAbility[] NewAbilityArray()
	{
		byte id = 0;
		ArrayList<ActiveAbility> abilityList = new ArrayList<ActiveAbility>();
		for (Class c : abilityClasses)
		{
			try
			{
				ActiveAbility ability = (ActiveAbility) c.getConstructor().newInstance();
				abilityList.add(ability);
				ability.activeID = id;
				id++;
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
				System.out.println((isClient()?"Client: ":"Server: ") + playerData.energy);
				if ((isClient() && CastSpellClient(player, world)) || (!isClient() && castSpellServer(player, world)))
				{
					cooldown = Cooldown;
					casttime = 0;
					playerData.setCharge(0, 100);
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
				
				playerData.setCharge(casttime, Casttime);
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
					
					playerData.setCharge(0, 100);
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

	public boolean castSpellServer(EntityPlayer player, World world){return isToggleable;}; // Ability logic Togglable, or rapid casting
	
	//packet triggered Ability logic - override the method below this
	public void castSpellServerByHandler(Player player, DataInputStream data) throws IOException
	{
		//Debug: server casting availability check info
		System.out.println("SERVER CAST "+cooldown+" "+casttime+" "+Casttime);
		
		int attemptID = data.readInt();
		if (cooldown <= 0 && casttime >= (float)Casttime-0.1f)
		{
			if (castSpellServerPacket(player, attemptID, data))
			{
				cooldown = Cooldown;
				casttime = 0;
				return;
			}
		}
		PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, activeID, attemptID, 0, (int)(casttime*20.0f)), player);
	}
	
	//method to override
	protected boolean castSpellServerPacket(Player player, int attemptID, DataInputStream data) throws IOException{return false;};

	protected boolean CastSpellToggledClient(EntityPlayer player){return true;}

	protected boolean CastSpellToggledServer(EntityPlayer player){return true;}

	protected void CastSpellUntoggledClient(EntityPlayer player) {}

	protected void CastSpellUntoggledServer(EntityPlayer player) {}
}
