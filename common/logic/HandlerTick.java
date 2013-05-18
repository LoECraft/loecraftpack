package loecraftpack.common.logic;

import java.util.EnumSet;

import loecraftpack.LoECraftPack;
import loecraftpack.dimensionaltransfer.TeleporterCustom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class HandlerTick implements ITickHandler {
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}
	
	@Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
		if (tickData != null)
		{
			for(Object entry : tickData)
			{
		        if(entry instanceof EntityPlayer)
		        {
		        	EntityPlayer player = (EntityPlayer)entry;
		        	
		        	//check if player is fallen thru the bottom of skyland.
		        	if(player.worldObj==DimensionManager.getWorld(LoECraftPack.SkylandDimensionID) && player.posY<-20)
		        	{
		        		TeleporterCustom.refreshTeleporter(LoECraftPack.teleporterSkyLandsFalling, 0);
						LoECraftPack.teleporterSkyLandsFalling.travelToDimension(player);
		        	}
		        }
			}
		}
    }
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "LoeCraftPackTickHandler";
	}

}
