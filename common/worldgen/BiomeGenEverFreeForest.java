package loecraftpack.common.worldgen;

import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenEverFreeForest extends BiomeGenBase {

	public BiomeGenEverFreeForest(int par1) {
		super(par1);
		//this.spawnableCreatureList.add(new SpawnListEntry(EntityTimberWolf.class, 2, 4, 4));
        this.theBiomeDecorator.treesPerChunk = 20;
        this.theBiomeDecorator.grassPerChunk = 20;
	}
	
	public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
    {
        return (WorldGenerator)(par1Random.nextInt(3) == 0 ?
    								this.worldGeneratorForest :
    								(par1Random.nextInt(2) == 0 ?
    									this.worldGeneratorBigTree :
    		                			this.worldGeneratorTrees));
    }

}
