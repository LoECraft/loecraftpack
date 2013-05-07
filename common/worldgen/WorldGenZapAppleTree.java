package loecraftpack.common.worldgen;

import java.util.Random;

import loecraftpack.common.blocks.BlockZapAppleSapling;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ForgeDirection;

public class WorldGenZapAppleTree extends WorldGenerator {

	Block saplingType;
	Block logType;
	Block leafType;
	int minTreeHeight;
	
	public WorldGenZapAppleTree(boolean sapling, Block saplingType, Block log, Block leaf, int minTreeHeight) {
		super(sapling);
		this.saplingType = saplingType;
		this.logType = log;
		this.leafType = leaf;
		this.minTreeHeight = minTreeHeight;
	}
	
	@Override
    public boolean generate(World par1World, Random par2Random, int xCoord, int yCoord, int zCoord)
    {
        int l = par2Random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (yCoord >= 1 && yCoord + l + 1 <= 256)
        {
            int i1;
            byte b0;
            int j1;
            int k1;

            for (i1 = yCoord; i1 <= yCoord + 1 + l; ++i1)
            {
                b0 = 1;
                
                if (i1 == yCoord)
                {
                    b0 = 0;
                }

                if (i1 >= yCoord + 1 + l - 2)
                {
                    b0 = 2;
                }

                for (int l1 = xCoord - b0; l1 <= xCoord + b0 && flag; ++l1)
                {
                    for (j1 = zCoord - b0; j1 <= zCoord + b0 && flag; ++j1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            k1 = par1World.getBlockId(l1, i1, j1);
                            Block block = Block.blocksList[k1];
                            
                            if (k1 != saplingType.blockID &&
                            	k1 != 0 &&
                                !block.isLeaves(par1World, l1, i1, j1) &&
                                k1 != Block.grass.blockID &&
                                k1 != Block.dirt.blockID &&
                                !block.isWood(par1World, l1, i1, j1) )
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }
            if (!flag)
            {
                return false;
            }
            else
            {
                i1 = par1World.getBlockId(xCoord, yCoord - 1, zCoord);
                Block soil = Block.blocksList[i1];
                boolean isSoil = (soil != null && soil.canSustainPlant(par1World, xCoord, yCoord - 1, zCoord, ForgeDirection.UP, (BlockZapAppleSapling)saplingType ));
                System.out.println("SOIL: "+ isSoil);
                if (isSoil && yCoord < 256 - l - 1)
                {
                    soil.onPlantGrow(par1World, xCoord, yCoord - 1, zCoord, xCoord, yCoord, zCoord);
                    b0 = 3;
                    byte b1 = 0;
                    int i2;
                    int j2;
                    int k2;

                    for (j1 = yCoord - b0 + l; j1 <= yCoord + l; ++j1)
                    {
                        k1 = j1 - (yCoord + l);
                        i2 = b1 + 1 - k1 / 2;

                        for (j2 = xCoord - i2; j2 <= xCoord + i2; ++j2)
                        {
                            k2 = j2 - xCoord;

                            for (int l2 = zCoord - i2; l2 <= zCoord + i2; ++l2)
                            {
                                int i3 = l2 - zCoord;

                                if (Math.abs(k2) != i2 || Math.abs(i3) != i2 || par2Random.nextInt(2) != 0 && k1 != 0)
                                {
                                    int j3 = par1World.getBlockId(j2, j1, l2);
                                    Block block = Block.blocksList[j3];

                                    if (block == null || block.canBeReplacedByLeaves(par1World, j2, j1, l2))
                                    {
                                        this.setBlockAndMetadata(par1World, j2, j1, l2, leafType.blockID, 0);
                                    }
                                }
                            }
                        }
                    }

                    for (j1 = 0; j1 < l; ++j1)
                    {
                        k1 = par1World.getBlockId(xCoord, yCoord + j1, zCoord);

                        Block block = Block.blocksList[k1];

                        if (k1 == 0 || block == null || block.isLeaves(par1World, xCoord, yCoord + j1, zCoord) || k1 == saplingType.blockID )
                        {
                            this.setBlockAndMetadata(par1World, xCoord, yCoord + j1, zCoord, logType.blockID, 0);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

}
