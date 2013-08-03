package loecraftpack.ponies.abilities.active;

import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.mechanics.MechanicTreeBucking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class AbilityBuckTree extends ActiveAbility
{
	public AbilityBuckTree()
	{
		super("Buck Tree", Race.EARTH, 100, 3);
	}
	
	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
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
			
			if (player.worldObj.getBlockId((int)x, (int)y, (int)z) == LoECraftPack.blockZapAppleLog.blockID ||
				player.worldObj.getBlockId((int)x, (int)y, (int)z) == LoECraftPack.blockAppleBloomLog.blockID)
			{
				int attemptID = AbilityPlayerData.attemptUse(energyCost);
				
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, Ability.TreeBuck, attemptID, (int)x, (int)y, (int)z));
				return true;
			}
			return false;
		}
	}
	
	@Override
	public void CastSpellServer(Player player, DataInputStream data) throws IOException
	{
		EntityPlayer sender = (EntityPlayer) player;
		int attemptID = data.readInt();
		MechanicTreeBucking.buckTree(sender.worldObj, data.readInt(), data.readInt(), data.readInt(), 0/*Do: BuckTree - fortune*/);
		int energyCost = (int)(this.getEnergyCost(sender));
		playerData.addEnergy(-energyCost);
		PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, attemptID, energyCost), player);
	}
}
