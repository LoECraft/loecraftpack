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
		System.out.println("TreeBuck Client");
		MovingObjectPosition target = player.rayTrace(100, 1);
		if (target == null)
			return false;
		else
		{
			int x = (int)target.hitVec.xCoord;
			int y = (int)target.hitVec.yCoord;
			int z = (int)target.hitVec.zCoord;
			
			System.out.println("BUCK?"+world.isRemote);
			if (player.worldObj.getBlockId(x, y, z) == LoECraftPack.blockZapAppleLog.blockID ||
				player.worldObj.getBlockId(x, y, z) == LoECraftPack.blockAppleBloomLog.blockID)
			{
				System.out.println("BUCK"+world.isRemote);
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, AbilityList.TreeBuck, x, y, z));
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