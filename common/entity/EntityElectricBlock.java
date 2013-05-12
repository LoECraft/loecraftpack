package loecraftpack.common.entity;

import loecraftpack.common.logic.PrivateAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityElectricBlock extends Entity
{
	int maxAge;
	int age;
	
	public EntityElectricBlock(World par1World) {
		super(par1World);
		this.setSize(1.2f, 1.2f);
		age = 0;
		maxAge = 10;//ticks
		this.isImmuneToFire = true;
		PrivateAccessor.setPrivateVariable(Entity.class, this, "invulnerable", true);
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
        return "/mods/loecraftpack/misc/static.png";
    }

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		System.out.println("back from the dead!!!!");
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		
	}

}
