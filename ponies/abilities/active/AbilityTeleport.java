package loecraftpack.ponies.abilities.active;

import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.ActiveAbility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class AbilityTeleport extends ActiveAbility
{
	public float energyCostRate = 5.0f;
	
	public AbilityTeleport()
	{
		super("Teleport", Race.UNICORN, 0, 5);
	}

	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		MovingObjectPosition target = player.rayTrace(getMaxDistance(player), 1);
		if (target == null)
			return false;
		else
		{
			double x;
			double y;
			double z;
			
			x = target.hitVec.xCoord;
			y = target.hitVec.yCoord;
			z = target.hitVec.zCoord;
			
			if(target.entityHit == null)
			{
				switch(target.sideHit)
				{
					case 0: y -= 0.5d; break;
					case 1: y += 0.5d; break;
					case 2: z -= 0.5d; break;
					case 3: z += 0.5d; break;
					case 4: x -= 0.5d; break;
					case 5: x += 0.5d; break;
				}
			}
			
			int attemptID = AbilityPlayerData.attemptUse(energyCost);
			
			PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, Ability.Teleport, attemptID, x, y, z));
		}
		
		return true;
	}
	
	@Override
	public void CastSpellServer(Player player, AbilityPlayerData abilityData, DataInputStream data) throws IOException
	{
		EntityPlayer sender = (EntityPlayer)player;
		int attemptID = data.readInt();
		double x = data.readDouble();
		double y = data.readDouble();
		double z = data.readDouble();
		
		if (sender.capabilities.isCreativeMode)
		{
			sender.setPositionAndUpdate(x, y, z);
			PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, attemptID, 0), player);
		}
		else
		{
			double distance = sender.getPosition(1.0f).distanceTo(Vec3.createVectorHelper(x, y, z));
			if (distance <= getMaxDistance(sender))
			{
				int energyCost = (int)(energyCostRate * distance);
				if (energyCost <= playerData.energy)
				{
					sender.setPositionAndUpdate(x, y, z);
					playerData.addEnergy(-energyCost);
					PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, attemptID, energyCost), player);
					return;
				}
			}
			PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, attemptID, 0), player);
		}
	}
	
	@Override
	public float getEnergyCost(EntityPlayer player)
	{
		if (!player.worldObj.isRemote)
			return 0;
		MovingObjectPosition target = player.rayTrace(getMaxDistance(player), 1);
		if (target == null)
			return Float.MAX_VALUE;
		else
		{
			double distance = player.getPosition(1.0f).distanceTo(target.hitVec);
			return energyCost = (int)(energyCostRate * distance);
		}
	}
	
	public float getMaxDistance(EntityPlayer player)
	{
		return 100;
	}
}
