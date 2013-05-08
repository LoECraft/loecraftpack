package loecraftpack.common.worldgen;

import loecraftpack.LoECraftPack;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddZapApples extends GenCustomBiomeLayer
{
    public GenLayerAddZapApples(long par1, GenLayer par3GenLayer, WorldType worldType)
    {
        super(par1, par3GenLayer ,worldType);
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i2 = 0; i2 < par4; ++i2)
        {
            for (int j2 = 0; j2 < par3; ++j2)
            {
                int k2 = aint[j2 + 0 + (i2 + 0) * k1];
                int l2 = aint[j2 + 2 + (i2 + 0) * k1];
                int i3 = aint[j2 + 0 + (i2 + 2) * k1];
                int j3 = aint[j2 + 2 + (i2 + 2) * k1];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];
                this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));

                if ( valid(k3) && valid(k2) && valid(l2) && valid(i3) && valid(j3) && this.nextInt(1) == 0)
                {
                    aint1[j2 + i2 * par3] = LoECraftPack.biomeGeneratorZapAppleForest.biomeID;
                }
                else
                {
                    aint1[j2 + i2 * par3] = k3;
                }
            }
        }

        return aint1;
    }
    
    private boolean valid(int id)
    {
    	return (id == LoECraftPack.biomeGeneratorEverFreeForest.biomeID || id == LoECraftPack.biomeGeneratorZapAppleForest.biomeID);
    }
}
