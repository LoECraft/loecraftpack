package loecraftpack.common.items;

import loecraftpack.common.entity.EntityPhantomArrow;
import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

public class ItemRingPhantomArrow extends ItemRing {

	public ItemRingPhantomArrow(int par1) {
		super(par1);
		this.setMaxDamage(640);
	}
	
	public void onArrowLoose(ArrowLooseEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack)
	{
		System.out.println("Phantom");
		
		ItemStack par1ItemStack = event.bow;
		World par2World = player.worldObj;
		EntityPlayer par3EntityPlayer = event.entityPlayer;
		
		int j;
		
		j = event.charge;

        boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

        if (flag || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID))
        {
            float f = (float)j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if ((double)f < 0.1D)
            {
                return;
            }

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            EntityPhantomArrow entityarrow = new EntityPhantomArrow(par2World, par3EntityPlayer, f * 2.0F);
            System.out.println("ARROW L-"+entityarrow);
            

            if (f == 1.0F)
            {
                entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

            if (k > 0)
            {
                entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

            if (l > 0)
            {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
            {
                entityarrow.setFire(100);
            }

            par1ItemStack.damageItem(1, par3EntityPlayer);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag)
            {
                entityarrow.canBePickedUp = 2;
            }
            else
            {
            	//damage ring
            	itemStack.damageItem(1, player);
            }

            if (!par2World.isRemote)
            {
                par2World.spawnEntityInWorld(entityarrow);
            }
        }
	}

}
