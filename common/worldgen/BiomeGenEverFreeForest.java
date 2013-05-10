package loecraftpack.common.worldgen;

import java.util.Random;

import loecraftpack.LoECraftPack;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenEverFreeForest extends BiomeGenBase {

	public BiomeGenEverFreeForest(int par1) {
		super(par1);
		//this.spawnableCreatureList.add(new SpawnListEntry(EntityTimberWolf.class, 2, 4, 4));
		this.theBiomeDecorator = new BiomeDecoratorEverFree(this);
        this.theBiomeDecorator.treesPerChunk = 20;
        this.theBiomeDecorator.grassPerChunk = 20;
        this.theBiomeDecorator.mushroomsPerChunk = 8;
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        return (WorldGenerator)(random.nextInt(3) == 0 ?
    								this.worldGeneratorForest :
    								(random.nextInt(2) == 0 ?
    									this.worldGeneratorBigTree :
    		                			this.worldGeneratorTrees));
    }
}
