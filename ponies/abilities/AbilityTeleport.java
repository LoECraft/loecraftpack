package loecraftpack.ponies.abilities;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class AbilityTeleport extends Ability
{
	int maxDistance = 100;
	float energyCostRate = 5.0f;
	
	public AbilityTeleport()
	{
		super("Teleport", Race.UNICORN, 0, 5);
	}

	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		MovingObjectPosition target = player.rayTrace(maxDistance, 1);
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
			
			PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, AbilityList.Teleport, x, y, z));
		}
		
		return true;
	}
	
	@Override
	protected boolean CastSpellServer(EntityPlayer player, World world)
	{
		return true;
	}
	
	@Override
	public float getEnergyCost(EntityPlayer player)
	{
		MovingObjectPosition target = player.rayTrace(maxDistance, 1);
		if (target == null)
			return 1000000000;
		else
		{
			double distance = player.getPosition(1.0f).distanceTo(target.hitVec);
			return energyCost = (int)(energyCostRate * distance);
		}
	}
	
	
}
