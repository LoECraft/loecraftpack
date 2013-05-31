package loecraftpack.common.entity;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPhantomArrow extends EntityCustomArrow {
	
	public EntityPhantomArrow(World par1World)
    {
        super(par1World);
        setDamage(1.0);
    }

    public EntityPhantomArrow(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        setDamage(1.0);
    }

    public EntityPhantomArrow(World par1World, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving, float par4, float par5)
    {
        super(par1World, par2EntityLiving, par3EntityLiving, par4, par5);
        setDamage(1.0);
    }
    
    public EntityPhantomArrow(World par1World, EntityLiving par2EntityLiving, float par3)
    {
    	super(par1World, par2EntityLiving, par3);
    	setDamage(1.0);
    }

    public EntityPhantomArrow(World par1World, EntityLiving par2EntityLiving, float par3, Random rand)
    {
    	super(par1World);
    	setDamage(1.0);
    	
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = par2EntityLiving;

        this.canBePickedUp = 2;

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
        
        float h = (rand.nextFloat()-0.5f);
        float v = (rand.nextFloat()-0.5f);
        this.posX -= (double)(MathHelper.cos((this.rotationYaw+90.0f) / 180.0F * (float)Math.PI) * 0.32F)*h;
        this.posY += 0.32f*v;
        this.posZ -= (double)(MathHelper.sin((this.rotationYaw+90.0f) / 180.0F * (float)Math.PI) * 0.32F)*h;
    }
}
