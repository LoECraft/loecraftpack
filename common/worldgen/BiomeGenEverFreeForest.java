package loecraftpack.common.worldgen;

import java.util.List;
import java.util.Random;

import loecraftpack.common.entity.EntityTimberWolf;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenEverFreeForest extends BiomeGenBase {

	public BiomeGenEverFreeForest(int par1) {
		super(par1);
		this.spawnableMonsterList.remove(3);//the forest will be bad enough as is... it won't need creepers
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTimberWolf.class, 6, 4, 6));
		//this.spawnableMonsterList.add(new SpawnListEntry(EntityCockatrice.class, 3, 2, 4));
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
