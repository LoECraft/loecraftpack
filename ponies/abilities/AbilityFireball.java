package loecraftpack.ponies.abilities;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.projectiles.Fireball;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AbilityFireball extends ActiveAbility
{
	public AbilityFireball()
	{
		super("Fireball", Race.UNICORN, 100, 3, 1);
	}

	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		return true;
	}

	@Override
	protected boolean CastSpellServer(EntityPlayer player, World world)
	{
		Fireball fireball = new Fireball(world, player, player.getLookVec().xCoord/10f, player.getLookVec().yCoord/10f, player.getLookVec().zCoord/10f);
		world.spawnEntityInWorld(fireball);
		return true;
	}
}
