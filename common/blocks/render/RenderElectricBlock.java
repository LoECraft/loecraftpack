package loecraftpack.common.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class RenderElectricBlock extends Render {
	
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		GL11.glPushMatrix();
		this.loadTexture(entity.getTexture());
        renderOffsetAABB(entity.boundingBox, d0 - entity.lastTickPosX, d1 - entity.lastTickPosY, d2 - entity.lastTickPosZ);
        GL11.glPopMatrix();
		
	}
}
