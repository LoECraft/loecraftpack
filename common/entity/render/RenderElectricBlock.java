package loecraftpack.common.entity.render;

import loecraftpack.common.entity.EntityElectricBlock;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class RenderElectricBlock extends Render {
	
	float frame;
	float frameTotal;
	float minU=0.0f;
	float maxU=1.0f;
	float minV;
	float maxV;
	boolean flag;
	
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		flag = false;
		frame = 0.0f;
		frameTotal = 1.0f;
		if (entity instanceof EntityElectricBlock)
		{
			frame = ((EntityElectricBlock)entity).getAge();
			frameTotal = ((EntityElectricBlock)entity).getMaxAge();
		}
		if (frame >= frameTotal || frame < 0)
			return;
		minV = frame/frameTotal;
		maxV = (frame+1.0f)/frameTotal;
		
		
		GL11.glPushMatrix();
		//get texture side
		this.loadTexture(entity.getTexture());
		renderFaceWest(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
        renderFaceNorth(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
        renderFaceEast(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
        renderFaceSouth(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
        //get texture top/bottom
		if (entity instanceof EntityElectricBlock)
		{
			this.loadTexture(((EntityElectricBlock)entity).getTextureSub());
			flag = true;
		}
		renderFaceTop(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
		renderFaceBottom(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
        GL11.glPopMatrix();
		
	}
	
	public void renderFaceTop(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5)
	{
		float minV;
		float maxV;
		if(flag)//render backwards?
		{
			minV = 1.0f - this.minV;
			maxV = 1.0f - this.maxV;
		}
		else
		{
			minV = this.minV;
			maxV = this.maxV;
		}
		Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(par1, par3, par5);
        tessellator.setNormal(0.0F, -1.0F, 0.0F);

        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ, maxU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ, minU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ, minU, maxV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}
	
	public void renderFaceBottom(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5)
	{
		Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(par1, par3, par5);
        tessellator.setNormal(0.0F, -1.0F, 0.0F);

        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ, maxU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ, minU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ, minU, maxV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}
	
	//control side for image rotation
	public void renderFaceWest(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5)
	{
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(par1, par3, par5);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);

        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ, maxU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ, minU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ, minU, maxV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}
	
	public void renderFaceNorth(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5)
	{
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(par1, par3, par5);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);

        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ, maxU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ, minU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ, minU, maxV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}
	
	public void renderFaceEast(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5)
	{
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(par1, par3, par5);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);

        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ, maxU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ, minU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ, minU, maxV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}
	
	public void renderFaceSouth(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5)
	{
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslation(par1, par3, par5);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);

        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ, maxU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ, minU, minV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ, minU, maxV);
        tessellator.addVertexWithUV(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ, maxU, maxV);
        
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}
}
