package tekner.loecraftpack.blocks.te.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import tekner.loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import tekner.loecraftpack.logic.handlers.LoECraftKeyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

enum Orientation {Up, Down, West, East, North, South}

@SideOnly(Side.CLIENT)
public class ProtectionMonolithRenderer extends TileEntitySpecialRenderer
{
	Tessellator tessellator = Tessellator.instance;
	final Color blue = new Color(0, 32, 255, 64);
	final Color blueFaded = new Color(0, 32, 255, 26);
	final Color red = new Color(255, 32, 0, 64);
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		ProtectionMonolithTileEntity te = (ProtectionMonolithTileEntity)tileentity;
		
        GL11.glDisable(GL11.GL_TEXTURE_2D);
		
        //Draw monolith
      	renderPlane(x, y, z, 1, 1, Orientation.North, new Color(128, 128, 128));
      	renderPlane(x+1, y, z, 1, 1, Orientation.West, new Color(128, 128, 128));
      	renderPlane(x, y, z, 1, 1, Orientation.East, new Color(128, 128, 128));
      	renderPlane(x, y, z+1, 1, 1, Orientation.South, new Color(128, 128, 128));
      	renderPlane(x, y+1, z, 1, 1, Orientation.Up, new Color(128, 128, 128));
      	renderPlane(x, y, z, 1, 1, Orientation.Down, new Color(128, 128, 128));
      	if (LoECraftKeyHandler.renderMonolith)
		{
      		x += te.offsetX;
	        z += te.offsetZ;
	        int w = te.width
	          , l = te.length;
	        GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glDepthMask(false);
	        
	        Color color;
	        
	        //Draw preview area
	        if (te.offsetXInc != 0 || te.offsetZInc != 0 || te.widthInc > 0 || te.lengthInc > 0)
	        {
		        x += te.offsetXInc;
		        z += te.offsetZInc;
		        w += te.widthInc;
		        l += te.lengthInc;
		        color = new Color(0, 255, 32, 32+Math.abs((int)(Math.sin((te.worldObj.getTotalWorldTime()%40)*0.1570796d)*96)));
		        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord, z-Math.floor(l/2f)-0.01F, w+0.02F, 256.02F, Orientation.North, color);
		        renderDoublePlane(x+Math.ceil(w/2f)+0.01F, y-te.yCoord, z-Math.floor(l/2f)-0.01F, l+0.02F, 256.02F, Orientation.West, color);
		        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord, z-Math.floor(l/2f)-0.01F, l+0.02F, 256.02F, Orientation.East, color);
		        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord, z+Math.ceil(l/2f)+0.01F, w+0.02F, 256.02F, Orientation.South, color);
		        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord+256.02F, z-Math.floor(l/2f)-0.01F, w+0.02F, l+0.02F, Orientation.Down, color);
		        
		        x -= te.offsetXInc;
		        z -= te.offsetZInc;
		        w -= te.widthInc;
		        l -= te.lengthInc;
		        
		        color = te.isOwner(Minecraft.getMinecraft().thePlayer.username)?blueFaded:red;
	        }
	        else
	        	color = te.isOwner(Minecraft.getMinecraft().thePlayer.username)?blue:red;
	        
		    //Draw protection area
	        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord, z-Math.floor(l/2f)-0.01F, w+0.02F, 256.02F, Orientation.North, color);
	        renderDoublePlane(x+Math.ceil(w/2f)+0.01F, y-te.yCoord, z-Math.floor(l/2f)-0.01F, l+0.02F, 256.02F, Orientation.West, color);
	        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord, z-Math.floor(l/2f)-0.01F, l+0.02F, 256.02F, Orientation.East, color);
	        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord, z+Math.ceil(l/2f)+0.01F, w+0.02F, 256.02F, Orientation.South, color);
	        renderDoublePlane(x-Math.floor(w/2f)-0.01F, y-te.yCoord+256.02F, z-Math.floor(l/2f)-0.01F, w+0.02F, l+0.02F, Orientation.Down, color);
		}
      	GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	private void renderPlane(double x, double y, double z, double width, double height, Orientation orientation, Color color)
	{
        switch(orientation)
        {
        	case Up:
        		tessellator.startDrawingQuads();
        		tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        		tessellator.setNormal(0, -1.0f, 0);
        		tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y, z+height);
                tessellator.addVertex(x+width, y, z+height);
                tessellator.addVertex(x+width, y, z);
                tessellator.draw();
                break;
        	case Down:
        		tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.setNormal(0, 1.0f, 0);
                tessellator.addVertex(x+width, y, z);
                tessellator.addVertex(x+width, y, z+height);
                tessellator.addVertex(x, y, z+height);
                tessellator.addVertex(x, y, z);
                tessellator.draw();
        		break;
        	case North:
        		tessellator.startDrawingQuads();
        		tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        		tessellator.setNormal(0, 0, -1.0f);
        		tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x+width, y+height, z);
                tessellator.addVertex(x+width, y, z);
                tessellator.draw();
                break;
        	case South:
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.setNormal(0, 0, 1.0f);
                tessellator.addVertex(x+width, y, z);
                tessellator.addVertex(x+width, y+height, z);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x, y, z);
                tessellator.draw();
        		break;
        	case East:
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.setNormal(1.0f, 0, 0);
                tessellator.addVertex(x, y, z+width);
                tessellator.addVertex(x, y+height, z+width);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x, y, z);
                tessellator.draw();
        		break;
        	case West:
        		tessellator.startDrawingQuads();
        		tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        		tessellator.setNormal(-1.0f, 0, 0);
        		tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x, y+height, z+width);
                tessellator.addVertex(x, y, z+width);
                tessellator.draw();
                break;
        }
	}
	
	private void renderDoublePlane(double x, double y, double z, double width, double height, Orientation orientation, Color color)
	{
        switch(orientation)
        {
        	case Up:
        	case Down:
        		tessellator.startDrawingQuads();
        		tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        		tessellator.setNormal(0, -1.0f, 0);
        		tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y, z+height);
                tessellator.addVertex(x+width, y, z+height);
                tessellator.addVertex(x+width, y, z);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.setNormal(0, 1.0f, 0);
                tessellator.addVertex(x+width, y, z);
                tessellator.addVertex(x+width, y, z+height);
                tessellator.addVertex(x, y, z+height);
                tessellator.addVertex(x, y, z);
                tessellator.draw();
        		break;
        	case North:
        	case South:
        		tessellator.startDrawingQuads();
        		tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        		tessellator.setNormal(0, 0, -1.0f);
        		tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x+width, y+height, z);
                tessellator.addVertex(x+width, y, z);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.setNormal(0, 0, 1.0f);
                tessellator.addVertex(x+width, y, z);
                tessellator.addVertex(x+width, y+height, z);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x, y, z);
                tessellator.draw();
        		break;
        	case West:
        	case East:
        		tessellator.startDrawingQuads();
        		tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        		tessellator.setNormal(-1.0f, 0, 0);
        		tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x, y+height, z+width);
                tessellator.addVertex(x, y, z+width);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                tessellator.setNormal(1.0f, 0, 0);
                tessellator.addVertex(x, y, z+width);
                tessellator.addVertex(x, y+height, z+width);
                tessellator.addVertex(x, y+height, z);
                tessellator.addVertex(x, y, z);
                tessellator.draw();
        		break;
        }
	}
}
