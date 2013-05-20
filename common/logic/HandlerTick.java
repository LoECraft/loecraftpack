package loecraftpack.common.logic;

import java.util.EnumSet;

import loecraftpack.LoECraftPack;
import loecraftpack.dimensionaltransfer.TeleporterCustom;
import loecraftpack.ponies.abilities.mechanics.MechanicHiddenOres;
import net.minecraft.client.Minecraft;
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
		if (type.contains(TickType.PLAYER))
		{
			if (tickData != null)
			{
				for (Object entry : tickData)
				{
			        if (entry instanceof EntityPlayer)
			        {
			        	EntityPlayer player = (EntityPlayer)entry;
			        	
			        	if (!player.worldObj.isRemote)//server
			        	{
				        	//check if player is fallen thru the bottom of skyland.
				        	if (player.worldObj==DimensionManager.getWorld(LoECraftPack.SkylandDimensionID) && player.posY<-10)
				        	{
				        		TeleporterCustom.refreshTeleporter(TeleporterCustom.teleporterSkyLandsFalling, 0);
								TeleporterCustom.teleporterSkyLandsFalling.travelToDimension(player);
				        	}
				        	else if (player.worldObj==DimensionManager.getWorld(0) && player.posY > player.worldObj.getHeight()+10)
				        	{
				        		TeleporterCustom.refreshTeleporter(TeleporterCustom.teleporterSkyLandsRising, LoECraftPack.SkylandDimensionID);
								TeleporterCustom.teleporterSkyLandsRising.travelToDimension(player);
				        	}
			        	}
			        	else //client
			        	{
			        		//clients player
			        		if (Minecraft.getMinecraft().thePlayer.entityId == player.entityId)
			        		{
				        		//hidden ore vision
				        		if (MechanicHiddenOres.revealHiddenGems)
					        	{
				        			
					        		if (player.isPotionActive(LoECraftPack.potionOreVision))
					        		{
					        			//update render?
					        			if (MechanicHiddenOres.bootUp)
					        			{
					        				MechanicHiddenOres.bootUp = false;
					        				MechanicHiddenOres.refreshRenderWithRange(player);
					        			}
					        			else if (MechanicHiddenOres.xPos != (int) player.posX ||
				        						 MechanicHiddenOres.yPos != (int) player.posY ||
				        						 MechanicHiddenOres.zPos != (int) player.posZ )
					        			{
					        				MechanicHiddenOres.refreshRenderWithRange(player);
					        			}
					        		}
					        		else
					        		{
					        			MechanicHiddenOres.bootUp = true;
					        			MechanicHiddenOres.revealHiddenGems = false;
					        			MechanicHiddenOres.refreshRenderWithRange(player);
					        		}
					        	}//hidden ore vision
			        		}//clients player
			        	}//client side
			        }//entry instanceof EntityPlayer
				}//Object entry : tickData
			}//tickData != null
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
