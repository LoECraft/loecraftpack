package loecraftpack.common.items;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import net.minecraft.item.EnumAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.Icon;

public class ItemRestorativeSubType {
	
	public List<Integer> restoreIDs = new ArrayList<Integer>();
	public String name;
	public String iconName;
	public Icon icon;
	public EnumAction useAnimation;
	
	ItemRestorativeSubType(String name)
	{
		this.name = name;
	    this.iconName = name.toLowerCase().replace(" ", "");
	    this.useAnimation = EnumAction.block;
	}
	
	/**
	 * set the use-age animation
	 */
	public ItemRestorativeSubType setAnimation(EnumAction useAnimation)
	{
		this.useAnimation = useAnimation;
		return this;
	}
	
	/**
	 * remove a potion effect that this can restore
	 */
	public ItemRestorativeSubType removeR(int effectID)
	{
		restoreIDs.remove((Integer)effectID);
		return this;
	}
	
	/**
	 * add a potion effect that this can restore
	 */
	public ItemRestorativeSubType addR(int effectID)
	{
		restoreIDs.add(effectID);
		return this;
	}
    
	/**
	 * add the generic list of utility spells effects
	 */
    public ItemRestorativeSubType addRMinorSpells()
	{
		restoreIDs.add(Potion.nightVision.id);
		restoreIDs.add(Potion.invisibility.id);
		
		return this;
	}
	
    /**
     * add the generic list of (de-)empowering spells effects
     */
	public ItemRestorativeSubType addRMajorSpells()
	{
		restoreIDs.add(LoECraftPack.potionCharged.id);
		
		return this;
	}
	
	/**
	 * add the generic list of bothersome debuffs
	 */
	public ItemRestorativeSubType addRSimpleDebuffs()
	{
		restoreIDs.add(Potion.blindness.id);
		restoreIDs.add(Potion.confusion.id);
		restoreIDs.add(Potion.poison.id);
		restoreIDs.add(Potion.hunger.id);
		restoreIDs.add(Potion.digSlowdown.id);
		
		return this;
	}
	
	/**
	 * add the generic list of encumbering debuffs
	 */
	public ItemRestorativeSubType addRHarshDebuffs()
	{
		restoreIDs.add(Potion.weakness.id);
		restoreIDs.add(Potion.wither.id);
		restoreIDs.add(Potion.moveSlowdown.id);
		
		return this;
	}

}
