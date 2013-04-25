package loecraftpack.logic.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Dye;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ColoredBedHandler
{
	private ColoredBedHandler(){}
	
	public static int numBeds = 0;
	public static List<String> iconNames = new ArrayList<String>();
	private static Map<String, String[]> bedPairs = new HashMap<String, String[]>();
	
	private static void addBedRecipe(Dye color1, Dye color2, Dye color3)
	{
		CraftingManager.getInstance().addRecipe(new ItemStack(LoECraftPack.bedItems, 1, numBeds), "ABC", "XXX",
				                                     'A', new ItemStack(Block.cloth, 1, color1.ordinal()),
				                                     'B', new ItemStack(Block.cloth, 1, color2.ordinal()),
				                                     'C', new ItemStack(Block.cloth, 1, color3.ordinal()),
				                                     'X', Block.planks);
		numBeds++;
	}
	
	public static void addCustomBed(String display, Dye color)
	{
		LanguageRegistry.instance().addStringLocalization("item.coloredBed." + numBeds + ".name", display + " Bed");
		iconNames.add(display.replace(" ", "").toLowerCase());
		addBedRecipe(color, color, color);
	}
	
	public static void addCustomBed(String display, Dye color1, Dye color2, Dye color3)
	{
		LanguageRegistry.instance().addStringLocalization("item.coloredBed." + numBeds + ".name", display + " Bed");
		iconNames.add(display.replace(" ", "").toLowerCase());
		addBedRecipe(color1, color2, color3);
	}
	
	public static void addBedPair(String name, String bedLeft, String bedRight)
	{
		bedPairs.put(name, new String[] {bedLeft.replace(" ", "").toLowerCase(), bedRight.replace(" ", "").toLowerCase()});
	}
	
	public static String getPairName(String bedLeft, String bedRight)
	{
		for(int i = 0; i < bedPairs.size(); i++ )
		{
			String[][] pairs = bedPairs.values().toArray(new String[0][0]);
			
			if (pairs[i][0].equals(bedLeft) && pairs[i][1].equals(bedRight))
			{
				String[] names = bedPairs.keySet().toArray(new String[0]);
				return names[i];
			}
		}
		
		return "";
	}
}
