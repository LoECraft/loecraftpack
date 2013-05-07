package loecraftpack.common.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Dye;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class HandlerColoredBed
{
	private HandlerColoredBed(){}
	
	public static int numBeds = 0;
	public static List<String> iconNames = new ArrayList<String>();
	public static Map<String, String[]> bedPairs = new HashMap<String, String[]>();
	
	private static void addBedRecipe(Dye color1, Dye color2, Dye color3)
	{
		CraftingManager.getInstance().addRecipe(new ItemStack(LoECraftPack.bedItems, 1, numBeds), "ABC", "XXX",
				                                     'A', new ItemStack(Block.cloth, 1, color1.ordinal()),
				                                     'B', new ItemStack(Block.cloth, 1, color2.ordinal()),
				                                     'C', new ItemStack(Block.cloth, 1, color3.ordinal()),
				                                     'X', Block.planks);
		numBeds++;
	}
	
	//locate and remove old bed recipe
	public static void cleanBedRecipe()
	{
    	Iterator r = CraftingManager.getInstance().getRecipeList().iterator();
    	while (r.hasNext())
    	{
    		IRecipe ir = (IRecipe)r.next();
    		//if the recipe outputs a bed, remove it
			if (ir.getRecipeOutput() != null && ir.getRecipeOutput().itemID == Item.bed.itemID)
			{
				r.remove();
				break; //there really should only be one vanilla bed to remove, so stop once we find it
			}
    	}
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
	
	
	
	public static String getPairName(int id)
	{
		if (id >= 0 && id < bedPairs.size())
			return bedPairs.keySet().toArray(new String[0])[id];
		return "";
	}
	
	public static int getPairID(int bedLeftID, int bedRightID)
	{
		String bedLeftName = iconNames.get(bedLeftID);
		String bedRightName = iconNames.get(bedRightID);
		for (int i = 0; i < bedPairs.size(); i++)
		{
			String[][] pairs = bedPairs.values().toArray(new String[0][0]);
			
			if (pairs[i][0].equals(bedLeftName) && pairs[i][1].equals(bedRightName))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public static int findPairDirection(int pairID, int scourceID)
	{
		String scourceName = iconNames.get(scourceID);
		//return direction the partner would be in.  1 for right, -1 for left
		String[][] pairs = bedPairs.values().toArray(new String[0][0]);
		if (pairs[pairID][0].equals(scourceName))
			return 1; //to the right
		else if (pairs[pairID][1].equals(scourceName))
			return -1; //to the left
		return 0;//none
	}
}
