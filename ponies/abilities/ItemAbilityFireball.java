package loecraftpack.ponies.abilities;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemAbilityFireball extends ItemAbility
{
	public ItemAbilityFireball(int par1)
	{
		super(par1, 3, 1);
		setUnlocalizedName("abilityFireball");
		race = Race.UNICORN;
		instance = this;
	}

	@Override
	protected boolean CastSpell(EntityPlayer player, World world)
	{
		PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.useAbility, AbilityList.Fireball));
		return true;
	}
}
