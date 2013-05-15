package loecraftpack.ponies.abilities.mechanics;

import java.util.List;

import loecraftpack.common.logic.PrivateAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class MechanicHiddenOres {
	
	public static boolean revealHiddenGems = false;//TODO move this to player logic

	//client side code
	public static void switchHiddenOreRevealState(EntityPlayer player)
	{
		//switch visual mode, and refresh render
		if(player.worldObj.isRemote)
		{
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
