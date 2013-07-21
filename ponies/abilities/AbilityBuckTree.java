package loecraftpack.ponies.abilities;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.mechanics.MechanicTreeBucking;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class AbilityBuckTree extends Ability {

	public AbilityBuckTree()
	{
		super("Buck Tree", Race.EARTH, 20, 3);
	}
	
	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		//Debug: TreeBuck Client
		System.out.println("TreeBuck Client");
		MovingObjectPosition target = player.rayTrace(100, 1);
		if (target == null)
			return false;
		else
		{
			double x = (int)target.hitVec.xCoord;
			double y = (int)target.hitVec.yCoord;
			double z = (int)target.hitVec.zCoord;
			
			if(target.entityHit == null)
			{
				switch(target.sideHit)
				{
					case 0: y += 0.5d; break;
					case 1: y -= 0.5d; break;
					case 2: z += 0.5d; break;
					case 3: z -= 0.5d; break;
					case 4: x += 0.5d; break;
					case 5: x -= 0.5d; break;
				}
			}
			//Debug: TreeBuck Client
			System.out.println("BUCK?"+world.isRemote);
			if (player.worldObj.getBlockId((int)x, (int)y, (int)z) == LoECraftPack.blockZapAppleLog.blockID ||
				player.worldObj.getBlockId((int)x, (int)y, (int)z) == LoECraftPack.blockAppleBloomLog.blockID)
			{
				//Debug: TreeBuck Client
				System.out.println("BUCK"+world.isRemote);
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, AbilityList.TreeBuck, (int)x, (int)y, (int)z));
				return true;
			}
			return false;
		}
	}
	
	@Override
	protected boolean CastSpellServer(EntityPlayer player, World world)
	{
		System.out.println("TreeBuck Server");
		return true;
	}
	
}
