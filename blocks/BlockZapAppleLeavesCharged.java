package loecraftpack.blocks;

import java.util.List;
import java.util.Random;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockZapAppleLeavesCharged extends BlockZapAppleLeaves
{
	private Icon icon[] = new Icon[4];
	//public int renderID;

	public BlockZapAppleLeavesCharged(int id) {
		super(id);
		this.setLightValue(0.5f);
		this.setUnlocalizedName("leavesZapCharged");
		this.appleType=1;
	}
	
	@Override
	public void dropBlockAsItemWithChance(World world, int xCoord, int yCoord, int zCoord, int par5, float par6, int par7)
    {
		System.out.println("DROP");
        if (!world.isRemote)
        {
            int j1 = 20;

            if ((par5 & 3) == 3)
            {
                j1 = 40;
            }

            if (par7 > 0)
            {
                j1 -= 2 << par7;

                if (j1 < 10)
                {
                    j1 = 10;
                }
            }

            if (world.rand.nextInt(j1) == 0)
            {
                int k1 = this.idDropped(par5, world.rand, par7);
                this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, new ItemStack(k1, 1, this.damageDropped(par5)));
            }

            j1 = 200;

            if (par7 > 0)
            {
                j1 -= 10 << par7;

                if (j1 < 40)
                {
                    j1 = 40;
                }
            }
            
            if (!sheared)
    		{
            	this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, new ItemStack(apple, 1, appleType));
    		}
            //bug fix
            sheared = false;
        }
    }
	
	@Override
	public void attemptGrow(World world, int xCoord, int yCoord, int zCoord, Random random){}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
		System.out.println("DESTROY");
		int meta = world.getBlockMetadata(x, y, z);
		if(!sheared)
		{
			System.out.println("REPLACE");
			return world.setBlock(x, y, z, LoECraftPack.blockZapAppleLeaves.blockID, meta&12, 2);
		}
        return world.setBlockToAir(x, y, z);
    }
	
	//TODO add spark effect, when charged
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random random)
    {
        if (world.canLightningStrikeAt(xCoord, yCoord + 1, zCoord) && !world.doesBlockHaveSolidTopSurface(xCoord, yCoord - 1, zCoord) && random.nextInt(15) == 1)
        {
            double d0 = (double)((float)xCoord + random.nextFloat());
            double d1 = (double)yCoord - 0.05D;
            double d2 = (double)((float)zCoord + random.nextFloat());
            world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon[0] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_charge");
		icon[1] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_charge_opaque");
	}
	/*
	@Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
    public int getRenderType()
    {
        return renderID;
    }
	*/
}
