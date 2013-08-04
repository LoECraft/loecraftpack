package loecraftpack.common.potions;

import loecraftpack.ponies.abilities.AbilityPlayerData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class PotionEnergy extends Potion {

	/*bad: FALSE*/
	public PotionEnergy(int potionID, boolean bad, int color) {
		super(potionID, bad, color);
	}
	
	public void performEffect(EntityLiving entityLiving, int level)
	{
		System.out.println("Surge");
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;
			AbilityPlayerData abilityData = AbilityPlayerData.Get(player.username);
			
			if (player.worldObj.isRemote)
			{
				abilityData.restoreOrDrainEnergyWithOffset(100*level);
			}
			else
				abilityData.addEnergy(100*level);
		}
	}
	
	@Override
	public boolean isInstant()
    {
        return true;
    }
	
	@Override
	public boolean isUsable()
	{
		return true;
	}
	
	@Override
	public boolean isReady(int par1, int par2)
    {
        return par1 >= 20;
    }
}
