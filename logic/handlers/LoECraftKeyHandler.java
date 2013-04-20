package tekner.loecraftpack.logic.handlers;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import tekner.loecraftpack.gui.DialogGUI;
import tekner.loecraftpack.logic.DialogLogic;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LoECraftKeyHandler extends KeyHandler
{
	static KeyBinding renderMonolithKeybind = new KeyBinding("RenderMonolith", Keyboard.KEY_F12);
	static KeyBinding dialogShortcut = new KeyBinding("DialogShortcut", Keyboard.KEY_F);
	public static boolean renderMonolith = false;
	
	public LoECraftKeyHandler()
	{
		super(new KeyBinding[] {renderMonolithKeybind, dialogShortcut}, new boolean[] {false, false});
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
			if (kb.equals(renderMonolithKeybind))
				renderMonolith = !renderMonolith;
		}
		else if (kb.equals(dialogShortcut))
		{
			if (Minecraft.getMinecraft().currentScreen instanceof DialogGUI)
			{
				if (DialogLogic.getButtonOrdinal() == 0)
				{
					if (!DialogLogic.ChangeMessage(true))
						Minecraft.getMinecraft().thePlayer.sendChatMessage("next");
				}
				else
				{
					Minecraft.getMinecraft().thePlayer.sendChatMessage("done");
					Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				}
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}
}
