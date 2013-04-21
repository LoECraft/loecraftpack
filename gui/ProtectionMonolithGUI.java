package loecraftpack.gui;

import java.awt.Color;
import java.text.DecimalFormat;

import loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import loecraftpack.packethandling.PacketHelper;
import loecraftpack.packethandling.PacketIds;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProtectionMonolithGUI extends GuiScreen
{
	final int xSizeOfTexture = 192
			, ySizeOfTexture = 135
		  , white = Color.white.getRGB()
		  , green = new Color(128, 255, 128).getRGB();
	int posX
	  , posY;
	
	ProtectionMonolithTileEntity te;
	
	public ProtectionMonolithGUI(ProtectionMonolithTileEntity te)
	{
		this.te = te;
	}
	
	public void initGui()
	{
		this.buttonList.clear();
	
		posX = (this.width - xSizeOfTexture) / 2;
		posY = (this.height - ySizeOfTexture) / 2;
	
		this.buttonList.add(new GuiButton(0, posX + 4, posY + 43, 20, 20, ""));
		this.buttonList.add(new GuiButton(1, posX + 44, posY + 43, 20, 20, ""));
		this.buttonList.add(new GuiButton(2, posX + 4, posY + 80, 20, 20, ""));
		this.buttonList.add(new GuiButton(3, posX + 44, posY + 80, 20, 20, ""));
		this.buttonList.add(new GuiButton(4, posX + 146, posY + 43, 20, 20, ""));
		this.buttonList.add(new GuiButton(5, posX + 166, posY + 63, 20, 20, ""));
		this.buttonList.add(new GuiButton(6, posX + 126, posY + 63, 20, 20, ""));
		this.buttonList.add(new GuiButton(7, posX + 146, posY + 83, 20, 20, ""));
		this.buttonList.add(new GuiButton(8, posX + 138, posY + 112, 48, 20, "Apply"));
	}
	
	public void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0:
				if (te.widthInc > 0)
					te.widthInc --;
				if ((te.width+te.widthInc)/2 <= te.offsetX+te.offsetXInc)
					te.offsetXInc --;
				if (1-Math.ceil((te.width+te.widthInc)/2f) >= te.offsetX+te.offsetXInc)
					te.offsetXInc ++;
				break;
			case 1:
				if (te.width+te.widthInc < 80)
					te.widthInc ++;
				break;
			case 2:
				if (te.lengthInc > 0)
					te.lengthInc --;
				if (1-Math.ceil((te.length+te.lengthInc)/2f) >= te.offsetZ+te.offsetZInc)
					te.offsetZInc ++;
				if ((te.length+te.lengthInc)/2 <= te.offsetZ+te.offsetZInc)
					te.offsetZInc --;
				break;
			case 3:
				if (te.length+te.lengthInc < 80)
					te.lengthInc ++;
				break;
			case 4:
				if (1-Math.ceil((te.length+te.lengthInc)/2f) < te.offsetZ+te.offsetZInc)
					te.offsetZInc --;
				break;
			case 5:
				if ((te.width+te.widthInc)/2 > te.offsetX+te.offsetXInc)
					te.offsetXInc ++;
				break;
			case 6:
				if (1-Math.ceil((te.width+te.widthInc)/2f) < te.offsetX+te.offsetXInc)
					te.offsetXInc --;
				break;
			case 7:
				if ((te.length+te.lengthInc)/2 > te.offsetZ+te.offsetZInc)
					te.offsetZInc ++;
				break;
			case 8:
				te.width += te.widthInc;
				te.length += te.lengthInc;
				te.offsetX += te.offsetXInc;
				te.offsetZ += te.offsetZInc;
				te.widthInc = 0;
				te.lengthInc = 0;
				te.offsetXInc = 0;
				te.offsetZInc = 0;
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.monolithEdit, te.xCoord, te.yCoord, te.zCoord, te.width, te.length, te.offsetX, te.offsetZ, te.getOwners()));
				this.mc.displayGuiScreen((GuiScreen)null);
				break;
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();
	
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/loecraftpack/gui/monolith.png");
	
		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;
	
		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
		this.drawCenteredString(fontRenderer, (te.Owners.size()==0?"Protection":te.Owners.get(0)+"'s") + " Monolith", posX + xSizeOfTexture/2, posY + 9, white);
		this.drawString(fontRenderer, "Width:", posX + 20, posY + 31, white);
		this.drawString(fontRenderer, "Length:", posX + 16, posY + 68, white);
		this.drawString(fontRenderer, "(+" + te.widthInc + ")", posX + 54, posY + 31, te.widthInc>0?green:white);
		this.drawString(fontRenderer, "(+" + te.lengthInc + ")", posX + 54, posY + 68, te.lengthInc>0?green:white);
		this.drawString(fontRenderer, "Move:", posX + 144, posY + 31, white);
		this.drawString(fontRenderer, ""+(new DecimalFormat("00")).format(te.width+te.widthInc), posX + 28, posY + 48, white);
		this.drawString(fontRenderer, ""+(new DecimalFormat("00")).format(te.length+te.lengthInc), posX + 28, posY + 85, white);
		this.drawString(fontRenderer, String.format("Cost: %s Bits", ((te.widthInc*te.lengthInc)+(te.width*te.lengthInc)+(te.length*te.widthInc))*10 + ((te.width+te.widthInc)*Math.abs(te.offsetZInc))*5 + ((te.length+te.lengthInc)*Math.abs(te.offsetXInc))*5), posX +32, posY + 118, white);
		super.drawScreen(x, y, f);
		this.mc.renderEngine.bindTexture("/loecraftpack/gui/monolith.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(posX + 148, posY + 45, 240, 0, 16, 16);  //up
		this.drawTexturedModalRect(posX + 168, posY + 65, 240, 48, 16, 16); //right
		this.drawTexturedModalRect(posX + 128, posY + 65, 240, 32, 16, 16); //left
		this.drawTexturedModalRect(posX + 148, posY + 85, 240, 16, 16, 16); //down
		
		this.drawTexturedModalRect(posX + 6, posY + 45, 224, 16, 16, 16); //width minus
		this.drawTexturedModalRect(posX + 46, posY + 45, 224, 0, 16, 16); //width plus
		this.drawTexturedModalRect(posX + 6, posY + 82, 224, 16, 16, 16); //length minus
		this.drawTexturedModalRect(posX + 46, posY + 82, 224, 0, 16, 16); //length plus
	}
}
