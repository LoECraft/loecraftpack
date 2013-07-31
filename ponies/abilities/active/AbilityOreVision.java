package loecraftpack.ponies.abilities.active;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.mechanics.MechanicHiddenOres;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class AbilityOreVision extends ActiveAbility
{	
	public static List<ItemStack> curatives = new ArrayList<ItemStack>();
	
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
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		MechanicHiddenOres.revealHiddenGems = true;
		//Do: adjust values by player stats
		MechanicHiddenOres.powerLevel = 2;
		MechanicHiddenOres.refreshRenderWithRange(player);
		return true;
	}

	@Override
	protected boolean CastSpellServer(EntityPlayer player, World world)
	{
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
		return true;
	}
	
	@Override
	protected void CastSpellUntoggledClient(EntityPlayer player)
	{
		MechanicHiddenOres.revealHiddenGems = false;
		MechanicHiddenOres.refreshRenderWithRange(player);
	}
}
