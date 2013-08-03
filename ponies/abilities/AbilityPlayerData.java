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
	public static HashMap<Integer, EnergyUsePiece<Integer>> useAttemptsClient = new HashMap<Integer, EnergyUsePiece<Integer>>();
	@SideOnly(Side.CLIENT)
	public static int nextAttemptID = 0;
	@SideOnly(Side.CLIENT)
	public static int energyAttemptOffset = 0;
	@SideOnly(Side.CLIENT)
	public static HashMap<Integer, EnergyUsePiece<Float>> afterImageClient = new HashMap<Integer, EnergyUsePiece<Float>>();
	@SideOnly(Side.CLIENT)
	public static int nextAfterImageID = 0;
	@SideOnly(Side.CLIENT)
	public static float energyAfterImageOffset = 0;
	
	
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
		
		int currentTime = (int)(System.currentTimeMillis()/100L);
		for (int id : useAttemptsClient.keySet())
		{
			if (useAttemptsClient.get(id)==null)
				useAttemptsClient.remove(id);
			if (currentTime - useAttemptsClient.get(id).timestamp > 80)
			{
				cleanUse(id);
			}
			else
			{
				energyAttemptOffset += useAttemptsClient.get(id).cost;
			}
		}
		
		//trims energy with offset, to keep it from going to high.
		setEnergyWithOffset(energy);
		
		energyAfterImageOffset = 0;
		
		for (int id : afterImageClient.keySet())
		{
			if (afterImageClient.get(id)==null)
				afterImageClient.remove(id);
			if (id >= 0 && currentTime - afterImageClient.get(id).timestamp > 10)
			{
				cleanAfterImage(id);
			}
			else
			{
				energyAfterImageOffset += afterImageClient.get(id).cost;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static float getClientEnergyAfterImageRatio()
	{
		if (clientData == null)
			return 0;
		
		if (clientData.energy + energyAfterImageOffset >= clientData.energyMax)
			return 1;
		else if (clientData.energy + energyAfterImageOffset <= 0)
			return 0;
		else
			return (clientData.energy + energyAfterImageOffset) / (float) clientData.energyMax;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getClientEffectiveEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		if (clientData.energy - (float)energyAttemptOffset >= clientData.energyMax)
			return 1;
		else if (clientData.energy - (float)energyAttemptOffset<= 0)
			return 0;
		else
			return (clientData.energy  - (float)energyAttemptOffset) / (float) clientData.energyMax;
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
			return clientData.charge / clientData.chargeMax;
	}
	
	@SideOnly(Side.CLIENT)
	public static int attemptUse(int activeID, int cost)
	{
		int useID = nextAttemptID;
		nextAttemptID = (useID+1)%256;
		useAttemptsClient.put(useID, new EnergyUsePiece<Integer>(activeID, cost, (int)(System.currentTimeMillis()/100L)));
		energyAttemptOffset += cost;
		return useID;
	}
	
	@SideOnly(Side.CLIENT)
	public static void cleanUse(int useID)
	{
		energyAttemptOffset -= useAttemptsClient.get(useID).cost;
		useAttemptsClient.remove(useID);
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
			if (useAttemptsClient.containsKey(useID))
				afterImageClient.put(imageId, new EnergyUsePiece<Float>(activeID, (float)value, useAttemptsClient.get(useID).timestamp));
			else
				afterImageClient.put(imageId, new EnergyUsePiece<Float>(activeID, (float)value, (int)(System.currentTimeMillis()/100L)));
			clientData.addEnergy(-value);
			energyAfterImageOffset += value;
		}
		else
		{
			failedUseUpdateClient(activeID, (float)(value*20));
		}
		if (useAttemptsClient.containsKey(useID))
			energyAttemptOffset -= useAttemptsClient.get(useID).cost;
		useAttemptsClient.remove(useID);
	}
	
	@SideOnly(Side.CLIENT)
	public static void addToggleAfterImage(int activeID, int imageID, float cost, float costMax)
	{
		if (afterImageClient.containsKey(imageID))
		{
			cost+=afterImageClient.get(imageID).cost;
			if (cost > costMax)
				cost = costMax;
		}
		afterImageClient.put(imageID, new EnergyUsePiece<Float>(activeID, (float)cost, (int)(System.currentTimeMillis()/100L)));
	}
	
	@SideOnly(Side.CLIENT)
	public static void cleanAfterImage(int id)
	{
		afterImageClient.remove(id);
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
