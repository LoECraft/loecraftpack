package loecraftpack.ponies.abilities.mechanics;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;

/**
* This Command enables the use of the ore vision ability
*/
public class CommandHiddenOres implements ICommand {

	private List aliases;
	
	public CommandHiddenOres()
	{
		aliases = new ArrayList();
		aliases.add("orevision");
		aliases.add("OreVision");
	}
	
	@Override
	public int compareTo(Object o)
	{
		return 0;
	}

	@Override
	public String getCommandName()
	{
		return "OreVision";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/orevision : no arguments needed, simply call the command to enable this ability";
	}

	@Override
	public List getCommandAliases() 
	{
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		//TODO this this do what it's supposed to do
		String name = icommandsender.getCommandSenderName();
		if (!name.matches("Rcon"))
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name);
			if (player != null && !player.worldObj.isRemote)
			{
				//testing code: ore vision
				PotionEffect effect = player.getActivePotionEffect(LoECraftPack.potionOreVision);
				if (effect!= null)
				{
					player.addPotionEffect(new PotionEffect(LoECraftPack.potionOreVision.id, 1200, effect.getAmplifier()+1));
				}
				else
					player.addPotionEffect(new PotionEffect(LoECraftPack.potionOreVision.id, 1200, 0));
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	{
		String name = icommandsender.getCommandSenderName();
		if (!name.matches("Rcon"))
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name);
			if (player != null && LoECraftPack.statHandler.isRace(player, Race.UNICORN))
			{
				//TODO make this also check to see if the power is already enabled or re-charging
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
