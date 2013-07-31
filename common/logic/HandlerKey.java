package loecraftpack.common.logic;

import java.util.EnumSet;
import java.util.HashMap;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiDialog;
import loecraftpack.enums.Race;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandlerKey extends KeyHandler
{
	public static KeyBinding renderMonolithKeybind = new KeyBinding("RenderMonolith", Keyboard.KEY_F12);
	public static KeyBinding dialogShortcut = new KeyBinding("DialogShortcut", Keyboard.KEY_F);
	public static KeyBinding extendedInventory = new KeyBinding("CycleInv", Keyboard.KEY_R);
	public static KeyBinding jump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
	private static HashMap<KeyBinding, Boolean> keyDownMap = new HashMap<KeyBinding, Boolean>();
	
	public static boolean renderMonolith = false;
	
	public HandlerKey()
	{
		super(new KeyBinding[] {renderMonolithKeybind, dialogShortcut, jump, extendedInventory},
		         new boolean[] {false,                 false,          true, false});
		
		for(KeyBinding keybind : keyBindings)
			keyDownMap.put(keybind, false);
	}

	@Override
	public String getLabel()
	{
		return "LoECraft Key Handler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if (tickEnd)
		{
			keyDownMap.put(kb, true);
			
			if (Minecraft.getMinecraft().thePlayer != null)
			{
				/*if (kb.equals(jump))
				{
					EntityPlayer player = Minecraft.getMinecraft().thePlayer;
					if (LoECraftPack.statHandler.isRace(player, Race.EARTH) && player.motionY > 0)
						player.motionY *= 1.175f; //Adds an extra jump height of 1 block over time - stacks with jump boost potion effect
				}
				else */if (kb.equals(renderMonolithKeybind))
					renderMonolith = !renderMonolith;
				else if (kb.equals(dialogShortcut))
				{
					if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog)
					{
						if (LogicDialog.getButtonOrdinal() == 0)
						{
							if (!LogicDialog.ChangeMessage(true))
								Minecraft.getMinecraft().thePlayer.sendChatMessage("next");
						}
						else
						{
							Minecraft.getMinecraft().thePlayer.sendChatMessage("done");
							Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
						}
					}
				}
				else if (kb.equals(extendedInventory))
				{
					HandlerExtendedInventoryClient.cycleInventory();
				}
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		if (tickEnd)
		{
			keyDownMap.put(kb, false);
		}
	}
	
	public static boolean GetKeyDown(KeyBinding keybind)
	{
		return keyDownMap.get(keybind);
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}
}
