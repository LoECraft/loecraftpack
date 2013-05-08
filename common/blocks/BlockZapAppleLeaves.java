package loecraftpack.common.blocks;

import java.util.Random;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*TODO
make electric cloud entity
BIOME:
	add Everfree forest
	add Biome generation code
		-zap apple trees
*/
public class BlockZapAppleLeaves extends BlockAppleBloomLeaves
{
	
	public BlockZapAppleLeaves(int id)
    {
        super(id);
        this.setTickRandomly(true);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.setUnlocalizedName("leavesZap");
        this.apple = LoECraftPack.itemZapApple;
        saplingDropRate = 600;//roughly 60 leaves on a tree
    }
	
	//no color change
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int par1)
    {
        return 16777215;
    }
	
	//no color change
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 16777215;
    }
	
	@Override
	public void attemptGrow(World world, int xCoord, int yCoord, int zCoord, Random random)
	{
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		if ((meta&4) == 0)
        {
        	//chance to grow
        	if (random.nextInt(30) == 0)
        	{
        		if (meta == 3)
        			zapGrow(world, xCoord, yCoord, zCoord);
        		else
        		{
        			if (world.setBlock(xCoord, yCoord, zCoord, this.blockID, meta + 1, 2))
        				tellClientOfChange(world, xCoord, yCoord, zCoord, this.blockID);
        		}
        	}
        }
	}
	
	public void zapGrow(World world, int xCoord, int yCoord, int zCoord)
	{
		if (!world.isRaining())return;
		
		int idTemp;

		world.addWeatherEffect(new EntityLightningBolt(world, xCoord, yCoord, zCoord));
		if (world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID, 0, 2))
			tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID);
		
		for (int xMod = -3; xMod <4; xMod++)
		{
			for (int yMod = -3; yMod <4; yMod++)
			{
				for (int zMod = -3; zMod <4; zMod++)
				{
					if (world.blockExists(xCoord+xMod, yCoord+yMod, zCoord+zMod) 
							&& world.getBlockId(xCoord+xMod, yCoord+yMod, zCoord+zMod) == LoECraftPack.blockZapAppleLeaves.blockID)
					{
						int meta = world.getBlockMetadata(xCoord+xMod, yCoord+yMod, zCoord+zMod);
						if ((meta&3) >= bloomStage && (meta & 4) == 0)
						{
							if (world.setBlock(xCoord+xMod, yCoord+yMod, zCoord+zMod, LoECraftPack.blockZapAppleLeavesCharged.blockID, 0, 2))
								tellClientOfChange(world, xCoord+xMod, yCoord+yMod, zCoord+zMod, LoECraftPack.blockZapAppleLeavesCharged.blockID);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void dropAppleThruTree(World world, int xCoord, int yCoord, int zCoord, ItemStack itemStack)
	{
		dropAppleThruTree(world, xCoord, yCoord, zCoord, itemStack,
				new int[]{LoECraftPack.blockZapAppleLog.blockID,
				          LoECraftPack.blockZapAppleLeaves.blockID,
				          LoECraftPack.blockZapAppleLeavesCharged.blockID});
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return LoECraftPack.blockZapAppleSapling.blockID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon[0] = iconRegister.registerIcon("loecraftpack:trees/leaves_zapapple");
		icon[1] = iconRegister.registerIcon("loecraftpack:trees/leaves_zapapple_opaque");
		icon[2] = iconRegister.registerIcon("loecraftpack:trees/leaves_zapapple_bloom");
		icon[3] = iconRegister.registerIcon("loecraftpack:trees/leaves_zapapple_bloom_opaque");
	}
}
