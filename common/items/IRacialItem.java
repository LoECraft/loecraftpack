package loecraftpack.common.items;

import net.minecraft.entity.player.EntityPlayer;
import loecraftpack.enums.Race;

public interface IRacialItem
{
	/**
	 * Determines which races can use this Item
	 */
	public abstract boolean canBeUsedBy(Race race);
}
