package loecraftpack.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public abstract class ItemFoodWithSubTypes extends ItemFood {
	
	protected int[] potionId;
    protected int[] potionDuration;
    protected int[] potionAmplifier;
    protected float[] potionEffectProbability;

	public ItemFoodWithSubTypes(int id, int heal, float saturation, boolean wolf) {
		super(id, heal, saturation, wolf);
	}
	
	public ItemFoodWithSubTypes(int id, int heal, boolean wolf) {
		super(id, heal, wolf);
	}
    
    @Override
	protected void onFoodEaten(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	int i = itemStack.getItemDamage();
        if (!par2World.isRemote && this.potionId[i] > 0 && par2World.rand.nextFloat() < this.potionEffectProbability[i])
        {
            par3EntityPlayer.addPotionEffect(new PotionEffect(this.potionId[i], this.potionDuration[i] * 20, this.potionAmplifier[i]));
        }
    }

}
