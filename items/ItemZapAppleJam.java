package loecraftpack.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemZapAppleJam extends ItemFood {

	public ItemZapAppleJam(int id, int heal, float saturation, boolean wolf)
    {
        super(id, heal, saturation, wolf);
    }
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack)
    {
        return true;
    }
	
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
		if (!world.isRemote)
        {
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1000, 3));
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1000, 3));
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 3));
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 6000, 0));
        }
    }

}
