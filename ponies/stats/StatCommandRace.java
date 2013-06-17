package loecraftpack.ponies.stats;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class StatCommandRace implements ICommand {
	
	private List aliases;
	
	public StatCommandRace()
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
		return "Race <name>";
	}

	@Override
	public List getCommandAliases() 
	{
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] string) 
	{
		if (string.length<1 || string.length>2)
		{
			icommandsender.sendChatToPlayer("Invalid number of arguments");
			return;
		}
		
		String goal = string[string.length-1].toLowerCase();
		String username = "";
		Race race = null;
		EntityPlayer player = null;
		
		
		//get user
		if(string.length==1)
			username = icommandsender.getCommandSenderName();
		else
			username = string[0];
		player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(username);
		
		//get race
		if (goal == "n" || goal.startsWith("no"))
			race = Race.NONE;
		else if(goal == "u" || goal.startsWith("uni"))
			race = Race.UNICORN;
		else if(goal == "p" || goal.startsWith("peg"))
			race = Race.PEGASUS;
		else if(goal == "e" || goal.startsWith("ear"))
			race = Race.EARTH;
		else if(goal == "a" || goal.startsWith("ali"))
			race = Race.ALICORN;
		
		if (player==null)
			icommandsender.sendChatToPlayer("Invalid player name");
		else if (race==null)
			icommandsender.sendChatToPlayer("Invalid race type");
		else
			LoECraftPack.statHandler.setRace(player, race);
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		// TODO Allow only Authorized access
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] string, int i) {
		// TODO Auto-generated method stub
		return false;
	}

}
