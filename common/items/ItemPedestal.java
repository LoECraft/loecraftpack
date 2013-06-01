package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import loecraftpack.common.entity.EntityPedestal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPedestal extends Item {

	public ItemPedestal(int par1) {
		super(par1);
		this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float par8, float par9, float par10)
    {
		if (!player.canPlayerEdit(xCoord, yCoord, zCoord, side, itemStack))
        {
            return false;
        }
		
		--itemStack.stackSize;
		
		if (world.isRemote)
        {
            return true;
        }
		
		EntityPedestal entity = new EntityPedestal(world, xCoord, yCoord, zCoord, side);
		world.spawnEntityInWorld(entity);
		return true;
    }
}
