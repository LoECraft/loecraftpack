package loecraftpack.common.logic;

import java.util.EnumSet;

import loecraftpack.LoECraftPack;
import loecraftpack.dimensionaltransfer.TeleporterCustom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.SingleIntervalHandler;
import cpw.mods.fml.common.TickType;

public class HandlerTick implements ITickHandler {
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		System.out.println("tick S");
		
	}
	
	@Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
		System.out.println("tick E");
        if (tickData != null && tickData[0] != null && tickData[0] instanceof EntityPlayer)
        {
        	
        	EntityPlayer player = (EntityPlayer)tickData[0];
        	if(player.worldObj==DimensionManager.getWorld(8) && player.posY<-20)
        	{
        		TeleporterCustom.varifyTeleporter(LoECraftPack.teleporterSkyLandsFalling, 0);
				LoECraftPack.teleporterSkyLandsFalling.travelToDimension(player);
        	}
        }
    }
	
	@Override
	public EnumSet<TickType> ticks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
