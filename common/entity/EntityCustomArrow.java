package loecraftpack.common.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

import loecraftpack.accessors.FieldAccessor;
import loecraftpack.accessors.ValueAccessor;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityCustomArrow extends EntityArrow
{
	protected static FieldAccessor<Integer> xTileField = new FieldAccessor<Integer>(EntityArrow.class, "xTile");
    protected ValueAccessor<Integer> xTile = new ValueAccessor<Integer>(this, xTileField);
    
    protected static FieldAccessor<Integer> yTileField = new FieldAccessor<Integer>(EntityArrow.class, "yTile");
    protected ValueAccessor<Integer> yTile = new ValueAccessor<Integer>(this, yTileField);
    
    protected static FieldAccessor<Integer> zTileField = new FieldAccessor<Integer>(EntityArrow.class, "zTile");
    protected ValueAccessor<Integer> zTile = new ValueAccessor<Integer>(this, zTileField);
    
    protected static FieldAccessor<Integer> inTileField = new FieldAccessor<Integer>(EntityArrow.class, "inTile");
    protected ValueAccessor<Integer> inTile = new ValueAccessor<Integer>(this, inTileField);
    
    protected static FieldAccessor<Integer> inDataField = new FieldAccessor<Integer>(EntityArrow.class, "inData");
    protected ValueAccessor<Integer> inData = new ValueAccessor<Integer>(this, inDataField);
    
    protected static FieldAccessor<Boolean> inGroundField = new FieldAccessor<Boolean>(EntityArrow.class, "inGround");
    protected ValueAccessor<Boolean> inGround = new ValueAccessor<Boolean>(this, inGroundField);
    
    

    /** 1 if the player can pick up the arrow */
    // public int canBePickedUp = 0;
    
    

    /** Seems to be some sort of timer for animating an arrow. */
    // public int arrowShake = 0;
    
    

    /** The owner of this arrow. */
    // public Entity shootingEntity;
    protected static FieldAccessor<Integer> ticksInGroundField = new FieldAccessor<Integer>(EntityArrow.class, "ticksInGround");
    protected ValueAccessor<Integer> ticksInGround = new ValueAccessor<Integer>(this, ticksInGroundField);
    
    protected static FieldAccessor<Integer> ticksInAirField = new FieldAccessor<Integer>(EntityArrow.class, "ticksInAir");
    protected ValueAccessor<Integer> ticksInAir = new ValueAccessor<Integer>(this, ticksInAirField);
    
    protected static FieldAccessor<Double> damageField = new FieldAccessor<Double>(EntityArrow.class, "damage");
    protected ValueAccessor<Double> damage = new ValueAccessor<Double>(this, damageField);

    
    
    /** The amount of knockback an arrow applies when it hits a mob. */
    protected static FieldAccessor<Integer> knockbackStrengthField = new FieldAccessor<Integer>(EntityArrow.class, "knockbackStrength");
    protected ValueAccessor<Integer> knockbackStrength = new ValueAccessor<Integer>(this, knockbackStrengthField);

    public EntityCustomArrow(World par1World)
    {
        super(par1World);
    }

    public EntityCustomArrow(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public EntityCustomArrow(World par1World, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving, float par4, float par5)
    {
        super(par1World, par2EntityLiving, par3EntityLiving, par4, par5);
    }
    
    /**
     * only called by bow
     */
    public EntityCustomArrow(World par1World, EntityLiving par2EntityLiving, float par3)
    {
        super(par1World, par2EntityLiving, par3);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        int i = this.worldObj.getBlockId(this.xTile.get(), this.yTile.get(), this.zTile.get());

        if (i > 0)
        {
            Block.blocksList[i].setBlockBoundsBasedOnState(this.worldObj, this.xTile.get(), this.yTile.get(), this.zTile.get());
            AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile.get(), this.yTile.get(), this.zTile.get());

            if (axisalignedbb != null && axisalignedbb.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
            {
                this.inGround.set(true);
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround.get())
        {
            int j = this.worldObj.getBlockId(this.xTile.get(), this.yTile.get(), this.zTile.get());
            int k = this.worldObj.getBlockMetadata(this.xTile.get(), this.yTile.get(), this.zTile.get());

            if (j == this.inTile.get() && k == this.inData.get())
            {
                this.ticksInGround.Increment();

                if (this.ticksInGround.get() == 1200)
                {
                    this.setDead();
                }
            }
            else
            {
                this.inGround.set(false);
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround.set(0);
                this.ticksInAir.set(0);
            }
        }
        else
        {
            this.ticksInAir.Increment();
            Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks_do_do(vec3, vec31, false, true);
            vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;

            for (l = 0; l < list.size(); ++l)
            {
                Entity entity1 = (Entity)list.get(l);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir.get() >= 5))
                {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double)f1, (double)f1, (double)f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).func_96122_a(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f3;

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit != null)
                {
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int i1 = MathHelper.ceiling_double_int((double)f2 * this.damage.get());

                    if (this.getIsCritical())
                    {
                        i1 += this.rand.nextInt(i1 / 2 + 2);
                    }

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null)
                    {
                        damagesource = DamageSource.causeArrowDamage(this, this);
                    }
                    else
                    {
                        damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    }

                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        movingobjectposition.entityHit.setFire(5);
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, i1))
                    {
                        if (movingobjectposition.entityHit instanceof EntityLiving)
                        {
                            EntityLiving entityliving = (EntityLiving)movingobjectposition.entityHit;

                            if (!this.worldObj.isRemote)
                            {
                                entityliving.setArrowCountInEntity(entityliving.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength.get() > 0)
                            {
                                f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (f3 > 0.0F)
                                {
                                    movingobjectposition.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength.get() * 0.6000000238418579D / (double)f3, 0.1D, this.motionZ * (double)this.knockbackStrength.get() * 0.6000000238418579D / (double)f3);
                                }
                            }

                            if (this.shootingEntity != null)
                            {
                                EnchantmentThorns.func_92096_a(this.shootingEntity, entityliving, this.rand);
                            }

                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                            {
                                ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                            }
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                        {
                            this.setDead();
                        }
                    }
                    else
                    {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir.set(0);
                    }
                }
                else
                {
                    this.xTile.set(movingobjectposition.blockX);
                    this.yTile.set(movingobjectposition.blockY);
                    this.zTile.set(movingobjectposition.blockZ);
                    this.inTile.set(this.worldObj.getBlockId(this.xTile.get(), this.yTile.get(), this.zTile.get()));
                    this.inData.set(this.worldObj.getBlockMetadata(this.xTile.get(), this.yTile.get(), this.zTile.get()));
                    this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround.set(true);
                    this.arrowShake = 7;
                    this.setIsCritical(false);

                    if (this.inTile.get() != 0)
                    {
                        Block.blocksList[this.inTile.get()].onEntityCollidedWithBlock(this.worldObj, this.xTile.get(), this.yTile.get(), this.zTile.get(), this);
                    }
                }
            }

            if (this.getIsCritical())
            {
                for (l = 0; l < 4; ++l)
                {
                    this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double)l / 4.0D, this.posY + this.motionY * (double)l / 4.0D, this.posZ + this.motionZ * (double)l / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
            {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f4 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                for (int j1 = 0; j1 < 4; ++j1)
                {
                    f3 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ);
                }

                f4 = 0.8F;
            }

            this.motionX *= (double)f4;
            this.motionY *= (double)f4;
            this.motionZ *= (double)f4;
            this.motionY -= (double)f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }


    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote && this.inGround.get() && this.arrowShake <= 0)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
}
