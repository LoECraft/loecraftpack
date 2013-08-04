package loecraftpack.ponies.abilities;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AbilityPlayerData
{
	@SideOnly(Side.CLIENT)
	public static AbilityPlayerData clientData = new AbilityPlayerData();
	
	@SideOnly(Side.CLIENT)
	protected static HashMap<Integer, EnergyUsePiece<Integer>> useAttempts = new HashMap<Integer, EnergyUsePiece<Integer>>();
	@SideOnly(Side.CLIENT)
	protected static int nextAttemptID = 0;
	@SideOnly(Side.CLIENT)
	public static int energyAttemptOffset = 0;
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> useAttemptsDrawBackRising = new EnergyUsePiece<Float>(0,0.0f,0);
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> useAttemptsDrawBackFalling = new EnergyUsePiece<Float>(0,0.0f,0);
	
	@SideOnly(Side.CLIENT)
	public static HashMap<Integer, EnergyUsePiece<Float>> afterImage = new HashMap<Integer, EnergyUsePiece<Float>>();
	@SideOnly(Side.CLIENT)
	protected static int nextAfterImageID = 0;
	@SideOnly(Side.CLIENT)
	protected static float energyAfterImageOffset = 0;
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> afterImageDrawBack = new EnergyUsePiece<Float>(0,0.0f,0);
	
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> restoreDrawBack = new EnergyUsePiece<Float>(0,0.0f,0);
	@SideOnly(Side.CLIENT)
	protected static EnergyUsePiece<Float> drainDrawBack = new EnergyUsePiece<Float>(0,0.0f,0);
	
	
	public String playerName;
	private EntityPlayer player = null;
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
	
	public void setEnergy(float newEnergy)
	{
		energy = Math.min(energyMax, Math.max(0, newEnergy));
	}
	
	public void addEnergy(float energyDifference)
	{
		energy = Math.min(energyMax, Math.max(0, energy + energyDifference));
	}
	
	@SideOnly(Side.CLIENT)
	public void setEnergyWithOffset(float newEnergy)
	{
		energy = Math.min(energyMax + energyAttemptOffset, Math.max(0, newEnergy));
	}
	
	@SideOnly(Side.CLIENT)
	public void addEnergyWithOffset(float energyDifference)
	{
		energy = Math.min(energyMax + energyAttemptOffset, Math.max(0, energy + energyDifference));
	}
	
	//uses drain and restore animation
	@SideOnly(Side.CLIENT)
	public void restoreOrDrainEnergyWithOffset(float energyDifference)
	{
		float oldEnergy = clientData.energy;
		addEnergyWithOffset(energyDifference);
		
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
		}
		else
			map.put(player, playerData);
		
		return playerData;
	}
	
	public static void UnregisterPlayer(String player)
	{
		map.remove(player);
	}
	
	public void onUpdateSERVER(EntityPlayer player)
	{
		for(ActiveAbility ability : activeAbilities)
			ability.onUpdate(player);
		
		for(PassiveAbility ability : passiveAbilities)
			ability.onTick(player);
	}
	
	public void onUpdateCLIENT(EntityPlayer player)
	{

		energyAttemptOffset = 0;
		
		long currentTime = (System.currentTimeMillis()/100L);
		for (int id : useAttempts.keySet())
		{
			if (useAttempts.get(id)==null)
				useAttempts.remove(id);
			if (currentTime - useAttempts.get(id).timestamp > 80)
			{
				cleanUse(id);
			}
			else
			{
				energyAttemptOffset += useAttempts.get(id).cost;
			}
		}
		
		//trims energy with offset, to keep it from going to high.
		setEnergyWithOffset(energy);
		
		energyAfterImageOffset = 0;
		
		for (int id : afterImage.keySet())
		{
			if (afterImage.get(id)==null)
				afterImage.remove(id);
			if (id >= 0 && currentTime - afterImage.get(id).timestamp > 10)
			{
				cleanAfterImage(id);
			}
			else
			{
				energyAfterImageOffset += afterImage.get(id).cost;
			}
		}
	}
	
	public void setCharge(float partial, float max)
	{
		charge = partial;
		chargeMax = max;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getClientEnergyAfterImageRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy + energyAfterImageOffset + getDrawBack(afterImageDrawBack) + getDrawBack(drainDrawBack) - getDrawBack(restoreDrawBack);
		
		return clampRatio(goal);
	}
	
	@SideOnly(Side.CLIENT)
	public static float getClientEffectiveEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy - (float)energyAttemptOffset + getDrawBack(useAttemptsDrawBackFalling) - getDrawBack(useAttemptsDrawBackRising) + getDrawBack(drainDrawBack) - getDrawBack(restoreDrawBack);
		
		return clampRatio(goal);
	}
	
	@SideOnly(Side.CLIENT)
	public static float getClientRegenEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy - (float)energyAttemptOffset + getDrawBack(useAttemptsDrawBackFalling) - getDrawBack(useAttemptsDrawBackRising) + getDrawBack(drainDrawBack);
		
		return clampRatio(goal);
	}
	
	@SideOnly(Side.CLIENT)
	public static float getClientDrainEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		float goal = clientData.energy - (float)energyAttemptOffset + getDrawBack(useAttemptsDrawBackFalling) - getDrawBack(useAttemptsDrawBackRising) - getDrawBack(restoreDrawBack);
		
		return clampRatio(goal);
	}
	
	protected static float clampRatio(float goal)
	{
		if (goal >= clientData.energyMax)
			return 1;
		else if (goal <= 0)
			return 0;
		else
			return goal / (float) clientData.energyMax;
	}
	
	@SideOnly(Side.CLIENT)
	protected static float getDrawBack(EnergyUsePiece<Float> meter)
	{
		float progress = (float)(System.currentTimeMillis() - meter.timestamp);
		if (progress>=400.0f)
			return 0.0f;
		return (1-(progress / 400.0f))*meter.cost;
	}
	
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
	
	@SideOnly(Side.CLIENT)
	public static int attemptUse(int activeID, int cost)
	{
		int useID = nextAttemptID;
		nextAttemptID = (useID+1)%256;
		useAttempts.put(useID, new EnergyUsePiece<Integer>(activeID, cost, (System.currentTimeMillis()/100L)));
		energyAttemptOffset += cost;
		useAttemptsDrawBackFalling.cost = getDrawBack(useAttemptsDrawBackFalling) + cost;
		useAttemptsDrawBackFalling.timestamp = System.currentTimeMillis();
		return useID;
	}
	
	@SideOnly(Side.CLIENT)
	public static void cleanUse(int useID)
	{
		useAttemptsDrawBackRising.cost = getDrawBack(useAttemptsDrawBackRising) + useAttempts.get(useID).cost;
		useAttemptsDrawBackRising.timestamp = System.currentTimeMillis();
		energyAttemptOffset -= useAttempts.get(useID).cost;
		useAttempts.remove(useID);
	}
	
	@SideOnly(Side.CLIENT)
	public static void cleanUse(int activeID, int useID, boolean worked, int value)//value is either cost or casttime, based on the value of worked
	{
		//Debug: information of returned ability use packet
		System.out.println("clean AID:"+activeID+" UID:"+useID+" W:"+worked+" V:"+value);
		if (worked)
		{
			int imageId = nextAfterImageID;
			nextAfterImageID = (imageId+1)%256;
			if (useAttempts.containsKey(useID))
				afterImage.put(imageId, new EnergyUsePiece<Float>(activeID, (float)value, useAttempts.get(useID).timestamp));
			else
			{
				afterImage.put(imageId, new EnergyUsePiece<Float>(activeID, (float)value, (System.currentTimeMillis()/100L)));
				useAttemptsDrawBackFalling.cost = getDrawBack(useAttemptsDrawBackFalling) + value;
				useAttemptsDrawBackFalling.timestamp = System.currentTimeMillis();
			}
			clientData.addEnergy(-value);
			energyAfterImageOffset += value;
		}
		else
		{
			failedUseUpdateClient(activeID, (float)(value*20));
		}
		if (useAttempts.containsKey(useID))
		{
			energyAttemptOffset -= useAttempts.get(useID).cost;
			useAttempts.remove(useID);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void addToggleAfterImage(int activeID, int imageID, float cost, float costMax)
	{
		if (afterImage.containsKey(imageID))
		{
			cost+=afterImage.get(imageID).cost;
			if (cost > costMax)
				cost = costMax;
		}
		afterImage.put(imageID, new EnergyUsePiece<Float>(activeID, (float)cost, (System.currentTimeMillis()/100L)));
	}
	
	@SideOnly(Side.CLIENT)
	public static void cleanAfterImage(int id)
	{
		afterImageDrawBack.cost = getDrawBack(afterImageDrawBack) + afterImage.get(id).cost;
		afterImageDrawBack.timestamp = System.currentTimeMillis();
		afterImage.remove(id);
	}
	
	@SideOnly(Side.CLIENT)
	protected static void failedUseUpdateClient(int activeID, float casttime)
	{
		String abilityName = ActiveAbility.abilityNames[activeID];
		
		for(ActiveAbility ability : clientData.activeAbilities) 
		{
			if (ability.name.equals(abilityName))
			{
				ability.casttime = casttime;
				ability.cooldown = 0;
				
				break;
			}
		}
	}
}
