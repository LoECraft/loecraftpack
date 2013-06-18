package loecraftpack.ponies.stats;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandStatRace implements ICommand {
	
	private List aliases;
	
	public CommandStatRace()
	{
		aliases = new ArrayList();
		aliases.add("race");
		aliases.add("Race");
	}

	@Override
	public int compareTo(Object arg0) 
	{
		return 0;
	}

	@Override
	public String getCommandName() 
	{
		return "Race";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/race <Set/Get> <username:optional> <race:set>";
	}

	@Override
	public List getCommandAliases() 
	{
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] string) 
	{
		if (string.length<1 || string.length>3)
		{
			icommandsender.sendChatToPlayer("Invalid number of arguments");
			return;
		}
		
		int mode;
		String modeS = string[0].toLowerCase();
		System.out.println(modeS);
		if (modeS.matches("get"))
			mode = 0;
		else if (modeS.matches("set"))
		{
			if (string.length<2)
			{
				icommandsender.sendChatToPlayer("Invalid number of arguments");
				return;
			}
			mode = 1;
		}
		else
		{
			icommandsender.sendChatToPlayer("Invalid 2nd argument");
			return;
		}
		
		
		String username = "";
		Race race = null;
		EntityPlayer player = null;
		
		//get user
		if ( (mode==1 && string.length==2) || (mode==0 && string.length==1))
			username = icommandsender.getCommandSenderName();
		else 
			username = string[1];
		player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(username);
		if (player==null)
			icommandsender.sendChatToPlayer("Invalid player name");
		
		else if (mode == 0)
		{
			//get current race
			icommandsender.sendChatToPlayer(username+"'s race is: "+LoECraftPack.statHandler.getRace(player));
		}
		else
		{
			//get new race
			String goal = string[string.length-1].toLowerCase();
			if (goal.matches("n") || goal.startsWith("no"))
				race = Race.NONE;
			else if (goal.matches("u") || goal.startsWith("uni"))
				race = Race.UNICORN;
			else if (goal.matches("p") || goal.startsWith("peg"))
				race = Race.PEGASUS;
			else if (goal.matches("e") || goal.startsWith("ear"))
				race = Race.EARTH;
			else if (goal.matches("a") || goal.startsWith("ali"))
				race = Race.ALICORN;
			if (race==null)
				icommandsender.sendChatToPlayer("Invalid race type");
			else
			{
				//set race
				LoECraftPack.statHandler.setRace(player, race);
				icommandsender.sendChatToPlayer(username+"'s race is set to: "+LoECraftPack.statHandler.getRace(player));
			}
		}
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		// TODO Allow only Authorized access
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] string) 
	{
		List tab = new ArrayList();
		switch(string.length)
		{
		case 0:
			return null;
		case 1:
			String part = string[0].toLowerCase();
			if (part.matches(""))
			{
				tab.add("Set");
				tab.add("Get");
			}
			else if ("set".startsWith(part))
				tab.add("Set");
			else if ("get".startsWith(part))
				tab.add("Get");
			break;
		case 2:
			String function = string[0].toLowerCase();
			part = string[1].toLowerCase();
			String[] users = MinecraftServer.getServer().getConfigurationManager().getAllUsernames();
			if (function.matches("get") || function.matches("set"))
			{
				if (part.matches(""))
				{
					if (function.matches("get"))
					{
						tab.add("");
					}
					else
					{
						tab.add("None");
						tab.add("Earth");
						tab.add("Pegasus");
						tab.add("Unicorn");
						tab.add("Alicorn");
					}
					for (int i=0; i<users.length; i++)
						tab.add(users[i]);
				}
				else
				{
					if (function.matches("set"))
					{
						if ("none".startsWith(part))
							tab.add("None");
						else if ("earth".startsWith(part))
							tab.add("Earth");
						else if ("pegasus".startsWith(part))
							tab.add("Pegasus");
						else if ("unicorn".startsWith(part))
							tab.add("Unicorn");
						else if ("alicorn".startsWith(part))
							tab.add("Alicorn");
					}
					for (int i=0; i<users.length; i++)
						if (users[i].toLowerCase().startsWith(part))
							tab.add(users[i]);
				}
			}
			break;
		case 3:
			function = string[0].toLowerCase();
			if (!function.matches("set"))
				return null;
			part = string[2].toLowerCase();
			if (part.matches(""))
			{
				tab.add("None");
				tab.add("Earth");
				tab.add("Pegasus");
				tab.add("Unicorn");
				tab.add("Alicorn");
			}
			else
			{
				if ("none".startsWith(part))
					tab.add("None");
				else if ("earth".startsWith(part))
					tab.add("Earth");
				else if ("pegasus".startsWith(part))
					tab.add("Pegasus");
				else if ("unicorn".startsWith(part))
					tab.add("Unicorn");
				else if ("alicorn".startsWith(part))
					tab.add("Alicorn");
			}
			break;
		default:
			return null;
		}
		if(tab.size()>0)
			return tab;
		else
			return null;
	}

	@Override
	public boolean isUsernameIndex(String[] string, int i) {
		if (i==1)
		{
			if (string[0].toLowerCase().matches("get"))
				return true;
			else if (string[0].toLowerCase().matches("set"))
			{
				if (string.length > 2)
					return true;
			}
		}
		return false;
	}

}
