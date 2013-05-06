package loecraftpack.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.LoECraftPack;
import loecraftpack.common.worldgen.WorldGenZapAppleTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;

public class BlockZapAppleSapling extends BlockFlower{
	
	private Icon icon; 

	public BlockZapAppleSapling(int id)
    {
        super(id);
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
    }
	
	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random)
    {
        if (!world.isRemote)
        {
            super.updateTick(world, xCoord, yCoord, zCoord, random);

            if (world.getBlockLightValue(xCoord, yCoord + 1, zCoord) >= 9 && random.nextInt(7) == 0)
            {
                this.grow(world, xCoord, yCoord, zCoord, random);
            }
        }
    }
	
	@SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return this.icon;
    }
	
	public void grow(World world, int xCoord, int yCoord, int zCoord, Random random)
    {
        int l = world.getBlockMetadata(xCoord, yCoord, zCoord);

        if ((l & 8) == 0)
        {
            world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, l | 8, 4);
        }
        else
        {
        	( new WorldGenZapAppleTree(true, LoECraftPack.blockZapAppleSapling, 
                                             LoECraftPack.blockZapAppleLog,
                                             LoECraftPack.blockZapAppleLeaves, 6 ) ).generate(world, random, xCoord, yCoord, zCoord);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon = iconRegister.registerIcon("loecraftpack:sapling_zapapple");
	}
}