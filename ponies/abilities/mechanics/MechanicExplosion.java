package loecraftpack.ponies.abilities.mechanics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class MechanicExplosion extends Explosion
{
	public boolean explodeParticles = true; 
	private float damage = 1;
	private int field_77289_h = 16;
    private Random explosionRNG = new Random();
    private World worldObj;
    private Map field_77288_k = new HashMap();
	
	public MechanicExplosion(World world, float size, float damage, double x, double y, double z)
	{
		super(world, (Entity)null, x, y, z, size);
		this.worldObj = world;
		this.damage = damage;
	}

	public void DestroyBlocks(boolean destroyBlocks, boolean dropBlocks, float chanceToDrop)
    {
		float f = this.explosionSize;
        HashSet hashset = new HashSet();
        int i;
        int j;
        int k;
        double d0;
        double d1;
        double d2;

        for (i = 0; i < this.field_77289_h; ++i)
        {
            for (j = 0; j < this.field_77289_h; ++j)
            {
                for (k = 0; k < this.field_77289_h; ++k)
                {
                    if (i == 0 || i == this.field_77289_h - 1 || j == 0 || j == this.field_77289_h - 1 || k == 0 || k == this.field_77289_h - 1)
                    {
                        double d3 = (double)((float)i / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                        double d4 = (double)((float)j / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                        double d5 = (double)((float)k / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        d0 = this.explosionX;
                        d1 = this.explosionY;
                        d2 = this.explosionZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F)
                        {
                            int l = MathHelper.floor_double(d0);
                            int i1 = MathHelper.floor_double(d1);
                            int j1 = MathHelper.floor_double(d2);
                            int k1 = this.worldObj.getBlockId(l, i1, j1);

                            if (k1 > 0)
                            {
                                Block block = Block.blocksList[k1];
                                float f3 = this.exploder != null ? this.exploder.func_82146_a(this, this.worldObj, l, i1, j1, block) : block.getExplosionResistance(this.exploder, worldObj, l, i1, j1, explosionX, explosionY, explosionZ);
                                f1 -= (f3 + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && (this.exploder == null || this.exploder.func_96091_a(this, this.worldObj, l, i1, j1, k1, f1)))
                            {
                                hashset.add(new ChunkPosition(l, i1, j1));
                            }

                            d0 += d3 * (double)f2;
                            d1 += d4 * (double)f2;
                            d2 += d5 * (double)f2;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(hashset);
        
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.explodeParticles)
        {
	        if (this.explosionSize >= 2.0F)
	        {
	            this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
	        }
	        else
	        {
	            this.worldObj.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
	        }
        }

        Iterator iterator;
        ChunkPosition chunkposition;
        int l;

        iterator = this.affectedBlockPositions.iterator();

        while (iterator.hasNext())
        {
            chunkposition = (ChunkPosition)iterator.next();
            i = chunkposition.x;
            j = chunkposition.y;
            k = chunkposition.z;
            l = this.worldObj.getBlockId(i, j, k);

            d0 = (double)((float)i + this.worldObj.rand.nextFloat());
            d1 = (double)((float)j + this.worldObj.rand.nextFloat());
            d2 = (double)((float)k + this.worldObj.rand.nextFloat());
            double d3 = d0 - this.explosionX;
            double d4 = d1 - this.explosionY;
            double d5 = d2 - this.explosionZ;
            double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
            d3 /= d6;
            d4 /= d6;
            d5 /= d6;
            double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
            d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
            d3 *= d7;
            d4 *= d7;
            d5 *= d7;
            if (explodeParticles)
            	this.worldObj.spawnParticle("explode", (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
            if (isSmoking)
            	this.worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);

            if (l > 0)
            {
                Block block = Block.blocksList[l];

                if (block.canDropFromExplosion(this) && dropBlocks)
                {
                    block.dropBlockAsItemWithChance(this.worldObj, i, j, k, this.worldObj.getBlockMetadata(i, j, k), chanceToDrop, 0);
                }
                if (destroyBlocks)
                {
	                this.worldObj.setBlock(i, j, k, 0, 0, 3);
	                block.onBlockDestroyedByExplosion(this.worldObj, i, j, k, this);
                }
            }
        }

        if (this.isFlaming)
        {
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext())
            {
                chunkposition = (ChunkPosition)iterator.next();
                i = chunkposition.x;
                j = chunkposition.y;
                k = chunkposition.z;
                l = this.worldObj.getBlockId(i, j, k);
                int i1 = this.worldObj.getBlockId(i, j - 1, k);

                if (l == 0 && Block.opaqueCubeLookup[i1] && this.explosionRNG.nextInt(3) == 0)
                {
                    this.worldObj.setBlock(i, j, k, Block.fire.blockID);
                }
            }
        }
    }
	
	public void DamageEntities(boolean doubleExplosionSize, float force, boolean mobFire)
	{
		double d0 = this.explosionX;
        double d1 = this.explosionY;
        double d2 = this.explosionZ;
		
        float f = this.explosionSize;
		if (doubleExplosionSize)
			this.explosionSize *= 2.0F;
		
        int i = MathHelper.floor_double(this.explosionX - (double)this.explosionSize - 1.0D);
        int j = MathHelper.floor_double(this.explosionX + (double)this.explosionSize + 1.0D);
        int k = MathHelper.floor_double(this.explosionY - (double)this.explosionSize - 1.0D);
        int l1 = MathHelper.floor_double(this.explosionY + (double)this.explosionSize + 1.0D);
        int i2 = MathHelper.floor_double(this.explosionZ - (double)this.explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(this.explosionZ + (double)this.explosionSize + 1.0D);
        
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getAABBPool().getAABB((double)i, (double)k, (double)i2, (double)j, (double)l1, (double)j2));
        Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.explosionX, this.explosionY, this.explosionZ);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = (Entity)list.get(k2);
            double d7 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.explosionSize;

            if (d7 <= 1.0D)
            {
                d0 = entity.posX - this.explosionX;
                d1 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
                d2 = entity.posZ - this.explosionZ;
                double d8 = (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);

                if (d8 != 0.0D)
                {
                    d0 /= d8;
                    d1 /= d8;
                    d2 /= d8;
                    double d9 = (double)this.worldObj.getBlockDensity(vec3, entity.boundingBox);
                    double d10 = (1.0D - d7) * d9;
                    entity.attackEntityFrom(DamageSource.setExplosionSource(this), (int)((d10 * d10 + d10) / 2.0D * 8.0D * (double)this.damage + 1.0D));
                    double d11 = EnchantmentProtection.func_92092_a(entity, d10) * force;
                    entity.motionX += d0 * d11;
                    entity.motionY += Math.abs(d1 * d11);
                    entity.motionZ += d2 * d11;

                    if (entity instanceof EntityPlayer)
                    {
                        this.field_77288_k.put((EntityPlayer)entity, this.worldObj.getWorldVec3Pool().getVecFromPool(d0 * d10, d1 * d10, d2 * d10));
                    }
                    
                    if (mobFire)
                    	entity.setFire((int)(Math.min(1, 1.25d-d7) * damage + 3));
                }
            }
        }

        this.explosionSize = f;
	}
	
	public static void Explode(World world, double x, double y, double z, float size, boolean flamey, boolean dropBlocks, float chanceToDrop)
	{
		ExplodeCustomDamage(world, x, y, z, size, size, false, flamey, true, true, true, dropBlocks, true, 1, chanceToDrop);
	}
	
	public static void AOEDamage(World world, double x, double y, double z, float size, float force , float damage, boolean flamey)
	{
		ExplodeCustomDamage(world, x, y, z, size, damage, true, flamey, false, true, false, false, false, force, 0);
	}
	
	public static void ExplodeCustomDamage(World world, double x, double y, double z, float size, float damage, boolean mobFlame, boolean flamey, boolean smokey, boolean explodey, boolean destroyBlocks, boolean dropBlocks, boolean doubleDamageRadius, float force, float chanceToDrop)
	{
        MechanicExplosion explosion = new MechanicExplosion(world, size, damage, x, y, z);
        explosion.isFlaming = flamey;
        explosion.isSmoking = smokey;
        explosion.explodeParticles = explodey;
        explosion.DestroyBlocks(destroyBlocks, dropBlocks, chanceToDrop);
        explosion.DamageEntities(doubleDamageRadius, force, mobFlame);
	}
}
