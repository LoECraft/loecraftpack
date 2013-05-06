package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemZapAppleJam extends ItemFood {

	public ItemZapAppleJam(int id, int heal, float saturation, boolean wolf)
    {
        super(id, heal, saturation, wolf);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
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
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 6000, 1));
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 6000, 1));
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 6000, 0));
        	entityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 2));
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
	    iconIndex = iconRegister.registerIcon("loecraftpack:zapApple_jam");
	}

}
