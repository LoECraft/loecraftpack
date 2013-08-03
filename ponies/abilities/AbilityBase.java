package loecraftpack.ponies.abilities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.Stats;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public abstract class AbilityBase
{
	public String name;
	public String icon;
	protected Race race;
	private boolean isClient = false;
	
	protected String playerName = "";
	protected AbilityPlayerData playerData = null;
	protected Stats playerStats = null;
	public int id = -1;
	
	private static int index = 0;
	
	public AbilityBase(String name, Race race)
	{
		this.isClient = (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT);
		this.name = name;
		this.icon = name.toLowerCase().replace(" ", "");
		this.race = race;
	}
	
	public void SetPlayer(String player, AbilityPlayerData data)
	{
		playerName = player;
		playerData = data;
		playerStats = (Stats)LoECraftPack.statHandler.stats.get(player);
	}
	
	public boolean isClient()
	{
		return isClient;
	}
	
	public void SetID()
	{
		id = index++;
	}
}
