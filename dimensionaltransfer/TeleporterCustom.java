package loecraftpack.dimensionaltransfer;

import java.util.Random;

import loecraftpack.common.logic.PrivateAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterCustom extends Teleporter {

	protected final WorldServer worldServerInstance2;
	protected final int dimensionID;
	
	public TeleporterCustom(WorldServer par1WorldServer, int dimensionID) {
		super(par1WorldServer);
		worldServerInstance2 = (WorldServer)PrivateAccessor.getPrivateObject(Teleporter.class, this, "worldServerInstance");
		this.dimensionID = dimensionID;
	}
	
	public void placeInPortal(Entity entity, double x, double y, double z, float yaw)
	{
		int id = worldServerInstance2.provider.dimensionId;
		if( id == 0 || id == -1)
			super.placeInPortal(entity, x, y, z, yaw);
		else
		{
			y = worldServerInstance2.getHeightValue((int)x, (int)z);
			entity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
		}
	}
	
	
	
	
	public void travelToDimension(Entity entity)
	{
		System.out.println("To infinity and Beyond!!!!");
		int par1 = dimensionID;
		if (entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP entityPlayer = (EntityPlayerMP) entity;
	        if (entityPlayer.dimension == 1 && par1 == 1)
	        {
	            entityPlayer.triggerAchievement(AchievementList.theEnd2);
	            entityPlayer.worldObj.removeEntity(entityPlayer);
	            entityPlayer.playerConqueredTheEnd = true;
	            entityPlayer.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(4, 0));
	        }
	        else
	        {
	            if (entityPlayer.dimension == 1 && par1 == 0)
	            {
	                entityPlayer.triggerAchievement(AchievementList.theEnd);
	                ChunkCoordinates chunkcoordinates = entityPlayer.mcServer.worldServerForDimension(par1).getEntrancePortalLocation();

	                if (chunkcoordinates != null)
	                {
	                    entityPlayer.playerNetServerHandler.setPlayerLocation((double)chunkcoordinates.posX, (double)chunkcoordinates.posY, (double)chunkcoordinates.posZ, 0.0F, 0.0F);
	                }

	                par1 = 1;
	            }
	            else
	            {
	                entityPlayer.triggerAchievement(AchievementList.portal);
	            }

	            entityPlayer.mcServer.getConfigurationManager().transferPlayerToDimension(entityPlayer, par1, this);
	            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastExperience", -1);
	            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastHealth", -1);
	            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastFoodLevel", -1);
	        }
		}
		else
		{
	        if (!entity.worldObj.isRemote && !entity.isDead)
	        {
	            entity.worldObj.theProfiler.startSection("changeDimension");
	            MinecraftServer minecraftserver = MinecraftServer.getServer();
	            int j = entity.dimension;
	            WorldServer worldserver = minecraftserver.worldServerForDimension(j);
	            WorldServer worldserver1 = minecraftserver.worldServerForDimension(par1);
	            entity.dimension = par1;
	            entity.worldObj.removeEntity(entity);
	            entity.isDead = false;
	            entity.worldObj.theProfiler.startSection("reposition");
	            minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1, this);
	            entity.worldObj.theProfiler.endStartSection("reloading");
	            Entity entity2 = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
	
	            if (entity2 != null)
	            {
	                entity2.copyDataFrom(entity, true);
	                worldserver1.spawnEntityInWorld(entity2);
	            }
	
	            entity.isDead = true;
	            entity.worldObj.theProfiler.endSection();
	            worldserver.resetUpdateEntityTick();
	            worldserver1.resetUpdateEntityTick();
	            entity.worldObj.theProfiler.endSection();
	        }
	    }
	}

}
