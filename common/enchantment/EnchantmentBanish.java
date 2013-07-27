package loecraftpack.common.enchantment;

import loecraftpack.LoECraftPack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public class EnchantmentBanish extends Enchantment {
	
	public EnchantmentBanish(int id, int weight)
    {
        super(id, weight, EnumEnchantmentType.weapon);
        this.setName("banish");
    }
	
	/**
     * Returns the minimal value of enchant-ability needed on the enchantment level passed.
     */
	@Override
    public int getMinEnchantability(int par1)
    {
        return 10 + 10 * (par1 - 1);
    }

    /**
     * Returns the maximum value of enchant-ability needed on the enchantment level passed.
     */
	@Override
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
	@Override
    public int getMaxLevel()
    {
        return 3;
    }
    
    /**
     * Calculates the (magic) damage done by the enchantment on a living entity based on level and entity passed.
     */
	@Override
    public int calcModifierLiving(int par1, EntityLiving par2EntityLiving)
    {
    	if (par2EntityLiving.dimension==-1)//NETHER
    	{
    		int id = EntityList.getEntityID(par2EntityLiving);
	    	switch(id)
	    	{
	    	case 51://skeleton
	    	case 54://zombie
	    	case 58://enderman
	    	case 63://enderdragon
	    	case 64://witherboss
	    	case 100://timber wolf
	    		return par1;
	    	}
    	}
    	else if(par2EntityLiving.dimension==1)//THE END
    	{
    		int id = EntityList.getEntityID(par2EntityLiving);
	    	switch(id)
	    	{
	    	case 51://skeleton
	    	case 54://zombie
	    	case 56://ghast
	    	case 57://pigman
	    	case 64://witherboss
	    	case 100://timber wolf
	    		return par1;
	    	}
    	}
    	else
    	{
    		int id = EntityList.getEntityID(par2EntityLiving);
	    	switch(id)
	    	{
	    	case 51://skeleton
	    	case 54://zombie
	    	case 56://ghast
	    	case 57://pigman
	    	case 58://enderman
	    	case 63://enderdragon
	    	case 64://witherboos
	    	case 100://timber wolf
	    		return par1*2;
	    	}
    	}
    	
    	return 0;
    }
    
    
    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
	@Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return true;
    }


}
