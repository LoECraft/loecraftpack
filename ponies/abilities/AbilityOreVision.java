package loecraftpack.ponies.abilities;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.mechanics.MechanicHiddenOres;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class AbilityOreVision extends Ability {
	
	public static List<ItemStack> curatives = new ArrayList<ItemStack>();
	
	public AbilityOreVision()
	{
		super("Ore Vision", Race.UNICORN, 20);
	}

	@Override
	protected boolean CastSpellClient(EntityPlayer player, World world)
	{
		return true;
	}

	@Override
	protected boolean CastSpellServer(EntityPlayer player, World world)
	{
		//Do: adjust values by player stats
		PotionEffect effect = new PotionEffect(LoECraftPack.potionOreVision.id, 1200, 0);
		effect.setCurativeItems(curatives);
		player.addPotionEffect(effect);
		return true;
	}
	
	@Override
	public float getEnergyCostToggled(EntityPlayer player)
	{
		return 1f;
	}
}
