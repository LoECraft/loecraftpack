package loecraftpack.ponies.abilities.mechanics;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.common.logic.PrivateAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class MechanicHiddenOres {
	
	@SideOnly(Side.CLIENT)
	public static boolean revealHiddenGems = false;

	//client side code
	public static void switchHiddenOreRevealState(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
		{
			//switch visual mode, and refresh render
			revealHiddenGems = !revealHiddenGems;
			WorldRenderer[] worldRenderer = (WorldRenderer[])PrivateAccessor.getPrivateObject(Minecraft.getMinecraft().renderGlobal, "worldRenderers");
			List worldRenderersToUpdate = (List)PrivateAccessor.getPrivateObject(Minecraft.getMinecraft().renderGlobal, "worldRenderersToUpdate");
			for(int i=0; i < worldRenderer.length; i++)
			{
				if(worldRenderer[i] != null)
				{
					worldRenderersToUpdate.add(worldRenderer[i]);
					worldRenderer[i].markDirty();
				}
			}
			System.out.print("mode switch - "+ revealHiddenGems+" - got list - "+(worldRenderer != null));
		}
	}
}
