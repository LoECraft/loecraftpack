package loecraftpack.ponies.stats;

import java.util.HashMap;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.AbilityBase;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class StatHandlerServer
{
	public static Map stats = new HashMap<String, Stats>();
	
	public void addPlayer(String player) {} //to be overridden by the client stat handler
	
	public static void addPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		AbilityPlayerData data = AbilityPlayerData.RegisterPlayer(player.username);
		Stats playerStats = new Stats(data, Race.values()[nbt.getByte("Race")], nbt.getFloat("Energy"));
		stats.put(player.username, playerStats);
		
		PacketDispatcher.sendPacketToAllPlayers(PacketHelper.Make("loecraftpack", PacketIds.addPlayer, player.username));
		PacketDispatcher.sendPacketToAllPlayers(PacketHelper.Make("loecraftpack", PacketIds.applyStats, player.username, (byte)playerStats.race.ordinal()));
		
		for(AbilityBase ability : data.activeAbilities)
			ability.SetPlayer(player.username, data);
		
		for(AbilityBase ability : data.passiveAbilities)
			ability.SetPlayer(player.username, data);
	}
	
	public static void savePlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = player.getEntityData();
		Stats playerStats = (Stats)stats.get(player.username);
		nbt.setByte("Race", (byte)playerStats.race.ordinal());
		nbt.setFloat("Energy", 100);
		
		AbilityPlayerData.UnregisterPlayer(player.username);
	}
	
	public static boolean sendStatsToPlayer(EntityPlayer player)
	{
		if (stats.containsKey(player.username))
		{
			Stats stat = (Stats)stats.get(player.username);
			PacketDispatcher.sendPacketToAllPlayers(PacketHelper.Make("loecraftpack", PacketIds.applyStats, player.username, (byte)stat.race.ordinal()));
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//used by client side
	public void updatePlayerData(String player, Race race, float energy){}
	
	public boolean isRace(EntityPlayer player, Race race)
	{
		Stats playerStats = (Stats)stats.get(player.username);
		
		return playerStats.race == race || playerStats.race == Race.ALICORN; //Alicorn is master race
	}
	
	public Race getRace(EntityPlayer player)
	{
		return ((Stats)stats.get(player.username)).race;
	}
	
	public void setRace(EntityPlayer player, Race race)
	{
		((Stats)stats.get(player.username)).race = race;
	}
}
