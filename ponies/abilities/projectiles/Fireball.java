package loecraftpack.ponies.abilities.projectiles;

import loecraftpack.ponies.abilities.mechanics.MechanicExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class Fireball extends EntityLargeFireball
{
	public Fireball(World world, EntityLiving player, double xMotion, double yMotion, double zMotion)
	{
		super(world, player, xMotion, yMotion, zMotion);
		this.accelerationX = xMotion;
        this.accelerationY = yMotion;
        this.accelerationZ = zMotion;
        this.posY ++;
	}
	
	@Override
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (!this.worldObj.isRemote)
        {
            if (par1MovingObjectPosition.entityHit != null)
            {
                par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6);
            }

            MechanicExplosion.AOEDamage(this.worldObj, this.posX, this.posY, this.posZ, 3, 0, 1, false);
            this.setDead();
        }
    }
}
