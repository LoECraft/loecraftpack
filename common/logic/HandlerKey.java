package loecraftpack.common.logic;

import java.util.EnumSet;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiDialog;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.enums.Race;
import loecraftpack.packet.NetworkedPotions;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.AbilityList;
import loecraftpack.ponies.inventory.GuiEarthPonyInventory;
import loecraftpack.ponies.inventory.GuiSpecialEquipment;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandlerKey extends KeyHandler
{
	static KeyBinding renderMonolithKeybind = new KeyBinding("RenderMonolith", Keyboard.KEY_F12);
	static KeyBinding dialogShortcut = new KeyBinding("DialogShortcut", Keyboard.KEY_F);
	static KeyBinding extendedInventory = new KeyBinding("CycleInv", Keyboard.KEY_R);
	public static KeyBinding jump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
	public static boolean renderMonolith = false;
	
	public HandlerKey()
	{
		super(new KeyBinding[] {renderMonolithKeybind, dialogShortcut, jump, extendedInventory},
		         new boolean[] {false,                 false,          true, false});
	}

	@Override
	public String getLabel()
	{
		return "LoECraft Key Handler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if (tickEnd && Minecraft.getMinecraft().thePlayer != null)
		{
			if (kb.equals(jump))
			{
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				if (LoECraftPack.statHandler.isRace(player, Race.EARTH) && player.motionY > 0)
					player.motionY *= 1.175f;
			}
			else if (kb.equals(renderMonolithKeybind))
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
