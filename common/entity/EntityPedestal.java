package loecraftpack.common.entity;

import loecraftpack.LoECraftPack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPedestal extends Entity {
	
	public int xPosition;
    public int yPosition;
    public int zPosition;
    
    public String name = null;
    
    
    private float itemDropChance = 1.0F;
    
    public EntityPedestal(World par1World)
    {
        super(par1World);
        this.yOffset = 0.0F;
        this.setSize(0.75F, 0.8125F); // 12 x 13 pixils
    }
    
    public EntityPedestal(World par1World, int xPos, int yPos, int zPos, int side)
    {
        this(par1World);
        this.xPosition = xPos;
        this.yPosition = yPos;
        this.zPosition = zPos;
        this.setPositionAdjacent(xPosition, yPosition, zPosition, side);
        //this.setDirection(side);
        
        System.out.println("x:"+xPosition+" y:"+yPosition+" z:"+zPosition+" dir:"+side);
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
        //display angle
        this.getDataWatcher().addObject(5, Integer.valueOf((byte)0));
        //display angle sub
        this.getDataWatcher().addObject(6, Integer.valueOf((byte)0));
    }
	
	@Override
	public String getTexture()
    {
		//TODO make this return based on a variable;
        return "/mods/loecraftpack/textures/blocks/decor/pedestal.png";
    }
	
	public void setPositionAdjacent(int xPos, int yPos, int zPos, int side)
	{
		switch(side)
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
		
		
		switch(getDisplayMode())
		{
		case 1://rotate slowly
			setDisplayAngle( getDisplayAngle() + 90/(20 * 4) );
			break;
		case 2://track players
			
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
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            this.setDead();
            this.dropItemStack();
        }
    }
    
    public void addVelocity(double par1, double par3, double par5)
    {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            this.setDead();
            this.dropItemStack();
        }
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) 
	{
		this.xPosition = nbttagcompound.getInteger("TileX");
        this.yPosition = nbttagcompound.getInteger("TileY");
        this.zPosition = nbttagcompound.getInteger("TileZ");
		
        //item code
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.hasNoTags())
        {
            this.setDisplayedItem(ItemStack.loadItemStackFromNBT(nbttagcompound1));
            this.setDisplayMode(nbttagcompound.getByte("Mode"));
            int angle = nbttagcompound.getInteger("Direction");
            this.setDefaultAngle(angle);
            this.setDisplayAngle(angle);
            this.name = nbttagcompound.getString("SkullName");
        }
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
        nbttagcompound.setInteger("TileX", this.xPosition);
        nbttagcompound.setInteger("TileY", this.yPosition);
        nbttagcompound.setInteger("TileZ", this.zPosition);
        
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
	
	
	/*********************/
	/***** item code *****/
	/*********************/
	
	
	
	public boolean isInRangeToRenderDist(double par1)
    {
        double d1 = 16.0D;
        d1 *= 64.0D * this.renderDistanceWeight;
        return par1 < d1 * d1;
    }
	
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

    public void setDisplayedItem(ItemStack par1ItemStack)
    {
        par1ItemStack = par1ItemStack.copy();
        par1ItemStack.stackSize = 1;
        //par1ItemStack.setItemFrame(this);
        this.getDataWatcher().updateObject(2, par1ItemStack);
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
		return (double)this.getDataWatcher().getWatchableObjectInt(5)/100d;
	}
	
	public void setDisplayAngle(double par1)
    {
        this.getDataWatcher().updateObject(5, Integer.valueOf((int)(par1*100)));
    }
	
	public double getDisplayAngleSub()
	{
		return (double)this.getDataWatcher().getWatchableObjectInt(6)/100d;
	}
	
	public void setDisplayAngleSub(double par1)
    {
        this.getDataWatcher().updateObject(6, Integer.valueOf((int)(par1*100)));
    }

    
    
    public boolean interact(EntityPlayer player)
    {
        if (this.getDisplayedItem() == null)
        {
            ItemStack itemstack = player.getHeldItem();

            if (itemstack != null && !this.worldObj.isRemote)
            {
                this.setDisplayedItem(itemstack);
                
                //used for when playerskulls are placed
                if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("SkullOwner"))
                {
                    name = itemstack.getTagCompound().getString("SkullOwner");
                }
                
                if (!player.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                }
            }
        }
        else if (!this.worldObj.isRemote)
        {
        	//change mode
        	if (getDisplayMode()<2)
        		setDisplayMode(getDisplayMode()+1);
        	else
        		setDisplayMode(0);
        	
        	//set default angle
        	setDefaultAngle(180-(int)player.rotationYaw);
        	
        	//reset active position
        	setDisplayAngle(getDefaultAngle());
        }

        return true;
    }
}
