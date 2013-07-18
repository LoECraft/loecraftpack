package loecraftpack.common.entity;

import loecraftpack.accessors.PrivateAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityElectricBlock extends Entity
{
	int maxAge;
	int age;
	
	@SuppressWarnings("unused")
	public EntityElectricBlock(World par1World) {
		super(par1World);
		this.setSize(1.2f, 1.2f);
		age = 0;
		maxAge = 10;//ticks & frames
		this.isImmuneToFire = true;
		if(false)
		{
			//TODO MAKE ASM WORK!!!
			//this.invulnerable = true;
		}
		else
		{
			PrivateAccessor.setPrivateVariable(Entity.class, this, "invulnerable", true);
		}
	}
	
	public int getAge()
	{
		return age;
	}
	
	public int getMaxAge()
	{
		return maxAge;
	}
	
	@Override
	public void onUpdate()
    {
        if (this.age++ >= this.maxAge)
        {
            this.setDead();
        }
    }
	
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}
	
	@Override
	public String getTexture()
    {
        return "/mods/loecraftpack/misc/electricSide.png";
    }
	
	public String getTextureSub()
    {
        return "/mods/loecraftpack/misc/electricBottom.png";
    }

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound){}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound){}
}
