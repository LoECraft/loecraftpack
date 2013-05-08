package loecraftpack.common.worldgen;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;

public abstract class CustomBiomeGenLayer extends GenLayer {

	public CustomBiomeGenLayer(long par1, GenLayer par3GenLayer, WorldType worldType)
	{
		super(par1);
        this.parent = par3GenLayer;
	}
	
	

}
