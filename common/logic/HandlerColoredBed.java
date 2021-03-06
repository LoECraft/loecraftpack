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
	
	public static void loadBeds()
	{
		//Add base-color beds
    	HandlerColoredBed.addCustomBed("Rarity", Dye.White);
    	HandlerColoredBed.addCustomBed("Derpy", Dye.LightGray);
    	HandlerColoredBed.addCustomBed("Octavia", Dye.Gray);
    	HandlerColoredBed.addCustomBed("Discord", Dye.Black);
    	HandlerColoredBed.addCustomBed("Big Mac", Dye.Red);
    	HandlerColoredBed.addCustomBed("Applejack", Dye.Orange);
    	HandlerColoredBed.addCustomBed("Fluttershy", Dye.Yellow);
    	HandlerColoredBed.addCustomBed("Granny Smith", Dye.Lime);
    	HandlerColoredBed.addCustomBed("Spike", Dye.Green);
    	HandlerColoredBed.addCustomBed("Trixie", Dye.Cyan);
    	HandlerColoredBed.addCustomBed("Rainbow Dash", Dye.LightBlue);
    	HandlerColoredBed.addCustomBed("Luna", Dye.Blue);
    	HandlerColoredBed.addCustomBed("Twilight Sparkle", Dye.Purple);
    	HandlerColoredBed.addCustomBed("Cheerilee", Dye.Magenta);
    	HandlerColoredBed.addCustomBed("Pinkie Pie", Dye.Pink);
    	HandlerColoredBed.addCustomBed("Muffin", Dye.Brown);
    	
    	//Add combo-color beds
    	HandlerColoredBed.addCustomBed("Celestia", Dye.Lime, Dye.LightBlue, Dye.Pink);
    	HandlerColoredBed.addCustomBed("Fausticorn", Dye.White, Dye.Red, Dye.White);
    	HandlerColoredBed.addCustomBed("CMC", Dye.Red, Dye.Blue, Dye.Yellow);
    	HandlerColoredBed.addCustomBed("Sweetie Belle", Dye.White, Dye.Pink, Dye.Magenta);
    	HandlerColoredBed.addCustomBed("Scootaloo", Dye.Orange, Dye.Purple, Dye.Orange);
    	HandlerColoredBed.addCustomBed("Babs Seed", Dye.Brown, Dye.Red, Dye.Pink);
    	HandlerColoredBed.addCustomBed("Apple Bloom", Dye.Yellow, Dye.Red, Dye.Yellow);
    	HandlerColoredBed.addCustomBed("Silver Spoon", Dye.Gray, Dye.LightGray, Dye.LightBlue);
    	HandlerColoredBed.addCustomBed("Diamond Tiara", Dye.Pink, Dye.Purple, Dye.White);
    	HandlerColoredBed.addCustomBed("Lyra", Dye.Lime, Dye.Yellow, Dye.Cyan);
    	HandlerColoredBed.addCustomBed("Bon Bon", Dye.Blue, Dye.Pink, Dye.Yellow);
    	HandlerColoredBed.addCustomBed("Spitfire", Dye.Yellow, Dye.Orange, Dye.Yellow);
    	HandlerColoredBed.addCustomBed("Shining Armor", Dye.White, Dye.LightBlue, Dye.Blue);
    	HandlerColoredBed.addCustomBed("Cadence", Dye.Purple, Dye.Magenta, Dye.Yellow);
    	HandlerColoredBed.addCustomBed("Colgate", Dye.White, Dye.Blue, Dye.Cyan);
    	HandlerColoredBed.addCustomBed("Vinyl Scratch", Dye.White, Dye.Blue, Dye.LightBlue);
    	
    	//Register bed pairs
    	HandlerColoredBed.addBedPair("Alicorn Sisters", "Celestia", "Luna");
    	HandlerColoredBed.addBedPair("Belle Sisters", "Rarity", "Sweetie Belle");
    	HandlerColoredBed.addBedPair("Apple Sisters", "Apple Jack", "Apple Bloom");
    	HandlerColoredBed.addBedPair("Wing Sisters", "Rainbow Dash", "Scootaloo");
    	HandlerColoredBed.addBedPair("Double Trouble", "Silver Spoon", "Diamond Tiara");
    	HandlerColoredBed.addBedPair("Fangasm", "Lyra", "Bon Bon");
    	HandlerColoredBed.addBedPair("Royal Couple", "Shining Armor", "Cadence");
    	HandlerColoredBed.addBedPair("Muffin Love", "Derpy", "Muffin");
    	HandlerColoredBed.addBedPair("Chocolate Rain", "Pinkie Pie", "Discord");
    	HandlerColoredBed.addBedPair("Music", "Octavia", "Vinyl Scratch");
	}
}
