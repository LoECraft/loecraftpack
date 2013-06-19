package loecraftpack.ponies.abilities.mechanics;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class AbilityModeHandler 
{
	protected static List<Change> changes = new ArrayList<Change>();
	protected static int delay = 0;
	
	public static class Change
	{
		public String username = "";
		public Ability ability;
		public int state;
		public int life = 5;//5 Attempts if disconnected
		
		Change(String username, Ability ability, int state)
		{
			this.username = username;
			this.ability = ability;
			this.state = state;
		}
	}
	
	//used mainly during login
	public static void sync(EntityPlayer player)
	{
		MechanicTreeBucking.sync(player);
		//...
	}
	
	//used during logout
	public static void logout(EntityPlayer player)
	{
		MechanicTreeBucking.logout(player);
		//...
	}
	
	public static void abilityModeChange(EntityPlayer player, Ability ability, int state)
	{
		Change[] list = changes.toArray(new Change[changes.size()]);
		for (int i=0; i<list.length; i++)
		{
			if (list[i].username.matches(player.username) && list[i].ability==ability)
			{
				changes.remove(list[i]);
				break;
			}
		}
		changes.add(new Change(player.username, ability, state));
		PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.modeAbility, ability.ordinal(), state),(Player)player);
	}
	
	public static void clearSuccess(EntityPlayer player, Ability ability, int state)
	{
		Change[] list = changes.toArray(new Change[changes.size()]);
		for (int i=0; i<list.length; i++)
		{
			if (list[i] != null)
			{
				if (list[i].username.matches(player.username) && list[i].ability==ability && list[i].state == state)
				{
					changes.remove(list[i]);
					break;
				}
			}
		}
	}
	
	public static void retryAllRemaining()
	{
		delay = (delay+1)%20;//try once every sec
		if (delay == 0 && changes.size()>0)
		{
			System.out.println("HEY LISTEN!!!");
			Change[] list = changes.toArray(new Change[changes.size()]);
			for (int i=0; i<list.length; i++)
			{
				EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(list[i].username);
				if (player != null)
				{
					PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.modeAbility, list[i].ability.ordinal(), list[i].state),(Player)player);
					list[i].life = 5;
				}
				else if (list[i].life-- == 0)
				{
					changes.remove(list[i]);
				}
			}
		}
	}
}
