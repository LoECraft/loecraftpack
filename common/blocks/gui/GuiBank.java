package loecraftpack.common.blocks.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.opengl.GL11;

public class GuiBank extends GuiScreen
{
	final int xSizeOfTexture = 256
			, ySizeOfTexture = 138
		  , white = Color.white.getRGB();
	int posX
	  , posY;
	GuiTextField amount;
	
	@Override
	public void initGui()
	{
		this.buttonList.clear();
	
		posX = (this.width - xSizeOfTexture) / 2;
		posY = (this.height - ySizeOfTexture) / 2;
		
		amount = new GuiTextField(fontRenderer, posX + 20, posY + 75, 64, 20);
		amount.setFocused(true);
		amount.setCanLoseFocus(false);
		amount.setMaxStringLength(6);
		
		this.buttonList.add(new GuiButton(0, posX + 4, posY + 43, 20, 20, "Withdraw"));
	}
	
	@Override
	public void keyTyped(char c, int i)
	{
		super.keyTyped(c, i);
		if ((c >= '0' && c <= '9') || c == '\b')
			amount.textboxKeyTyped(c, i);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();
	
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/loecraftpack/resources/mods/loecraftpack/gui/bank.png");
	
		posX = (this.width - xSizeOfTexture) / 2;
		posY = (this.height - ySizeOfTexture) / 2;
	
		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
		
		amount.drawTextBox();
		super.drawScreen(x, y, f);
	}
}
