package loecraftpack.ponies.abilities;

import java.util.HashMap;

import loecraftpack.LoECraftPack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class AbilityPlayerData
{
	@SideOnly(Side.CLIENT)
	public static AbilityPlayerData clientData = new AbilityPlayerData();
	
	public String playerName;
	private EntityPlayer player = null;
	public float energyRegenNatural = 10;
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
	
	public void OnUpdate(EntityPlayer player)
	{
		for(ActiveAbility ability : activeAbilities)
			ability.onUpdate(player);
		
		for(PassiveAbility ability : passiveAbilities)
			ability.onTick(player);
	}
	
	public static float getClientEnergyRatio()
	{
		if (clientData == null)
			return 0;
		
		if (clientData.energy >= clientData.energyMax)
			return 1;
		else if (clientData.energy <= 0)
			return 0;
		else
			return clientData.energy / (float) clientData.energyMax;
	}

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
}
