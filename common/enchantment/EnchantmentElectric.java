package loecraftpack.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public class EnchantmentElectric extends Enchantment {

	public EnchantmentElectric(int id, int weight)
    {
        super(id, weight, EnumEnchantmentType.weapon);
        this.setName("electric");
    }
	
	/**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int par1)
    {
        return 15 + 20 * (par1 - 1);
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 2;
    }
    
    /**
     * Calculates de (magic) damage done by the enchantment on a living entity based on level and entity passed.
     */
    public int calcModifierLiving(int par1, EntityLiving par2EntityLiving)
    {
    	if (EntityList.getEntityID(par2EntityLiving) == 50 /*Creeper*/)
    		return 0;
    	else
    		return par1*2;
    }
}
