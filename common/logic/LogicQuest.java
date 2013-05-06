package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiDialog;
import loecraftpack.common.gui.GuiIds;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LogicQuest
{
	public static String questTitle = "Error: No Title - Contact An Admin.";
	public static String[] questTask = {"Error: No Data", "Contact An Admin."}; //The task is the explicit instruction of what to do 
	public static String[] rewardText = {"Error: No Data", "Contact An Admin."};
	
	public static void parseQuest(String[] params)
	{
		if (params.length == 4 && (Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen instanceof GuiDialog))
		{
			questTitle = params[1];
			questTask = params[2].split("\\\\\\\\");
			rewardText = params[3].split("\\\\\\\\");
			for(int i = 0; i < rewardText.length; i++ )
			{
				if (rewardText[i].indexOf(' ') == -1)
				{
					try
					{
						int bits = Integer.parseInt(rewardText[i]);
						rewardText[i] = LogicDialog.TranslateChatColor("&e" + bits + " &lBits");
					}
					catch(Exception e)
					{}
				}
				else
				{
					try
					{
						String[] split = rewardText[i].split(" ");
						int amount = Integer.parseInt(split[0]);
						rewardText[i] = LogicDialog.TranslateChatColor("&e" + amount + " &f&n" + rewardText[i].substring(split[0].length() + 1));
					}
					catch(Exception e)
					{}
				}
			}
			Minecraft.getMinecraft().thePlayer.openGui(LoECraftPack.instance, GuiIds.Quest, null, 0, 0, 0);
		}
	}
}
