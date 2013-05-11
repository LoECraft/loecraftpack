package loecraftpack.common.entity.render;

import loecraftpack.common.entity.EntityTimberWolf;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

public class RenderTimberWolf extends RenderLiving
{

	public RenderTimberWolf(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3)
    {
        super(par1ModelBase, par3);
        this.setRenderPassModel(par2ModelBase);
    }
	
	protected float getTailRotation(EntityTimberWolf entityTimberWolf, float par2)
    {
        return entityTimberWolf.getTailRotation();
    }
	
	protected int func_82447_a(EntityTimberWolf entityTimberWolf, int par2, float par3)
    {
        float f1;

        if (par2 == 0 && entityTimberWolf.getWolfShaking())
        {
            f1 = entityTimberWolf.getBrightness(par3) * entityTimberWolf.getShadingWhileShaking(par3);
            this.loadTexture(entityTimberWolf.getTexture());
            GL11.glColor3f(f1, f1, f1);
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving entityLiving, int par2, float par3)
    {
        return this.func_82447_a((EntityTimberWolf)entityLiving, par2, par3);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLiving entityLiving, float par2)
    {
        return this.getTailRotation((EntityTimberWolf)entityLiving, par2);
    }
}
