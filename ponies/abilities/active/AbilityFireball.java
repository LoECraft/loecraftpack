package loecraftpack.ponies.abilities.active;

import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.projectiles.Fireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class AbilityFireball extends ActiveAbility
{
	public AbilityFireball()
	{
		super("Fireball", Race.UNICORN, 100, 3, 1);
	}

	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		int attemptID = AbilityPlayerData.attemptUse(activeID, energyCost);
		PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, activeID, attemptID));
		return true;
	}

	@Override
	protected boolean castSpellServerPacket(Player player, int attemptID, DataInputStream data) throws IOException
	{
		EntityPlayer sender = (EntityPlayer) player;
		energyCost = (int)(this.getEnergyCost(sender));
		System.out.println("FireBall: "+energyCost+" "+playerData.energy);
		if(playerData.energy>=energyCost)
		{
			Fireball fireball = new Fireball(sender.worldObj, sender, sender.getLookVec().xCoord/10f, sender.getLookVec().yCoord/10f, sender.getLookVec().zCoord/10f);
			sender.worldObj.spawnEntityInWorld(fireball);
			playerData.addEnergy(-energyCost);
			PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, activeID, attemptID, 1, energyCost), player);
			
			return true;
		}
		return false;
	}
}
