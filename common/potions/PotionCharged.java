package loecraftpack.common.potions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/** 
 * This effect causes players to randomly take tiny damage, unless it's raining and they are outside; 
 * to which they randomly get hit by lightning
 */
public class PotionCharged extends Potion {

	/*bad: TRUE*/
	public PotionCharged(int potionID, boolean bad, int color) {
		super(potionID, bad, color);
		
	}
	
	@Override
	public void performEffect(EntityLiving entityLiving, int level)
    {
		int coordX = (int)(entityLiving.posX+0.5);
		int coordY = (int)(entityLiving.posY+0.5);
		int coordZ = (int)(entityLiving.posZ+0.5);
		World world = entityLiving.worldObj;
		if (!world.isRemote && world.rand.nextInt(20/(level+1)) == 0)
			world.addWeatherEffect(new EntityLightningBolt(world, entityLiving.posX, entityLiving.posY, entityLiving.posZ));
    }
	
	@Override
	public boolean isReady(int par1, int par2)
    {
        int k;
        
        k = 40 >> par2;
        return k > 0 ? par1 % k == 0 : true;
    }

}
