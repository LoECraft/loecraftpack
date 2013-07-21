package loecraftpack.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public class EnchantmentFriendship extends Enchantment {
	
	public EnchantmentFriendship(int id, int weight)
    {
        super(id, weight, EnumEnchantmentType.weapon);
        this.setName("friendship");
    }
	
	/**
     * Returns the minimal value of enchant-ability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int par1)
    {
        return 5 + 5 * (par1 - 1);
    }

    /**
     * Returns the maximum value of enchant-ability needed on the enchantment level passed.
     */
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 30;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 6;
    }
    
    /**
     * Calculates the (magic) damage done by the enchantment on a living entity based on level and entity passed.
     */
    public int calcModifierLiving(int par1, EntityLiving par2EntityLiving)
    {
    	return -100;
    }
    
    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return !(enchantment instanceof EnchantmentDamage || enchantment instanceof EnchantmentFireAspect || enchantment instanceof EnchantmentBanish);
    }

}
