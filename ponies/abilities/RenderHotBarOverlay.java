package loecraftpack.ponies.abilities;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHotBarOverlay
{
	//Reference variables
	public static RenderHotBarOverlay instance = new RenderHotBarOverlay();
	Minecraft mc = Minecraft.getMinecraft();
	protected float zLevel = 0.0f;
	
	
	public void renderHotBarOverlay(EnumSet<TickType> type, Object... tickData)
	{
		// setup render
		ScaledResolution res = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		FontRenderer fontRender = mc.fontRenderer;
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();
		mc.entityRenderer.setupOverlayRendering();
		
		this.mc.mcProfiler.startSection("actionBarOverlay");
		mc.renderEngine.bindTexture("/loecraftpack/gui/overlay.png");
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        if (this.mc.currentScreen instanceof GuiChat)
        {
        	GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        }
        GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0f);
        
        if(!this.mc.thePlayer.capabilities.isCreativeMode)
        	renderEnergyBar(width, height);
        
        renderChargeBar(width, height);
        
        if (this.mc.currentScreen instanceof GuiChat)
        	GL11.glDisable(GL11.GL_BLEND);
        
        renderAbilityOverlays(width, height);
        
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        this.mc.mcProfiler.endSection();

	}
	
	protected void renderChargeBar(int width, int height)
	{
		this.mc.mcProfiler.startSection("chargeBar");
		
		short chargeLength = 45;
		int posX = width / 2 + 92;
        int posY = height - chargeLength;

        if (ActiveAbility.chargeClientMAX> 0)
        {
            int progress = (int)(ActiveAbility.getCastTimeRatio() * (float)(chargeLength));
            this.drawTexturedModalRect(posX, posY, 0, 10, 5, chargeLength);

            if (progress > 0)
            {
                this.drawTexturedModalRect(posX, posY + chargeLength - progress, 5, 10 + chargeLength - progress, 5, progress);
            }
        }
        
        this.mc.mcProfiler.endSection();
	}
	
	protected void renderEnergyBar(int width, int height)
	{
		this.mc.mcProfiler.startSection("energyBar");
		
		short energyLength = 81;
		int posX = width / 2 + 10;
        int posY = height - 45;

        if (ActiveAbility.energyClientMAX > 0)
        {
            int progress = (int)(ActiveAbility.getEnergyRatio()  * (float)(energyLength));
            this.drawTexturedModalRect(posX, posY, 0, 0, energyLength, 5);

            if (progress > 0)
            {
                this.drawTexturedModalRect(posX + energyLength - progress, posY, energyLength - progress, 5, progress, 5);
            }
        }
        
        this.mc.mcProfiler.endSection();
	}
    
    protected void renderAbilityOverlays(int width, int height)
    {
    	for (int i = 0; i < 9; i++)
    	{
    		ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
    		if (stack != null)
    		{
	    		Item item = stack.getItem();
	    		if (item != null && item instanceof ItemAbility)
    				renderAbilityOverlay(width, height, i, ActiveAbility.getCooldown(stack.getItemDamage()), ActiveAbility.isToggled(stack.getItemDamage()));
    		}
    	}
    }
    
    protected void renderAbilityOverlay(int width, int height, int position, float coolDown, boolean isToggled)
    {
    	int posX = width / 2 - 88 + (20*position);
        int posY = height - 19;
        
        if (isToggled)
        {
        	drawToggledTexture(posX, posY, 26, 10, 16, 16, coolDown);
        }
        else if (coolDown > 0)
        {
        	drawCoolDownTexture(posX, posY, 10, 10, 16, 16, coolDown);
        }
    }
    
    /**
     * extracted from minecraft
     */
    protected void drawTexturedModalRect(int posX, int posY, int uCoord, int vCoord, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(posX + 0), (double)(posY + height), (double)this.zLevel, (double)((float)(uCoord + 0) * f), (double)((float)(vCoord + height) * f1));
        tessellator.addVertexWithUV((double)(posX + width), (double)(posY + height), (double)this.zLevel, (double)((float)(uCoord + width) * f), (double)((float)(vCoord + height) * f1));
        tessellator.addVertexWithUV((double)(posX + width), (double)(posY + 0), (double)this.zLevel, (double)((float)(uCoord + width) * f), (double)((float)(vCoord + 0) * f1));
        tessellator.addVertexWithUV((double)(posX + 0), (double)(posY + 0), (double)this.zLevel, (double)((float)(uCoord + 0) * f), (double)((float)(vCoord + 0) * f1));
        tessellator.draw();
    }
    
    protected void drawToggledTexture(int posX, int posY, int uCoord, int vCoord, int width, int height, float progress)
    {
    	float intensity = (float)(Math.sin(Math.toRadians(progress*360.0f))+1.0f)/6.0f;
    	float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(posX + 0    ), (double)(posY + height), (double)this.zLevel, (double)((float)(uCoord + 0    ) * f), (double)((float)(vCoord + height) * f1));
        tessellator.addVertexWithUV((double)(posX + width), (double)(posY + height), (double)this.zLevel, (double)((float)(uCoord + width) * f), (double)((float)(vCoord + height) * f1));
        tessellator.addVertexWithUV((double)(posX + width), (double)(posY + 0     ), (double)this.zLevel, (double)((float)(uCoord + width) * f), (double)((float)(vCoord + 0     ) * f1));
        tessellator.addVertexWithUV((double)(posX + 0    ), (double)(posY + 0     ), (double)this.zLevel, (double)((float)(uCoord + 0    ) * f), (double)((float)(vCoord + 0     ) * f1));
        tessellator.draw();
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, intensity);
        
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)(posX + 0    ), (double)(posY + height), 0.0f);
        tessellator.addVertex((double)(posX + width), (double)(posY + height), 0.0f);
        tessellator.addVertex((double)(posX + width), (double)(posY + 0     ), 0.0f);
        tessellator.addVertex((double)(posX + 0    ), (double)(posY + 0     ), 0.0f);
        tessellator.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    protected void drawCoolDownTexture(int posX, int posY, int uCoord, int vCoord, int width, int height, float progress)
    {
    	double angleD = progress*360.0f;
    	double angleR = Math.toRadians(angleD);
    	int stage = (int)angleD/45;
    	float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        double ratio = 0;
        
    	switch (stage)
    	{
    	case 0:
    		ratio = 1.0d-Math.tan(angleR);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + width/2        ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width/2                  ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2        ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width/2                  ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2        ), (double)(posY + 0       ), (double)this.zLevel, (double)((float)(uCoord + width/2                  ) * f), (double)((float)(vCoord + 0       ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2*(ratio)), (double)(posY + 0       ), (double)this.zLevel, (double)((float)(uCoord + (float)width/2.0f*(ratio)) * f), (double)((float)(vCoord + 0       ) * f1));
            tessellator.draw();
    		break;
    	case 1:
    		ratio = 1.0d-1.0d/Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + 0      ), (double)(posY + height/2*(ratio)), (double)this.zLevel, (double)((float)(uCoord + 0      ) * f), (double)((float)(vCoord + (float)height/2.0f*(ratio)) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height/2        ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height/2                  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + 0               ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + 0                         ) * f1));
            tessellator.addVertexWithUV((double)(posX + 0      ), (double)(posY + 0               ), (double)this.zLevel, (double)((float)(uCoord + 0      ) * f), (double)((float)(vCoord + 0                         ) * f1));
            tessellator.draw();
    		break;
    	case 2:
    		ratio = -1.0d/Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + 0      ), (double)(posY + height/2+height/2*(ratio)), (double)this.zLevel, (double)((float)(uCoord + 0      ) * f), (double)((float)(vCoord + height/2+(float)height/2.0f*(ratio)) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height/2                 ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height/2                           ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height/2                 ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height/2                           ) * f1));
            tessellator.addVertexWithUV((double)(posX + 0      ), (double)(posY + height/2                 ), (double)this.zLevel, (double)((float)(uCoord + 0      ) * f), (double)((float)(vCoord + height/2                           ) * f1));
            tessellator.draw();
    		break;
    	case 3:
    		ratio = 1.0d+Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + 0              ), (double)(posY + height  ), (double)this.zLevel, (double)((float)(uCoord + 0                        ) * f), (double)((float)(vCoord + height  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2*(ratio)), (double)(posY + height  ), (double)this.zLevel, (double)((float)(uCoord + (float)width/2.0f*(ratio)) * f), (double)((float)(vCoord + height  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2        ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width/2                  ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.addVertexWithUV((double)(posX + 0              ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + 0                        ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.draw();
    		break;
    	case 4:
    		ratio = Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + width/2                ), (double)(posY + height  ), (double)this.zLevel, (double)((float)(uCoord + width/2                          ) * f), (double)((float)(vCoord + height  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2+width/2*(ratio)), (double)(posY + height  ), (double)this.zLevel, (double)((float)(uCoord + width/2+(float)width/2.0f*(ratio)) * f), (double)((float)(vCoord + height  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2                ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width/2                          ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2                ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width/2                          ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.draw();
    		break;
    	case 5:
    		ratio = 1.0d/Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height                   ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height                             ) * f1));
            tessellator.addVertexWithUV((double)(posX + width  ), (double)(posY + height                   ), (double)this.zLevel, (double)((float)(uCoord + width  ) * f), (double)((float)(vCoord + height                             ) * f1));
            tessellator.addVertexWithUV((double)(posX + width  ), (double)(posY + height/2+height/2*(ratio)), (double)this.zLevel, (double)((float)(uCoord + width  ) * f), (double)((float)(vCoord + height/2+(float)height/2.0f*(ratio)) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height/2                 ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height/2                           ) * f1));
            tessellator.draw();
    		break;
    	case 6:
    		ratio = 1.0d+1.0d/Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height/2        ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height/2                  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width  ), (double)(posY + height/2        ), (double)this.zLevel, (double)((float)(uCoord + width  ) * f), (double)((float)(vCoord + height/2                  ) * f1));
            tessellator.addVertexWithUV((double)(posX + width  ), (double)(posY + height/2*(ratio)), (double)this.zLevel, (double)((float)(uCoord + width  ) * f), (double)((float)(vCoord + (float)height/2.0f*(ratio)) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2), (double)(posY + height/2        ), (double)this.zLevel, (double)((float)(uCoord + width/2) * f), (double)((float)(vCoord + height/2                  ) * f1));
            tessellator.draw();
    		break;
    	case 7:
    		ratio = -Math.tan(angleR);
    		tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(posX + width/2                ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width/2                          ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.addVertexWithUV((double)(posX + width                  ), (double)(posY + height/2), (double)this.zLevel, (double)((float)(uCoord + width                            ) * f), (double)((float)(vCoord + height/2) * f1));
            tessellator.addVertexWithUV((double)(posX + width                  ), (double)(posY + 0       ), (double)this.zLevel, (double)((float)(uCoord + width                            ) * f), (double)((float)(vCoord + 0       ) * f1));
            tessellator.addVertexWithUV((double)(posX + width/2+width/2*(ratio)), (double)(posY + 0       ), (double)this.zLevel, (double)((float)(uCoord + width/2+(float)width/2.0f*(ratio)) * f), (double)((float)(vCoord + 0       ) * f1));
            tessellator.draw();
    		break;
    	default:
    		this.drawTexturedModalRect(posX, posY, uCoord, vCoord, width, height);
    		return;
    	}
    	
    	switch (stage)
    	{
    	case 7:
    	case 6:
    		this.drawTexturedModalRect(posX+width/2, posY+height/2, uCoord+width/2, vCoord+height/2, width/2, height/2);
    	case 5:
    	case 4:
    		this.drawTexturedModalRect(posX, posY+height/2, uCoord, vCoord+height/2, width/2, height/2);
    	case 3:
    	case 2:
    		this.drawTexturedModalRect(posX, posY, uCoord, vCoord, width/2, height/2);
    	}
    }
}