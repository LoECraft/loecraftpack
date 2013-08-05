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
	protected int CooldownGlobal = 0;
	protected int CooldownNormal = 0;
	protected int Cooldown = 0;
	protected float cooldown = 0;
	protected int Casttime = 0;
	protected float casttime = 0;
	private boolean held;
	private boolean heldChanged;
	private long time;
	private long lastTime;
	

	public ActiveAbility(String name, Race race, int cost, int globalCooldown)
	{
		super(name, race);
		CooldownNormal = 1;
		energyCost = cost;
		this.CooldownGlobal = globalCooldown;
		isToggleable = true;
	}

	public ActiveAbility(String name, Race race, int cost, int globalCooldown, int cooldown)
	{
		super(name, race);
		CooldownNormal = cooldown;
		energyCost = cost;
		this.CooldownGlobal = globalCooldown;
	}

	public ActiveAbility(String name, Race race, int cost, int globalCooldown, int cooldown, int casttime)
	{
		super(name, race);
		CooldownNormal = cooldown;
		Casttime = casttime;
		energyCost = cost;
		this.CooldownGlobal = globalCooldown;
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
		
		if (cooldown <= 0 && (toggled || player.capabilities.isCreativeMode || getEnergyCost(player) <= playerData.energy))
		{
			if (casttime >= Casttime)
			{
				System.out.println((isClient()?"Client: ":"Server: ") + playerData.energy);
				if ((isClient() && castSpellClient(player, world)) || (!isClient() && castSpellServer(player, world)))
				{
					cooldown = (Cooldown = CooldownNormal);
					casttime = 0;
					playerData.setCharge(0, 100);
					if (isClient() && !toggled)
						playerData.applyGlobalCooldown();
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
				playerData.addEnergy(-getEnergyCostToggled(player), isClient());

			if (playerData.energy > getEnergyCostToggled(player))
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
	
	public boolean isToggleable()
	{
		return isToggleable;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isToggled(int metadata)
	{
		return AbilityPlayerData.clientData.activeAbilities[metadata].isToggled();
	}
	
	@SideOnly(Side.CLIENT)
	public void applyGlobalCooldown()
	{
		System.out.println("----"+this+" "+cooldown+" "+CooldownGlobal);
		if (cooldown < CooldownGlobal)
			cooldown = (Cooldown = CooldownGlobal);
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
	
	/**
	 * activation cost
	 */
	public float getEnergyCost(EntityPlayer player)
	{
		return energyCost;
	}
	
	/**
	 * sustain cost
	 */
	public float getEnergyCostToggled(EntityPlayer player)
	{
		return 0;
	}
	
	/**
	 * used to inform the server of client-only things like particles, raycasting, energy use attempts.
	 * as well as handle client side logic
	 */
	protected abstract boolean castSpellClient(EntityPlayer player, World world);
	
	/**
	 * Ability logic server side. for abilities that don't require the client to send a special packet
	 */
	protected abstract boolean castSpellServer(EntityPlayer player, World world);
	
	/**
	 * packet triggered Ability logic - please override the method: castSpellServerPacket(Player player, int attemptID, DataInputStream data) throws IOException
	 */
	public void castSpellServerByHandler(Player player, DataInputStream data) throws IOException
	{
		//Debug: server casting availability check info
		System.out.println("SERVER CAST "+cooldown+" "+casttime+" "+Casttime);
		
		if (cooldown <= 0 && casttime >= (float)Casttime-0.1f)
		{
			if (castSpellServerPacket(player, data))
			{
				cooldown = (Cooldown = CooldownNormal);
				casttime = 0;
				return;
			}
		}
		PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, activeID, this.playerData.energy), player);
	}
	
	/**
	 * packet triggered Ability logic
	 */
	protected boolean castSpellServerPacket(Player player, DataInputStream data) throws IOException{return false;};

	protected boolean CastSpellToggledClient(EntityPlayer player){return true;}

	protected boolean CastSpellToggledServer(EntityPlayer player){return true;}

	protected void CastSpellUntoggledClient(EntityPlayer player) {}

	protected void CastSpellUntoggledServer(EntityPlayer player) {}
}
