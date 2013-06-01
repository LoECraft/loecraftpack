package loecraftpack.common.entity.render;

import loecraftpack.common.entity.EntityPedestal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class RenderPedestal extends Render {
	
	public void doRenderPedestal(EntityPedestal entity, double d0, double d1, double d2, float f, float f1) 
	{
		GL11.glPushMatrix();
		
		renderItem(entity, entity.boundingBox, d0, d1, d2);
		
		this.loadTexture(entity.getTexture());
		renderStand(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
		
        GL11.glPopMatrix();
	}
	
	public void renderStand(AxisAlignedBB par0AxisAlignedBB, double xPos, double yPos, double zPos)
	{
		Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        double minX = par0AxisAlignedBB.minX;
        double maxX = par0AxisAlignedBB.maxX;
        double minY = par0AxisAlignedBB.minY;
        double maxY = par0AxisAlignedBB.minY + 0.1875d;
        double minZ = par0AxisAlignedBB.minZ;
        double maxZ = par0AxisAlignedBB.maxZ;
        
        /**
         * side top
         */
        float minU = 0.0625f;
        float maxU = 0.4375F;
        float minV = 0.5625f;
        float maxV = 0.9375f;
        
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);

        tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
        tessellator.addVertexWithUV(minX, maxY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * side bottom
         */
        minU = 0.5625f;
        maxU = 0.9375f;
        minV = 0.5625f;
        maxV = 0.9375f;
        
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, -1.0F, 0.0F);

        tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * side west
         */
        minU = 0.5625f;
        maxU = 0.9375f;
        minV = 0.40625f;
        maxV = 0.5f;
        
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);

        tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * side north
         */
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);

        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * side east
         */
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);

        tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * side south
         */
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);

        tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
        tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * 
         * GLOW
         * 
         */
        
        GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA);
    	
    	minX = par0AxisAlignedBB.minX + 0.125d;
        maxX = par0AxisAlignedBB.maxX - 0.125d;
        minY = par0AxisAlignedBB.minY + 0.1875d;
        maxY = par0AxisAlignedBB.minY + 0.5d;
        minZ = par0AxisAlignedBB.minZ + 0.125d;
        maxZ = par0AxisAlignedBB.maxZ - 0.125d;
        
    	minU = 0.125f;
        maxU = 0.375F;
        minV = 0.34375f;
        maxV = 0.5f;
        
        /**
         * 
         * INNER GLOW
         * 
         */
        
    	
        /**
         * glow west
         */
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        
        tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, minV);
        tessellator.addVertexWithUV(minX, maxY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * glow north
         */
    	tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, minU, minV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * glow east
         */
    	tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        
        tessellator.addVertexWithUV(maxX, minY, minZ, maxU, minV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * glow south
         */
    	tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        
        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);
        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        
        /**
         * 
         * OUTTER GLOW
         * 
         */
        
        
        /**
         * glow west
         */
        tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        
        tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, maxY, minZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, minZ, minU, minV);
        tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * glow north
         */
    	tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        
        tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(maxX, maxY, minZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, minZ, minU, minV);
        tessellator.addVertexWithUV(minX, minY, minZ, maxU, minV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * glow east
         */
    	tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        
        tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, minZ, maxU, minV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        /**
         * glow south
         */
    	tessellator.startDrawingQuads();
        tessellator.setTranslation(xPos, yPos, zPos);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        
        tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
        tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
        tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void renderItem(EntityPedestal entity, AxisAlignedBB par0AxisAlignedBB, double xPos, double yPos, double zPos)
	{
		GL11.glPushMatrix();
		ItemStack itemstack = entity.getDisplayedItem();

        if (itemstack != null)
        {
        	EntityItem entityitem = new EntityItem(entity.worldObj, xPos, yPos, zPos, itemstack);
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
            
            //apply coords
            GL11.glTranslatef((float)xPos, (float)yPos+0.5625f/*9 pixels*/, (float)zPos);
            
            GL11.glPushMatrix();
            
            //apply rotations
            switch(entity.displayMode)
            {
            case 0://slow rotate
	            GL11.glRotatef((float)entity.displayAngle, 0.0F, 1.0F, 0.0F);
	            GL11.glRotatef(-10, 1.0F, 0.0F, 0.0F);
	            GL11.glRotatef(-5, 0.0F, 0.0F, 1.0F);
            }
            
            
            
            //apply centering of item
            GL11.glTranslatef(0.0F, -0.125F/*2 pixels*/, 0.0F);
            
            RenderItem.renderInFrame = true;
        	RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        	RenderItem.renderInFrame = false;
        	
        	GL11.glPopMatrix();
        }
		
        GL11.glPopMatrix();
	}
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) 
	{
		doRenderPedestal((EntityPedestal)entity, d0, d1,  d2,  f, f1); 
	}

}
