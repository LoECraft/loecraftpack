package loecraftpack.common.entity;

import loecraftpack.LoECraftPack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPedestal extends Entity {
	
	//Do: EntityPedestal - make this have sub types, or use imported Icons
	
    /*ITEM VARS*/
    public String name = null;
    protected float itemDropChance = 1.0F;
    protected double displayAngle;
    protected double displayAngleSub;
    
    /*AI VARS*/
    protected Entity closestEntity = null;
    protected Class watchedClass = EntityLiving.class;
    protected float detectRange = 6.0F;
    protected float rotatingSpeed = 4.0F;
    
    public EntityPedestal(World par1World)
    {
    	super(par1World);
        this.yOffset = 0.0F;
        this.setSize(0.75F, 0.8125F); // 12 x 13 pixils
    }
    
    public EntityPedestal(World par1World, int xPos, int yPos, int zPos, int side)
    {
        this(par1World);
        
        this.setPositionAdjacent(xPos, yPos, zPos, side);
        
        //System.out.println("x:"+xPos+" y:"+yPos+" z:"+zPos+" dir:"+side);
    }

	@Override
	protected void entityInit()
    {
		//display item
        this.getDataWatcher().addObjectByDataType(2, 5);
        //display mode
        this.getDataWatcher().addObject(3, Byte.valueOf((byte)0));
        //default angle
        this.getDataWatcher().addObject(4, Integer.valueOf((byte)0));
    }
	
	@Override
	public String getTexture()
    {
        return "/mods/loecraftpack/textures/blocks/decor/pedestal.png";
    }
	
	public float getEyeHeight()
    {
        return 0.5F;
    }
	
	public void setPositionAdjacent(int xPos, int yPos, int zPos, int side)
	{
		switch (side)
		{
		case 0:
			yPos--;
			break;
		case 1:
			yPos++;
			break;
		case 2:
			zPos--;
			break;
		case 3:
			zPos++;
			break;
		case 4:
			xPos--;
			break;
		case 5:
			xPos++;
			break;
		}
		this.setPosition((double)xPos+0.5d, (double)yPos+0.0d, (double)zPos+0.5d);
	}
	
	public void onUpdate()
    {
		if (this.worldObj.isRemote)
		{
			/**HANDLED CLIENT SIDE**/
			
			switch (getDisplayMode())
			{
			case 0://static position
				setDisplayAngle( getDefaultAngle() );
				break;
			case 1://rotate slowly
				setDisplayAngle( getDisplayAngle() + 90/(20*4) );
				break;
			case 2://track living
				updateAITasks();
				break;
			}
		}
    }
	
    public boolean canBeCollidedWith()
    {
        return true;
    }
    
    public boolean attackEntityFrom(DamageSource damageSource, int par2)
    {
    	if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {/*
            	Entity source = damageSource.getSourceOfDamage();
            	if (source instanceof EntityPlayer)
            	{
            		//block un-allowed changes.
            	}*/
            	
                this.setDead();
                this.setBeenAttacked();
                EntityPlayer entityplayer = null;

                if (damageSource.getEntity() instanceof EntityPlayer)
                {
                    entityplayer = (EntityPlayer)damageSource.getEntity();
                }

                if (entityplayer != null && entityplayer.capabilities.isCreativeMode)
                {
                    return true;
                }

                this.dropItemStack();
            }

            return true;
        }
    }
    
    public void moveEntity(double par1, double par3, double par5)
    {
    	//block un-allowed changes
    	
    	
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            this.setDead();
            this.dropItemStack();
        }
    }
    
    public void addVelocity(double par1, double par3, double par5)
    {
    	//block un-allowed changes
    	
    	
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            this.setDead();
            this.dropItemStack();
        }
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) 
	{		
        //item code
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.hasNoTags())
        {
            this.setDisplayedItem(ItemStack.loadItemStackFromNBT(nbttagcompound1));
            this.setDisplayMode(nbttagcompound.getByte("Mode"));
            int angle = nbttagcompound.getInteger("Direction");
            this.setDefaultAngle(angle);
            this.name = nbttagcompound.getString("SkullName");
        }
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
        //item code
        if (this.getDisplayedItem() != null)
        {
        	nbttagcompound.setCompoundTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
        	nbttagcompound.setByte("Mode", (byte)this.getDisplayMode());
        	nbttagcompound.setInteger("Direction", this.getDefaultAngle());
        	if (name != null)
            	nbttagcompound.setString("SkullName", name);
        }
	}
	
	public boolean isInRangeToRenderDist(double par1)
    {
        double d1 = 16.0D;
        d1 *= 64.0D * this.renderDistanceWeight;
        return par1 < d1 * d1;
    }

	
	
	
	/*********************/
	/***** item code *****/
	/*********************/

	public void dropItemStack()
    {
        this.entityDropItem(new ItemStack(LoECraftPack.itemPedestal), 0.0F);
        ItemStack itemstack = this.getDisplayedItem();

        if (itemstack != null && this.rand.nextFloat() < this.itemDropChance)
        {
            itemstack = itemstack.copy();
            itemstack.setItemFrame((EntityItemFrame)null);
            this.entityDropItem(itemstack, 0.0F);
        }
    }
	
	public ItemStack getDisplayedItem()
    {
        return this.getDataWatcher().getWatchableObjectItemStack(2);
    }

    public void setDisplayedItem(ItemStack itemStack)
    {
        itemStack = itemStack.copy();
        itemStack.stackSize = 1;
        this.getDataWatcher().updateObject(2, itemStack);
        this.getDataWatcher().setObjectWatched(2);
    }
	
	public int getDisplayMode()
	{
		return this.getDataWatcher().getWatchableObjectByte(3);
	}
	
	public void setDisplayMode(int par1)
    {
        this.getDataWatcher().updateObject(3, Byte.valueOf((byte)(par1 % 3)));
    }
	
	public int getDefaultAngle()
	{
		return this.getDataWatcher().getWatchableObjectInt(4);
	}
	
	public void setDefaultAngle(int par1)
    {
        this.getDataWatcher().updateObject(4, Integer.valueOf((par1)));
    }
	
	public double getDisplayAngle()
	{
		//return (double)this.getDataWatcher().getWatchableObjectInt(5)/100d;
		return displayAngle;
	}
	
	public void setDisplayAngle(double par1)
    {
        //this.getDataWatcher().updateObject(5, Integer.valueOf((int)(par1*100)));
		displayAngle = par1;
    }
	
	public double getDisplayAngleSub()
	{
		//return (double)this.getDataWatcher().getWatchableObjectInt(6)/100d;
		return displayAngleSub;
	}
	
	public void setDisplayAngleSub(double par1)
    {
        //this.getDataWatcher().updateObject(6, Integer.valueOf((int)(par1*100)));
		displayAngleSub = par1;
    }

    
    
    public boolean interact(EntityPlayer player)
    {
    	//prevent non-allowed from interacting
    	
    	if (!this.worldObj.isRemote)
    	{
	        if (this.getDisplayedItem() == null)
	        {
	            ItemStack itemstack = player.getHeldItem();
	
	            if (itemstack != null)
	            {
	                this.setDisplayedItem(itemstack);
	                
	                //used for when player-skulls are placed
	                if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("SkullOwner"))
	                {
	                    name = itemstack.getTagCompound().getString("SkullOwner");
	                }
	                
	                if (!player.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
	                {
	                    player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
	                }
	                
	                setDefaultAngle(180-(int)player.rotationYaw);
	            }
	        }
	        else
	        {
	        	//change mode
	        	if (getDisplayMode()<2)
	        		setDisplayMode(getDisplayMode()+1);
	        	else
	        		setDisplayMode(0);
	        	
	        	//set default angle
	        	setDefaultAngle(180-(int)player.rotationYaw);
	        }
    	}

    	return true;
    }
    
    
    /*******************/
    /*** tracking AI ***/
    /*******************/
    
    protected void updateAITasks()
    {
    	if (findTarget())
    	{
    		//rotate head to target
    		double d0 = this.closestEntity.posX - this.posX;
            double d1 = this.closestEntity.posY- (this.posY + (double)this.getEyeHeight());
            double d2 = this.closestEntity.posZ - this.posZ;
            double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float angleH = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float angleV = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            this.setDisplayAngle( this.updateRotation((float)this.getDisplayAngle(), -angleH, rotatingSpeed) );
            this.setDisplayAngleSub( this.updateRotation((float)this.getDisplayAngleSub(), angleV, rotatingSpeed) );
    	}
    	else
    	{
    		//rotate head to resting position
    		this.setDisplayAngle( this.updateRotation((float)this.getDisplayAngle(), (float)this.getDefaultAngle(), rotatingSpeed) );
            this.setDisplayAngleSub( this.updateRotation((float)this.getDisplayAngleSub(), 0.0F, rotatingSpeed) );
    	}
    }
    
    protected boolean findTarget()
    {
    	if (this.watchedClass == EntityPlayer.class)
        {
            this.closestEntity = this.worldObj.getClosestPlayerToEntity(this, (double)this.detectRange);
        }
        else
        {
            this.closestEntity = this.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.boundingBox.expand((double)this.detectRange, 3.0D, (double)this.detectRange), this);
        }
    	
        return this.closestEntity != null;
    }
    
    private float updateRotation(float currentAngle, float targetAngle, float speed)
    {
        float f3 = MathHelper.wrapAngleTo180_float(targetAngle - currentAngle);

        if (f3 > speed)
        {
            f3 = speed;
        }

        if (f3 < -speed)
        {
            f3 = -speed;
        }

        return currentAngle + f3;
    }
    
}
