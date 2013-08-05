package loecraftpack.ponies.abilities.active;

import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.mechanics.MechanicHiddenOres;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AbilityOreVision extends ActiveAbility
{	
	protected static int toggleAfterImageID = -1;/*reserved for orevsion*/
	
	public AbilityOreVision()
	{
		super("Gem Vision", Race.UNICORN, 10);//to turn on
	}
	
	@Override
	public float getEnergyCostToggled(EntityPlayer player)
	{
		return 0.5f;
	}
	

	@Override
	protected boolean castSpellClient(EntityPlayer player, World world)
	{
		MechanicHiddenOres.revealHiddenGems = true;
		//Do: adjust values by player stats
		MechanicHiddenOres.powerLevel = 2;
		MechanicHiddenOres.refreshRenderWithRange(player);
		return true;
	}
	
	@Override
	protected boolean castSpellServer(EntityPlayer player, World world) {
		return true;
	}
	
	protected boolean CastSpellToggledClient(EntityPlayer player)
	{
		if (MechanicHiddenOres.xPos != (int) player.posX ||
			MechanicHiddenOres.yPos != (int) player.posY ||
			MechanicHiddenOres.zPos != (int) player.posZ )
		{
			MechanicHiddenOres.refreshRenderWithRange(player);
		}
		
		float cost = getEnergyCostToggled(player);
		AbilityPlayerData.addAfterImage(cost);
		
		return true;
	}
	
	@Override
	protected void CastSpellUntoggledClient(EntityPlayer player)
	{
		MechanicHiddenOres.revealHiddenGems = false;
		MechanicHiddenOres.refreshRenderWithRange(player);
	}

	
}
