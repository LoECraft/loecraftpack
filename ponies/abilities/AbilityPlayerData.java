package loecraftpack.ponies.abilities;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import loecraftpack.LoECraftPack;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.stats.Stats;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AbilityPlayerData
{
	@SideOnly(Side.CLIENT)
	public static AbilityPlayerData clientData = new AbilityPlayerData();
	
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> useDrawBackRising = new EnergyUsePiece<Float>(0,0.0f,0);
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> useDrawBackFalling = new EnergyUsePiece<Float>(0,0.0f,0);
	
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> afterImageDrawBack = new EnergyUsePiece<Float>(0,0.0f,0);
	@SideOnly(Side.CLIENT)
	protected static HashMap<Long, Float> afterImageStorage = new HashMap<Long, Float>();
	@SideOnly(Side.CLIENT)
	protected static float afterImageHold = 0;
	
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> restoreDrawBack = new EnergyUsePiece<Float>(0,0.0f,0);
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> drainDrawBack = new EnergyUsePiece<Float>(0,0.0f,0);
	
	
	public String playerName;
	private EntityPlayer player = null;
	protected Stats playerStats = null;
	
	public float energyRegenNatural = 5;//Multiples of 5, for max accuracy
	public int energyMax = 500;
	public float energy;
	
	public float chargeMax = 100.0f;
	public float charge;
	
	public final ActiveAbility[] activeAbilities;
	public final PassiveAbility[] passiveAbilities;
	
	private static HashMap<String, AbilityPlayerData> map = new HashMap<String, AbilityPlayerData>();
	
	@SideOnly(Side.CLIENT)
	public AbilityPlayerData()
	{
		this.activeAbilities = ActiveAbility.NewAbilityArray();
		this.passiveAbilities = PassiveAbility.NewAbilityArray();
		playerName = "";
	}
	
	public AbilityPlayerData(String player, ActiveAbility[] activeAbilities, PassiveAbility[] passiveAbilities)
	{
		this.activeAbilities = activeAbilities;
		this.passiveAbilities = passiveAbilities;
		playerName = player;
	}

	
	
	/**************************************/
	/****  External Access / Register  ****/
	/**************************************/
	
	public static AbilityPlayerData Get(String player)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			return clientData;
		else
			return map.get(player);
	}
	
	public static boolean HasPlayer(String player)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && player == Minecraft.getMinecraft().thePlayer.username)
			return true;
		else
			return map.containsKey(player);
	}
	
	public static AbilityPlayerData RegisterPlayer(String player)
	{	
		AbilityPlayerData playerData = new AbilityPlayerData(player, ActiveAbility.NewAbilityArray(), PassiveAbility.NewAbilityArray());

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Minecraft.getMinecraft().thePlayer.username.equals(player))
		{
			clientData = new AbilityPlayerData(player, ActiveAbility.NewAbilityArray(), PassiveAbility.NewAbilityArray());
			
			for(AbilityBase ability : clientData.activeAbilities)
				ability.SetPlayer(player, clientData);
			
			for(AbilityBase ability : clientData.passiveAbilities)
				ability.SetPlayer(player, clientData);
			
			clientData.bindPlayerStats(player);
		}
		else
			map.put(player, playerData);
		
		return playerData;
	}
	
	public void bindPlayerStats(String player)
	{
		playerStats = (Stats)LoECraftPack.statHandler.stats.get(player);
	}
	
	public static void UnregisterPlayer(String player)
	{
		map.remove(player);
	}
	
	public Stats getPlayerStats()
	{
		return playerStats;
	}
	
	
	
	
	/**************************************/
	/************  UPDATES  ***************/
	/**************************************/
	
	public void onUpdateSERVER(EntityPlayer player)
	{
		addEnergy(energyRegenNatural/20f, false);
		
		for(ActiveAbility ability : activeAbilities)
			ability.onUpdate(player);
		
		for(PassiveAbility ability : passiveAbilities)
			ability.onTick(player);
	}
	
	public void onUpdateCLIENT(EntityPlayer player)
	{
		restoreOrDrainEnergy(energyRegenNatural/20f);
		
		long currentTime = System.currentTimeMillis();
		Object[] timeStamps = afterImageStorage.keySet().toArray();
		for (Object obj : timeStamps)
		{
			long timeStamp = Long.valueOf(obj.toString());
			if (currentTime - timeStamp >= 500)
			{
				float cost = afterImageStorage.get(timeStamp);
				afterImageStorage.remove(timeStamp);
				
				afterImageDrawBack.cost = getDrawBack(afterImageDrawBack) + cost;
				afterImageDrawBack.timestamp = System.currentTimeMillis();
				afterImageHold -= cost;
			}
		}
		
	}
	
	
	
	
	
	/**************************************/
	/*****  GET/SET - ENERGY/CHARGE  ******/
	/**************************************/
	
	/**
	 * set energy
	 */
	public void setEnergy(float newEnergy, boolean isClient)
	{
		if (isClient)
		{
			float oldEnergy = clientData.energy;
			clientData.energy = Math.min(energyMax, Math.max(0, newEnergy));
			float energyDifference = clientData.energy-oldEnergy;
			
			if (clientData.energy-oldEnergy>0)
			{
				useDrawBackRising.cost = getDrawBack(useDrawBackRising) + energyDifference;
				useDrawBackRising.timestamp = System.currentTimeMillis();
			}
			else
			{
				useDrawBackFalling.cost = getDrawBack(useDrawBackFalling) - energyDifference;
				useDrawBackFalling.timestamp = System.currentTimeMillis();
			}
		}
		else
		{
			energy = Math.min(energyMax, Math.max(0, newEnergy));
		}
	}
	
	/**
	 * add/subtract energy
	 */
	public void addEnergy(float energyDifference, boolean isClient)
	{
		if (isClient)
		{
			clientData.energy = Math.min(energyMax, Math.max(0, energy + energyDifference));
			if (energyDifference>0)
			{
				useDrawBackRising.cost = getDrawBack(useDrawBackRising) + energyDifference;
				useDrawBackRising.timestamp = System.currentTimeMillis();
			}
			else
			{
				useDrawBackFalling.cost = getDrawBack(useDrawBackFalling) - energyDifference;
				useDrawBackFalling.timestamp = System.currentTimeMillis();
			}
		}
		else
		{
			energy = Math.min(energyMax, Math.max(0, energy + energyDifference));
		}
	}
	
	/**
	 * add/subtract energy - used by regen, restore, drain, etc.
	 */
	@SideOnly(Side.CLIENT)
	public void restoreOrDrainEnergy(float energyDifference)
	{
		float oldEnergy = clientData.energy;
		clientData.energy = Math.min(energyMax, Math.max(0, energy + energyDifference));
		
		if (energyDifference>0)
		{
			if (clientData.energy >= clientData.energyMax)
				restoreDrawBack.cost = clientData.energy - (oldEnergy - getDrawBack(restoreDrawBack));
			else
				restoreDrawBack.cost = getDrawBack(restoreDrawBack) + energyDifference;
			restoreDrawBack.timestamp = System.currentTimeMillis();
		}
		else
		{
			if (clientData.energy <= 0)
				drainDrawBack.cost = oldEnergy + getDrawBack(drainDrawBack);
			else
				drainDrawBack.cost = getDrawBack(drainDrawBack) - energyDifference;
			drainDrawBack.timestamp = System.currentTimeMillis();
		}
	}
	
	/**
	 * set charge
	 */
	public void setCharge(float partial, float max)
	{
		charge = partial;
		chargeMax = max;
	}
	
	
	
	
	/**************************************/
	/********  GET RENDER INFO  ***********/
	/**************************************/
	
	/**
	 * get the position of the charge
	 */
	@SideOnly(Side.CLIENT)
	public static float getClientCastTimeRatio()
	{
		if (clientData == null)
			return 0;
		
		if (clientData.charge >= clientData.chargeMax)
			return 1;
		else if (clientData.charge <= 0)
			return 0;
		else
			return (clientData.charge) / clientData.chargeMax;
	}
	
	/**
	 * get the position of the afterImage
	 */
	@SideOnly(Side.CLIENT)
	public static float getClientEnergyAfterImageRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy + afterImageHold + getDrawBack(afterImageDrawBack) - getDrawBack(useDrawBackRising) + getDrawBack(drainDrawBack) - getDrawBack(restoreDrawBack);
		
		return clampRatio(goal);
	}
	
	/**
	 * get the position of the estimated remaining energy
	 */
	@SideOnly(Side.CLIENT)
	public static float getClientEffectiveEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy + getDrawBack(useDrawBackFalling) - getDrawBack(useDrawBackRising) + getDrawBack(drainDrawBack) - getDrawBack(restoreDrawBack);
		
		return clampRatio(goal);
	}
	
	/**
	 * get the position of the regeneration / restore
	 */
	@SideOnly(Side.CLIENT)
	public static float getClientRegenEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy + getDrawBack(useDrawBackFalling) - getDrawBack(useDrawBackRising) + getDrawBack(drainDrawBack);
		
		return clampRatio(goal);
	}
	
	/**
	 * get the position of the drain / deplete
	 */
	@SideOnly(Side.CLIENT)
	public static float getClientDrainEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy + getDrawBack(useDrawBackFalling) - getDrawBack(useDrawBackRising) - getDrawBack(restoreDrawBack);
		
		return clampRatio(goal);
	}
	
	/**
	 * used to clamp the ratio position to a 0.0-1.0 value, in regards to max energy
	 */
	protected static float clampRatio(float goal)
	{
		if (goal >= clientData.energyMax)
			return 1;
		else if (goal <= 0)
			return 0;
		else
			return goal / (float) clientData.energyMax;
	}
	
	/**
	 * get the render equivalent of the gradual change due to some parameter.
	 */
	@SideOnly(Side.CLIENT)
	protected static float getDrawBack(EnergyUsePiece<Float> meter)
	{
		float progress = (float)(System.currentTimeMillis() - meter.timestamp);
		if (progress>=400.0f)
			return 0.0f;
		return (1-(progress / 400.0f))*meter.cost;
	}
	
	
	
	
	/****************************/
	/**********  OTHER  *********/
	/****************************/
	
	/**
	 *  store a after-image by sequential id
	 */
	@SideOnly(Side.CLIENT)
	public static void addAfterImage(float cost)
	{
		afterImageHold += cost;
		afterImageStorage.put(System.currentTimeMillis(), cost);
	}
	
	/**
	 * restore position of charge, if server denied the use of a non-toggle ability
	 */
	@SideOnly(Side.CLIENT)
	public static void handleDeny(int activeID, float setEnergy)
	{
		String abilityName = ActiveAbility.abilityNames[activeID];
		
		for(ActiveAbility ability : clientData.activeAbilities) 
		{
			if (ability.name.equals(abilityName))
			{
				ability.cooldown = 0;
				clientData.setEnergy(setEnergy, true);
				if (ability.isToggleable())
					ability.toggled = false;
				
				break;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void applyGlobalCooldown()
	{
		for (ActiveAbility ability : clientData.activeAbilities)
		{
			ability.applyGlobalCooldown();
		}
	}
	
	public void sendChangingPlayerStatPacket()
	{
		PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.statUpdate, energy), (Player)player);
	}
	
	@SideOnly(Side.CLIENT)
	public static void recieveChangingPlayerStatPacket(DataInputStream data) throws IOException
	{
		clientData.setEnergy(data.readFloat(), true);
	}
}
