package loecraftpack.ponies.abilities.passive;

import net.minecraft.entity.player.EntityPlayer;
import loecraftpack.common.logic.HandlerKey;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.PassiveAbility;

public class AbilityHighJump extends PassiveAbility
{
	public AbilityHighJump()
	{
		super("High Jump", Race.EARTH, 1);
	}

	@Override
	public void onUpdateClient(EntityPlayer player)
	{
		if (HandlerKey.GetKeyDown(HandlerKey.jump) && player.motionY > 0)
			player.motionY *= 1.4f;
	}
}
