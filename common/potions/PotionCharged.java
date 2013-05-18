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
	
	public void performEffect(EntityLiving entityLiving, int level)
    {
		int coordX = (int)(entityLiving.posX+0.5);
		int coordY = (int)(entityLiving.posY+0.5);
		int coordZ = (int)(entityLiving.posZ+0.5);
		World world =entityLiving.worldObj;
		if (world.rand.nextInt(50/(level+1))==0)
		{
			if(world.canLightningStrikeAt(coordX, coordY, coordZ))
			{
				world.addWeatherEffect(new EntityLightningBolt(world, entityLiving.posX, entityLiving.posY, entityLiving.posZ));
			}
			else
				entityLiving.attackEntityFrom(DamageSource.magic, 2 << level);
		}
    }

}
