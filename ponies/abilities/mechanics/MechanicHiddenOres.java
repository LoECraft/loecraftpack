package loecraftpack.ponies.abilities.mechanics;

import java.util.List;

import loecraftpack.common.logic.PrivateAccessor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class handles code regarding the visibility of hidden ores.
 */
public class MechanicHiddenOres {
	
	@SideOnly(Side.CLIENT)
	public static boolean revealHiddenGems = false;
	@SideOnly(Side.CLIENT)
	public static boolean bootUp = true;
	@SideOnly(Side.CLIENT)
	public static int powerLevel = 0;
	@SideOnly(Side.CLIENT)
	public static int xPos;
	@SideOnly(Side.CLIENT)
	public static int yPos;
	@SideOnly(Side.CLIENT)
	public static int zPos;
	
	//TODO make this range dependant
	public static void refreshRenderWithRange(EntityPlayer player)
	{
		if (!player.worldObj.isRemote)
			return;
		
		xPos = (int) player.posX;
		yPos = (int) player.posY;
		zPos = (int) player.posZ;
		
		WorldRenderer[] worldRenderer = (WorldRenderer[])PrivateAccessor.getPrivateObject(Minecraft.getMinecraft().renderGlobal, "worldRenderers");
		List worldRenderersToUpdate = (List)PrivateAccessor.getPrivateObject(Minecraft.getMinecraft().renderGlobal, "worldRenderersToUpdate");
		for (int i=0; i < worldRenderer.length; i++)
		{
			if (worldRenderer[i] != null && inRangeForRefresh(player, worldRenderer[i]))
			{
				worldRenderersToUpdate.add(worldRenderer[i]);
				worldRenderer[i].markDirty();
			}
		}
		System.out.print("ore mode - "+ revealHiddenGems);
	}
	
	//world renders that need to be updated
	@SideOnly(Side.CLIENT)
	protected static boolean inRangeForRefresh(EntityPlayer player, WorldRenderer worldRenderer)
	{
		float buffer = 5;//Dependent on move speed
		
		return inRange(Math.floor(player.posX)+0.5, Math.floor(player.posY)+0.5, Math.floor(player.posZ)+0.5,
					   worldRenderer.posX,    worldRenderer.posY,    worldRenderer.posZ,
					   worldRenderer.posX+16, worldRenderer.posY+16, worldRenderer.posZ+16,
					   (powerLevel+1)*10+buffer);
	}
	
	//blocks in range of the player
	@SideOnly(Side.CLIENT)
	public static boolean inRangeofClientPlayer(int x, int y, int z)
	{
		return inRange(xPos+0.5, yPos+0.5, zPos+0.5,
				       x, y, z, x+1, y+1, z+1,
				       (powerLevel+1)*10);
	}
	
	/**
	 * This methods finds if a square intersects a sphere
	 * @param xPos - sphere
	 * @param yPos - sphere
	 * @param zPos - sphere
	 * @param xMinus - square
	 * @param yMinus - square
	 * @param zMinus - square
	 * @param xPlus - square
	 * @param yPlus - square
	 * @param zPlus - square
	 * @param range - sphere
	 * @return
	 */
	protected static boolean inRange(double xPos,   double yPos,   double zPos,
									 double xMinus, double yMinus, double zMinus,
									 double xPlus,  double yPlus,  double zPlus,
									 double range)
	{
		byte closestX = (byte)(xMinus>xPos ? -1 : xPlus<xPos ? 1 : 0);
		if (closestX==1? xPlus+range<xPos : xMinus-range>xPos)return false;
		byte closestY = (byte)(yMinus>yPos ? -1 : yPlus<yPos ? 1 : 0);
		if (closestY==1? yPlus+range<yPos : yMinus-range>yPos)return false;
		byte closestZ = (byte)(zMinus>zPos ? -1 : zPlus<zPos ? 1 : 0);
		if (closestZ==1? zPlus+range<zPos : zMinus-range>zPos)return false;
		
		Vec3 center;
		Vec3 target;
		
		if(closestX==0)
		{
			if(closestY==0)
			{
				if(closestZ==0)
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(xPlus, yPlus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xPlus, yMinus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xPlus, yPlus, zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xPlus, yMinus, zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, yPlus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, yMinus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, yPlus, zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, yMinus, zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
				else
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(xPlus, yPlus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xPlus, yMinus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, yPlus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, yMinus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
			}
			else
			{
				if(closestZ==0)
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(xPlus, closestY==1? yPlus: yMinus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xPlus, closestY==1? yPlus: yMinus, zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, closestY==1? yPlus: yMinus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, closestY==1? yPlus: yMinus, zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
				else
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(xPlus, closestY==1? yPlus: yMinus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(xMinus, closestY==1? yPlus: yMinus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
			}
		}
		else
		{
			if(closestY==0)
			{
				if(closestZ==0)
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, yPlus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, yMinus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, yPlus, zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, yMinus, zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
				else
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, yPlus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, yMinus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
			}
			else
			{
				if(closestZ==0)
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, closestY==1? yPlus: yMinus, zPlus);
					if (center.distanceTo(target) <= range)return true;
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, closestY==1? yPlus: yMinus, zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
				else
				{
					center = Vec3.createVectorHelper(xPos, yPos, zPos);
					target = Vec3.createVectorHelper(closestX==1? xPlus: xMinus, closestY==1? yPlus: yMinus, closestZ==1? zPlus: zMinus);
					if (center.distanceTo(target) <= range)return true;
				}
			}
		}
		
		return false;
	}
}
