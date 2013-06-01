package loecraftpack.common.entity;

import loecraftpack.LoECraftPack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPedestal extends Entity {
	
	public int xPosition;
    public int yPosition;
    public int zPosition;
    public double displayAngle;
    
    private float itemDropChance = 1.0F;
    
    public EntityPedestal(World par1World)
    {
        super(par1World);
        this.displayAngle = 0;
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
        this.setDirection(side);
        
        System.out.println("x:"+xPosition+" y:"+yPosition+" z:"+zPosition+" dir:"+side);
    }

	@Override
	protected void entityInit()
    {
        this.getDataWatcher().addObjectByDataType(2, 5);
    }
	
	@Override
	public String getTexture()
    {
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
	
	public void setDirection(int par1)
	{
		
	}
	
	public void onUpdate()
    {
		
    }
	
    public boolean canBeCollidedWith()
    {
        return true;
    }
    
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
    	if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();
                EntityPlayer entityplayer = null;

                if (par1DamageSource.getEntity() instanceof EntityPlayer)
                {
                    entityplayer = (EntityPlayer)par1DamageSource.getEntity();
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
        this.displayAngle = nbttagcompound.getDouble("Direction");
		
        //item code
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.hasNoTags())
        {
            this.setDisplayedItem(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
		nbttagcompound.setDouble("Direction", this.displayAngle);
        nbttagcompound.setInteger("TileX", this.xPosition);
        nbttagcompound.setInteger("TileY", this.yPosition);
        nbttagcompound.setInteger("TileZ", this.zPosition);
        
        //item code
        if (this.getDisplayedItem() != null)
        {
        	nbttagcompound.setCompoundTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
        }
	}
	
	
	/***
	 * item code
	 */
	
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
    
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (this.getDisplayedItem() == null)
        {
            ItemStack itemstack = par1EntityPlayer.getHeldItem();

            if (itemstack != null && !this.worldObj.isRemote)
            {
                this.setDisplayedItem(itemstack);

                if (!par1EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                }
            }
        }
        else if (!this.worldObj.isRemote)
        {
            //this.setItemRotation(this.getRotation() + 1);
        }

        return true;
    }
}
