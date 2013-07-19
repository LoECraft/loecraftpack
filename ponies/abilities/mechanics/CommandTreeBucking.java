package loecraftpack.ponies.abilities.mechanics;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * This Command enables the use of the tree bucking ability
 */
public class CommandTreeBucking implements ICommand {

	private List aliases;
	
	public CommandTreeBucking()
	{
		aliases = new ArrayList();
		aliases.add("bucktree");
	}
	
	@Override
	public int compareTo(Object obj)
    {
		return this.getCommandName().compareTo(((ICommand)obj).getCommandName());
    }

	@Override
	public String getCommandName()
	{
		return "bucktree";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/bucktree";
	}

	@Override
	public List getCommandAliases() 
	{
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		//Do: TreeBuckCommand - make this do what it's supposed to do
		String name = icommandsender.getCommandSenderName();
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name);
		if (player != null)
			MechanicTreeBucking.switchBuckServer(player);
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	{
		String name = icommandsender.getCommandSenderName();
		if (!name.matches("Rcon"))
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name);
			if (player != null && LoECraftPack.statHandler.isRace(player, Race.EARTH))
			{
				//Do: TreeBuckCommand - make this also check to see if the power is already enabled or re-charging
				return true;
			}
		}
		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) 
	{
		return false;
	}

}
