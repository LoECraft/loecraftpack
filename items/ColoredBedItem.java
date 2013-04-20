package loecraftpack.items;

import loecraftpack.LoECraftPack;
import loecraftpack.blocks.ColoredBedBlock;
import loecraftpack.enums.Dye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ColoredBedItem extends Item
{
	//test
	//Associated Block
	public ColoredBedBlock block[] = new ColoredBedBlock[ColoredBedBlock.bedtypes];
	
    public ColoredBedItem(int par1)
    {
        super(par1);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (par7 != 1)
        {
            return false;
        }
        else
        {
            ++yCoord;
            int i1 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;

            if (i1 == 0)
            {
                b1 = 1;
            }

            if (i1 == 1)
            {
                b0 = -1;
            }

            if (i1 == 2)
            {
                b1 = -1;
            }

            if (i1 == 3)
            {
                b0 = 1;
            }

            if (player.canPlayerEdit(xCoord, yCoord, zCoord, par7, itemStack) && player.canPlayerEdit(xCoord + b0, yCoord, zCoord + b1, par7, itemStack))
            {
                if (world.isAirBlock(xCoord, yCoord, zCoord) && world.isAirBlock(xCoord + b0, yCoord, zCoord + b1) && world.doesBlockHaveSolidTopSurface(xCoord, yCoord - 1, zCoord) && world.doesBlockHaveSolidTopSurface(xCoord + b0, yCoord - 1, zCoord + b1))
                {
                    world.setBlock(xCoord, yCoord, zCoord, block[itemStack.getItemDamage()].blockID, i1, 3);

                    if (world.getBlockId(xCoord, yCoord, zCoord) == block[itemStack.getItemDamage()].blockID)
                    {
                        world.setBlock(xCoord + b0, yCoord, zCoord + b1, block[itemStack.getItemDamage()].blockID, i1 + 8, 3);
                    }

                    --itemStack.stackSize;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
