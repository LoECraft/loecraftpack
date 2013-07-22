package loecraftpack.common.items;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import net.minecraft.potion.Potion;
import net.minecraft.util.Icon;

public class ItemRestorativeSubType {
	
	public List<Integer> restoreIDs = new ArrayList<Integer>();
	public String name;
	public String iconName;
	public Icon icon;
	
	ItemRestorativeSubType(String name)
	{
		this.name = name;
	    this.iconName = name.toLowerCase().replace(" ", "");
	}
	
	/**
	 * add a potion effect that this can restore
	 */
	public ItemRestorativeSubType addR(int effectID)
	{
		restoreIDs.add(effectID);
		return this;
	}
    
    public ItemRestorativeSubType addRMinorSpells()
	{
		restoreIDs.add(LoECraftPack.potionOreVision.id);
		restoreIDs.add(Potion.nightVision.id);
		restoreIDs.add(Potion.invisibility.id);
		
		return this;
	}
	
	public ItemRestorativeSubType addRMajorSpells()
	{
		restoreIDs.add(LoECraftPack.potionCharged.id);
		
		return this;
	}
	
	public ItemRestorativeSubType addRSimpleDebuffs()
	{
		restoreIDs.add(Potion.blindness.id);
		restoreIDs.add(Potion.confusion.id);
		restoreIDs.add(Potion.poison.id);
		restoreIDs.add(Potion.hunger.id);
		restoreIDs.add(Potion.digSlowdown.id);
		
		return this;
	}
	
	public ItemRestorativeSubType addRHarshDebuffs()
	{
		restoreIDs.add(Potion.weakness.id);
		restoreIDs.add(Potion.wither.id);
		restoreIDs.add(Potion.moveSlowdown.id);
		
		return this;
	}

}
