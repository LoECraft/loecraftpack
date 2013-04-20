package tekner.loecraftpack.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import tekner.loecraftpack.logic.QuestLogic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuestGUI extends GuiScreen
{
	final int xSizeOfTexture = 256
			, ySizeOfTexture = 192
			, white = Color.white.getRGB();
	int posX
	  , posY;
	  
	
	public void initGui()
	{
		this.buttonList.clear();
	
		posX = (this.width - xSizeOfTexture) / 2;
		posY = (this.height - ySizeOfTexture) / 2;
	
		this.buttonList.add(new GuiButton(0, posX + 204, posY + 128, 48, 20, "Accept"));
		this.buttonList.add(new GuiButton(1, posX + 204, posY + 160, 48, 20, "Decline"));
	}
	
	public void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0:
				mc.thePlayer.sendChatMessage("accept");
				mc.displayGuiScreen((GuiScreen)null);
				break;
				
			case 1:
				mc.thePlayer.sendChatMessage("decline");
				mc.displayGuiScreen((GuiScreen)null);
				break;
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();
	
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/tekner/loecraftpack/gui/quest.png");
	
		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;
	
		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
		this.drawCenteredString(fontRenderer, QuestLogic.questTitle, posX + xSizeOfTexture/2, posY + 9, white);
		
		int e = 0;
		for(int i = 0; i < QuestLogic.questTask.length; i++ )
			drawString(fontRenderer, "§l§0*§r " + QuestLogic.questTask[i], posX + 16, posY + 52 + i*12, white);
		
		for(int i = 0; i < QuestLogic.rewardText.length; i++ )
			drawString(fontRenderer, "§l§0*§r " + QuestLogic.rewardText[i], posX + 16, posY + 144 + i*12, white);
		
		super.drawScreen(x, y, f);
	}
}
