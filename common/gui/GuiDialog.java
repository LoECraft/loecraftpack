package loecraftpack.common.gui;

import java.awt.Color;

import loecraftpack.common.logic.LogicDialog;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDialog extends GuiScreen
{
	final int xSizeOfTexture = 256
			, ySizeOfTexture = 192
			, white = Color.white.getRGB();
	int posX
	  , posY;
	
	boolean hitNext = false;
	
	public void initGui()
	{
		this.buttonList.clear();
	
		posX = (this.width - xSizeOfTexture) / 2;
		posY = (this.height - ySizeOfTexture) / 2;
	
		this.buttonList.add(new GuiButton(0, posX + 107, posY + 169, 42, 20, "Next"));
		this.buttonList.add(new GuiButton(1, posX + 107, posY + 169, 42, 20, "Done"));
	}
	
	public void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0:
				if (!LogicDialog.ChangeMessage(true))
					mc.thePlayer.sendChatMessage("next");
				hitNext = true;
				break;
				
			case 1:
				if (hitNext) //If this delay isn't in here, the instant transition from "Next" to "Done" will trigger both buttons
					hitNext = false;
				else
				{
					mc.thePlayer.sendChatMessage("done");
					mc.displayGuiScreen((GuiScreen)null);
				}
				break;
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();
	
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/loecraftpack/resources/mods/loecraftpack/gui/dialog.png");
	
		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;
	
		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
		drawCenteredString(fontRenderer, LogicDialog.dialogTitle, posX + xSizeOfTexture/2, posY + 9, white);
		
		for(int i = 0; i < LogicDialog.dialogText.length; i++ )
			drawCenteredString(fontRenderer, LogicDialog.dialogText[i], posX + xSizeOfTexture/2, posY + 31 + i*11, white);
		
		super.drawScreen(x, y, f);
	}
	
	@Override
	public void onGuiClosed()
	{
		LogicDialog.ResetMessages();
		hitNext = false;
	}
	
	public void changeButtons(int i)
	{
		switch(i)
		{
			case 0:
				((GuiButton)buttonList.get(0)).drawButton = true;
				((GuiButton)buttonList.get(1)).drawButton = false;
				break;
				
			case 1:
				((GuiButton)buttonList.get(0)).drawButton = false;
				((GuiButton)buttonList.get(1)).drawButton = true;
				break;
		}
	}
	
	public void changeNextButtonText(String str)
	{
		buttonList.set(0, new GuiButton(2, posX + 96 - fontRenderer.getStringWidth(str)/2 - 8, posY + 112, fontRenderer.getStringWidth(str) + 16, 20, str));
	}
	
	public void changeDoneButtonText(String str)
	{
		buttonList.set(1, new GuiButton(3, posX + 96 - fontRenderer.getStringWidth(str)/2 - 8, posY + 112, fontRenderer.getStringWidth(str) + 16, 20, str));
	}
}
