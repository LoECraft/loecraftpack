package loecraftpack.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.LoECraftPack;
import loecraftpack.common.worldgen.WorldGenCustomAppleTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;

public class BlockZapAppleSapling extends BlockAppleBloomSapling
{

	public BlockZapAppleSapling(int id)
    {
        super(id);
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
        	( new WorldGenCustomAppleTree(true, this, 
                                             LoECraftPack.blockZapAppleLog,
                                             LoECraftPack.blockZapAppleLeaves, 6 ) ).generate(world, random, xCoord, yCoord, zCoord);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon = iconRegister.registerIcon("loecraftpack:trees/sapling_zapapple");
	}
}