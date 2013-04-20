package loecraftpack.logic;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.gui.DialogGUI;
import loecraftpack.gui.GuiIds;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

enum Button {Next, Done}

@SideOnly(Side.CLIENT)
public class DialogLogic
{
	public static String dialogTitle = "Error - No Title";
	public static String[] dialogText = {"Error - No Data"};
	public static String nextButtonText = "Next";
	public static String doneButtonText = "Done";
	public static Button button = Button.Done; //changes with the other public variables when the message changes
	public static int index = 0; //Used to keep track of which message we're at.
	public static List<Message> messages = new ArrayList<Message>();
	private static String[] questParams;
	private static boolean openDialog = false;
	
	public static void AddMessage(String[] params)
	{
		if (params[0].equalsIgnoreCase("quest"))
		{
			if (messages.size() == 0)
				QuestLogic.parseQuest(params);
			else
			{
				if (messages.size() > 0)
					messages.get(messages.size()-1).button = Button.Next;
				questParams = params;
			}
		}
		else if (params[0].equalsIgnoreCase("shop"))
			ShopLogic.parseShop(params);
		switch(params.length)
		{
			case 2:
				if (Minecraft.getMinecraft().currentScreen != null)
				{
					messages.add(new Message(params[0], params[1], nextButtonText, doneButtonText, Button.Done));
					
					if (messages.size() > 1)
						messages.get(messages.size()-2).button = Button.Next;
				}
				break;
			case 3:
				if (params[0].equalsIgnoreCase("next"))
					messages.add(new Message(params[1], params[2], nextButtonText, doneButtonText, Button.Next));
				else if (params[0].equalsIgnoreCase("done"))
					messages.add(new Message(params[1], params[2], nextButtonText, doneButtonText, Button.Done));
				
				openDialog = true;
				
				break;
		}
		
		
		
		if (Minecraft.getMinecraft().currentScreen == null && openDialog)
		{
			Minecraft.getMinecraft().thePlayer.openGui(LoECraftPack.instance, GuiIds.Dialog, null, 0, 0, 0);
			ChangeMessage(false); //In this case (currentScreen == null), this is a shortcut to set up the static variables
			openDialog = false;
		}
	}
	
	public static void ResetMessages()
	{
		messages.clear();
		index = 0;
		openDialog = false;
	}
	
	public static boolean ChangeMessage(boolean forward)
	{
		if (forward)
			index++;
		else
			index--;
		
		if (index == -1)
			index = 0;
		else if (index >= messages.size())
		{
			index = messages.size() - 1;
			if (questParams == null)
				return false;
			else
			{
				QuestLogic.parseQuest(questParams);
				questParams = null;
				return true;
			}
		}
		
		dialogTitle = messages.get(index).dialogTitle;
		dialogText = messages.get(index).dialogText;
		nextButtonText = messages.get(index).nextButtonText;
		doneButtonText = messages.get(index).doneButtonText;
		button = messages.get(index).button;
		
		if (Minecraft.getMinecraft().currentScreen instanceof DialogGUI)
			((DialogGUI)Minecraft.getMinecraft().currentScreen).changeButtons(button.ordinal());
		
		return true;
	}
	
	public static int getButtonOrdinal()
	{
		return button.ordinal();
	}
}

class Message
{
	public String dialogTitle = "Error: No Title - Contact An Admin.";
	public String[] dialogText = {"Error: No Data", "Contact An Admin."};
	public String nextButtonText = "Next";
	public String doneButtonText = "Done";
	public Button button = Button.Done;
	
	public Message(String dialogTitle, String dialogText, String nextButtonText, String doneButtonText, Button button)
	{
		this.dialogTitle = dialogTitle;
		this.nextButtonText = nextButtonText;
		this.doneButtonText = doneButtonText;
		this.button = button;
		
		dialogText = dialogText.replaceAll("&((?i)[0-9a-fk-or])", "\u00A7$1");
		String str = "";
		List<String> temp = new ArrayList<String>();
		for(int i = 0; i < dialogText.length(); i ++ )
		{
			if (dialogText.charAt(i) == '\\' && (i < dialogText.length()-1 && dialogText.charAt(i+1) == '\\'))
			{
				temp.add(str);
				str = "";
				i += 2;
			}
			else if (Minecraft.getMinecraft().fontRenderer.getStringWidth(str) > 248)
			{
				int indexOfLastSpace = str.lastIndexOf(' ');
				if (indexOfLastSpace != -1)
				{
					temp.add(str.substring(0, indexOfLastSpace));
					i -= str.length() - indexOfLastSpace - 1;
				}
				else
					temp.add(str);
				
				str = "";
			}
			
			str += dialogText.charAt(i);
		}
		temp.add(str);
		this.dialogText = temp.toArray(new String[0]);
	}
}
