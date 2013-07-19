package loecraftpack.common.worldgen;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DimensionSkyland extends WorldProvider
{
	private float[] colorsSunriseSunset = new float[4];
	
	public static WorldProvider getProviderForDimension(int par0)
    {
        return DimensionManager.createProviderFor(8);
    }
	
	@Override
	public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderSkyland(worldObj, worldObj.getSeed());
    }
	
	@Override
	public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerSkyland(BiomeGenBase.sky);
    }
	
	@Override
	public String getDimensionName()
	{
		return "Skyland";
	}
	
	@Override
	public boolean canRespawnHere()
    {
        return false;
    }
	
	@Override
	public float getCloudHeight()
    {
        return 8.0F;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean getWorldHasVoidParticles()
    {
        return false;
    }
	
	@Override
	public String getSaveFolder()
    {
        return "DIM_SKYLAND";
    }
	
	@Override
	public String getWelcomeMessage()
    {
		return "Welcome to the Skylands!";
    }
	
	@Override
	public String getDepartMessage()
    {
        return "Falling out of the Sky...";
    }
	
	@Override
	public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        super.setAllowedSpawnTypes(allowHostile, false);
    }
	
	@Override
	public void updateWeather()
    {
		//Do: DimensionSkyland - SYNC WEATHER WITH OVERWORLD
        worldObj.updateWeatherBody();
    }
}
