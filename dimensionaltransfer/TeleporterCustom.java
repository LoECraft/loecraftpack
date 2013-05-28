package loecraftpack.dimensionaltransfer;

import java.util.Iterator;

import loecraftpack.LoECraftPack;
import loecraftpack.accessors.PrivateAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet202PlayerAbilities;
import net.minecraft.network.packet.Packet28EntityVelocity;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * This class allows players to be teleported in various ways, as opposed to the overly restricted Defaults.
 */
public class TeleporterCustom extends Teleporter {
	
	//instances
	public static TeleporterCustom teleporterSkyLandsRising;
	public static TeleporterCustom teleporterSkyLandsFalling;
	public static TeleporterCustom teleporterSkyLands;
	
	//versions
	public enum Method {Portal, Surface, Sky, Abyss};
	
	//instance data
	protected final WorldServer worldServerInstance2;
	protected final int dimensionID;
	private final Method method;
	
	
	public TeleporterCustom(WorldServer par1WorldServer, int dimensionID, Method method) {
		super(par1WorldServer);
		worldServerInstance2 = (WorldServer)PrivateAccessor.getPrivateObject(Teleporter.class, this, "worldServerInstance");
		this.dimensionID = dimensionID;
		this.method = method;
	}
	
	/**
	 * code executed to determine the location and events that occur when a player enters a dimension
	 */
	public void placeInPortal(Entity entity, double x, double y, double z, float yaw)
	{
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entity;
			player.timeUntilPortal = player.getPortalCooldown();
		}
		if(method == Method.Portal)
			super.placeInPortal(entity, x, y, z, yaw);
		else
		{
			if(method == Method.Surface)
				y = worldServerInstance2.getHeightValue((int)x, (int)z);
			else if(method == Method.Sky)
				y = worldServerInstance2.getHeight()-0.6;
			else
				y = 0;
			
			entity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
		}
	}
	
	/**
	 * Call this function before using a CustomTeleporter, that goes to a world that can unload.
	 * @param teleporter - the CustomTeleporter you want to use
	 * @param dimensionID - the dimension who's CustomTeleporter's are to be rebuilt, if the CustomTeleporter is null
	 */
	public static void refreshTeleporter(TeleporterCustom teleporter, int dimensionID)
	{
		if( teleporter == null)
		{
			World world = DimensionManager.getWorld(dimensionID);
			if (world == null)
				DimensionManager.initDimension(dimensionID);
			else
				buildTeleporters(world);
		}
		else if (DimensionManager.getWorld(teleporter.dimensionID) == null)
			DimensionManager.initDimension(teleporter.dimensionID);
	}
	
	/**
	 * rebuilds all CustomTeleporter's to that world.
	 * @param world
	 */
	public static void buildTeleporters(World world)
	{
		if(world == DimensionManager.getWorld(0))
		{
			System.out.println("world Overworld: creating teleporter");
			TeleporterCustom.teleporterSkyLandsFalling = new TeleporterCustom(MinecraftServer.getServer().worldServerForDimension(0), 0, Method.Sky);
		}
		else if (world == DimensionManager.getWorld(LoECraftPack.SkylandDimensionID))
		{
			System.out.println("world Skyland: creating teleporter");
			TeleporterCustom.teleporterSkyLands = new TeleporterCustom(MinecraftServer.getServer().worldServerForDimension(LoECraftPack.SkylandDimensionID), LoECraftPack.SkylandDimensionID, Method.Surface);
			TeleporterCustom.teleporterSkyLandsRising = new TeleporterCustom(MinecraftServer.getServer().worldServerForDimension(LoECraftPack.SkylandDimensionID), LoECraftPack.SkylandDimensionID, Method.Abyss);
		}
	}
	
	/**
	 * null's all CustomTeleporter's to that world.   call this function when a world unloads, to prevent leakage.
	 * @param world
	 */
	public static void clearTeleporters(World world)
	{
		if(world == DimensionManager.getWorld(0))
		{
			System.out.println("world Overworld: clearing teleporter");
			TeleporterCustom.teleporterSkyLandsFalling = null;
		}
		else if (world == DimensionManager.getWorld(LoECraftPack.SkylandDimensionID))
		{
			System.out.println("world Skyland: clearing teleporters");
			TeleporterCustom.teleporterSkyLands = null;
			TeleporterCustom.teleporterSkyLandsRising = null;
		}
	}
	
	
	/**
	 * modified code from Entity & EntityPlayerMP. It handles the transfer of a entity from one dimension to the next.
	 *   this version enables custom teleporting methods, assigned by, Enum Method.
	 * @param entity
	 */
	public void travelToDimension(Entity entity)
	{
		System.out.println("To infinity and Beyond!!!!");
		int id = dimensionID;
		
		if (entity instanceof EntityPlayerMP)
		{
			
			EntityPlayerMP entityPlayer = (EntityPlayerMP) entity;
			if(method == Method.Portal)
			{
		        if (entityPlayer.dimension == 1 && id == 1)
		        {
		            entityPlayer.triggerAchievement(AchievementList.theEnd2);
		            entityPlayer.worldObj.removeEntity(entityPlayer);
		            entityPlayer.playerConqueredTheEnd = true;
		            entityPlayer.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(4, 0));
		        }
		        else
		        {
		            if (entityPlayer.dimension == 1 && id == 0)
		            {
		                entityPlayer.triggerAchievement(AchievementList.theEnd);
		                ChunkCoordinates chunkcoordinates = entityPlayer.mcServer.worldServerForDimension(id).getEntrancePortalLocation();
	
		                if (chunkcoordinates != null)
		                {
		                    entityPlayer.playerNetServerHandler.setPlayerLocation((double)chunkcoordinates.posX, (double)chunkcoordinates.posY, (double)chunkcoordinates.posZ, 0.0F, 0.0F);
		                }
	
		                id = 1;
		            }
		            else
		            {
		                entityPlayer.triggerAchievement(AchievementList.portal);
		            }
	
		            entityPlayer.mcServer.getConfigurationManager().transferPlayerToDimension(entityPlayer, id, this);
		            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastExperience", -1);
		            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastHealth", -1);
		            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastFoodLevel", -1);
		        }
			}
			else
			{
				//Packet velocity = new Packet28EntityVelocity(entityPlayer);
				this.transferPlayerToDimension(entityPlayer, id, this);
	            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastExperience", -1);
	            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastHealth", -1);
	            PrivateAccessor.setPrivateVariable(EntityPlayerMP.class, entityPlayer, "lastFoodLevel", -1);
	            /*if (method==Method.Abyss || method==Method.Sky)
	            	entityPlayer.playerNetServerHandler.sendPacketToPlayer(velocity);*/
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
	            WorldServer worldserver1 = minecraftserver.worldServerForDimension(id);
	            entity.dimension = id;
	            entity.worldObj.removeEntity(entity);
	            entity.isDead = false;
	            entity.worldObj.theProfiler.startSection("reposition");
	            minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1, this);
	            entity.worldObj.theProfiler.endStartSection("reloading");
	            Entity entity2 = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
	
	            if (entity2 != null)
	            {
	                entity2.copyDataFrom(entity, true);
	                if(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.allowFlying)
	                {
	                	((EntityPlayer) entity2).capabilities.isFlying = ((EntityPlayer) entity).capabilities.isFlying;
	                }
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
	
	/**
	 * modified code from ServerConfigurationManager; part of the transfer process.
	 *   this version enables retaining flying mode, if the method is either Abyss or Sky.
	 * @param par1EntityPlayerMP
	 * @param targetDimensionID
	 * @param teleporter
	 */
	public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int targetDimensionID, Teleporter teleporter)
    {
		ServerConfigurationManager scm = par1EntityPlayerMP.mcServer.getConfigurationManager();
		MinecraftServer ms = (MinecraftServer)PrivateAccessor.getPrivateObject(ServerConfigurationManager.class, scm, "mcServer");
		
        int j = par1EntityPlayerMP.dimension;
        WorldServer worldserver = ms.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = targetDimensionID;
        WorldServer worldserver1 = ms.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(), worldserver1.getHeight(), par1EntityPlayerMP.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(par1EntityPlayerMP);
        par1EntityPlayerMP.isDead = false;
        scm.transferEntityToWorld(par1EntityPlayerMP, j, worldserver, worldserver1, teleporter);
        scm.func_72375_a(par1EntityPlayerMP, worldserver);
        par1EntityPlayerMP.playerNetServerHandler.setPlayerLocation(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
        par1EntityPlayerMP.theItemInWorldManager.setWorld(worldserver1);
        scm.updateTimeAndWeatherForPlayer(par1EntityPlayerMP, worldserver1);
        scm.syncPlayerInventory(par1EntityPlayerMP);
        Iterator iterator = par1EntityPlayerMP.getActivePotionEffects().iterator();

        while (iterator.hasNext())
        {
            PotionEffect potioneffect = (PotionEffect)iterator.next();
            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(par1EntityPlayerMP.entityId, potioneffect));
        }
        
        //retain flying
        if(method == Method.Abyss || method == Method.Sky)
        	par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet202PlayerAbilities(par1EntityPlayerMP.capabilities));
        

        GameRegistry.onPlayerChangedDimension(par1EntityPlayerMP);
    }

}
