package loecraftpack.common.entity;

import loecraftpack.LoECraftPack;
import loecraftpack.common.entity.ai.EntityAIPlayDead;
import loecraftpack.common.items.ItemBits;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityTimberWolf extends EntityMob {
	
	protected EntityAITasks possum;
	
	protected boolean activeForm = true;
	protected int dying = 0;
	protected int regenBuffer = 0;
	
	protected static float size = 3.0f;
	
	private float field_70926_e;
    private float field_70924_f;
    
	/** true is the wolf is wet else false */
    private boolean isShaking;
    private boolean field_70928_h;
    
    /** This time increases while wolf is shaking and emitting water particles. */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

	public EntityTimberWolf(World par1World)
	{
		super(par1World);
		
		possum = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		possum.addTask(1, new EntityAIPlayDead());
		
		this.texture = "/mods/loecraftpack/mob/timberwolf.png";
        this.setSize(size*0.6F, size*0.8F);
        this.experienceValue = 8;
        this.moveSpeed = 0.5F;
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, this.moveSpeed, true));
        this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
	}
	
	@Override
	protected void updateAITick()
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(this.getHealth()));
    }
	
	@Override
	public int getMaxHealth()
	{
		
		return 40;
	}
	
	@Override
	public int getAttackStrength(Entity par1Entity)
    {
        return 5;
    }
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, new Integer(this.getHealth()));
        //beg   : this.dataWatcher.addObject(19, new Byte((byte)0));
        //Collar: this.dataWatcher.addObject(20, new Byte((byte)BlockCloth.getBlockFromDye(1)));
    }
	
	@Override
	public boolean isAIEnabled()
    {
        return true;
    }
	
	/*******************************************************/
	/******************** Behavior *************************/
	/*******************************************************/
	
	@Override
	public void onLivingUpdate()
    {
		//effect of banish wears off a little
		if (dying>0)
		{
			dying--;
		}
		
		//body rebuilds a bit
		if (!activeForm)
		{
			regenBuffer=(regenBuffer+1)%10;
			if (regenBuffer==0)
			{
				heal(1);
				if (getHealth()==getMaxHealth()/2)
				{
					//body reactivates
					activeForm = true;
				}
			}
		}
		
        super.onLivingUpdate();

        if (!this.worldObj.isRemote && this.isShaking && !this.field_70928_h && !this.hasPath() && this.onGround)
        {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.worldObj.setEntityState(this, (byte)8);
        }
    }

	@Override
    public void onUpdate()
    {
        super.onUpdate();
        this.field_70924_f = this.field_70926_e;

        if (this.isBegging())
        {
            this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
        }
        else
        {
            this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
        }

        if (this.isBegging())
        {
            this.numTicksToChaseTarget = 10;
        }

        if (this.isWet())
        {
            this.isShaking = true;
            this.field_70928_h = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else if ((this.isShaking || this.field_70928_h) && this.field_70928_h)
        {
            if (this.timeWolfIsShaking == 0.0F)
            {
                this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;

            if (this.prevTimeWolfIsShaking >= 2.0F)
            {
                this.isShaking = false;
                this.field_70928_h = false;
                this.prevTimeWolfIsShaking = 0.0F;
                this.timeWolfIsShaking = 0.0F;
            }

            if (this.timeWolfIsShaking > 0.4F)
            {
                float f = (float)this.boundingBox.minY;
                int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

                for (int j = 0; j < i; ++j)
                {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.worldObj.spawnParticle("splash", this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ);
                }
            }
        }
    }
	
	@Override
	protected void updateAITasks()
    {
		if (activeForm)
			super.updateAITasks();
		else
		{
			++this.entityAge;
			this.worldObj.theProfiler.startSection("playDead");
			this.possum.onUpdateTasks();
			this.worldObj.theProfiler.endSection();
		}
    }
	
	/*******************************************************/
	/******************** Item Drop ************************/
	/*******************************************************/
	
	@Override
	protected int getDropItemId()
    {
        return Item.stick.itemID;
    }
	
	@Override
	protected void dropFewItems(boolean par1, int par2)
    {
        int j = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);
        
        if (j<4)
        {
        	for (int k = 0; k < j; ++k)
        	{
        		this.entityDropItem(new ItemStack(Item.stick.itemID, 1, 0), 0);
        	}
        }
        else
        {
            this.entityDropItem(new ItemStack(Block.wood, j-3, 0), 0);
        }
    }
	
	@Override
	protected void dropRareDrop(int par1)
    {
        switch (this.rand.nextInt(2))
        {
            case 0:
                this.entityDropItem(new ItemStack(LoECraftPack.itemZapApple, 1, 1), 0);
                break;
            case 1:
                this.entityDropItem(new ItemStack(LoECraftPack.blockZapAppleSapling, 1, 0), 0);
        }
    }
	
	@Override
	public void onKillEntity(EntityLiving par1EntityLiving)
    {
		if (par1EntityLiving instanceof EntityVillager)
			ItemBits.DropBits(5, par1EntityLiving);
		super.onKillEntity(par1EntityLiving);
    }
	
	/*******************************************************/
	/********************* States **************************/
	/*******************************************************/
	
	public boolean isBegging()//func_70922_bv()
    {
        return false;//this.dataWatcher.getWatchableObjectByte(19) == 1;
    }
	
	@SideOnly(Side.CLIENT)
    public float getTailRotation()
    {
        return this.isAngry() ? 1.5393804F : ((float)Math.PI / 5F);
    }
	
	public boolean isAngry()
    {
        return true;//(this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }
	
	public boolean isSitting()
    {
        return false;//(this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }
	
	@SideOnly(Side.CLIENT)
    public boolean getWolfShaking()
    {
        return this.isShaking;
    }
	
	/*******************************************************/
	/***************** Render checks ***********************/
	/*******************************************************/
	
	@SideOnly(Side.CLIENT)
    public float getInterestedAngle(float par1)
    {
        return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * par1) * 0.15F * (float)Math.PI;
    }
	
	@SideOnly(Side.CLIENT)
    /**
     * Used when calculating the amount of shading to apply while the wolf is shaking.
     */
    public float getShadingWhileShaking(float par1)
    {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1) / 2.0F * 0.25F;
    }

    @SideOnly(Side.CLIENT)
    public float getShakeAngle(float par1, float par2)
    {
        float f2 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1 + par2) / 1.8F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        else if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        return MathHelper.sin(f2 * (float)Math.PI) * MathHelper.sin(f2 * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 8)
        {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }
	
	/*******************************************************/
	/********************** Sound **************************/
	/*******************************************************/
	
	//Do: EntityTimberWolf - change this to a wood sound
	@Override
	protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }
	
	@Override
	protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }
	
	@Override
	protected String getDeathSound()
    {
        return "mob.wolf.death";
    }
	
	@Override
	protected float getSoundVolume()
    {
        return 0.4F;
    }
	
	/*******************************************************/
	/***************** Death & Injury **********************/
	/*******************************************************/
	
	@Override
	public void onDeath(DamageSource damageSource)
    {
		if (dying>0)
		{
			super.onDeath(damageSource);
		}
		else
		{
			//false death
			activeForm = false;
			isJumping = false;
			health = 1;
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, int par2)
	{
		Entity sourceEntity = damageSource.getSourceOfDamage();
		if (sourceEntity!= null)
		{
			ItemStack tool = null;
			
			if (sourceEntity instanceof EntityLiving)
			{
				if (sourceEntity instanceof EntityPlayer)
				{
					EntityPlayer attackingPlayer = (EntityPlayer)sourceEntity;
					tool = attackingPlayer.getHeldItem();
				}
			}
			
			if (tool != null)
			{
				  /****************************************/
				 /**Handle the effects of each type here**/
				/****************************************/
				int banishLevel = EnchantmentHelper.getEnchantmentLevel(LoECraftPack.banishEnchant.effectId, tool);
				if (dying < banishLevel*20 && activeForm)
				{
					dying = banishLevel*20;
				}
			}
		}
		return super.attackEntityFrom(damageSource, par2);
	}
	
	public boolean isPlayingDead()
	{
		return !activeForm;
	}
}
