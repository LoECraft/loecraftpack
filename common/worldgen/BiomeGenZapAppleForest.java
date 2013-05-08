package loecraftpack.common.worldgen;

import java.util.Random;

import loecraftpack.LoECraftPack;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenZapAppleForest extends BiomeGenBase {

	public BiomeGenZapAppleForest(int par1) {
		super(par1);
		//this.spawnableCreatureList.add(new SpawnListEntry(EntityTimberWolf.class, 2, 4, 4));
        this.theBiomeDecorator.treesPerChunk = 20;
        this.theBiomeDecorator.grassPerChunk = 4;
	}
	
	public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
    {
        return (WorldGenerator)LoECraftPack.worldGeneratorZapAppleForest;
    }

}
