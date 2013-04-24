package loecraftpack.logic.handlers;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.blocks.ColoredBedBlock;
import loecraftpack.enums.Dye;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ColoredBedHandler
{
	private ColoredBedHandler(){}
	
	public static int numBeds = 0;
	public static List<String> customBedIconNames = new ArrayList<String>();
	
	static void addBedRecipe(Dye color1, Dye color2, Dye color3)
	{
		CraftingManager.getInstance().addRecipe(new ItemStack(LoECraftPack.bedItems, 1, numBeds), "ABC", "XXX", 'A', new ItemStack(Block.cloth, 1, color1.ordinal()),
				                                                                    'B', new ItemStack(Block.cloth, 1, color2.ordinal()),
				                                                                    'C', new ItemStack(Block.cloth, 1, color3.ordinal()),
				                                                                    'X', Block.planks);
		numBeds++;
	}
	
	public static void addCustomBed(String display, Dye color)
	{
		LanguageRegistry.instance().addStringLocalization("item.coloredBed." + numBeds + ".name", display + " Bed");
		customBedIconNames.add(display.replace(" ", "").toLowerCase());
		addBedRecipe(color, color, color);
	}
	
	public static void addCustomBed(String display, Dye color1, Dye color2, Dye color3)
	{
		LanguageRegistry.instance().addStringLocalization("item.coloredBed." + numBeds + ".name", display + " Bed");
		customBedIconNames.add(display.replace(" ", "").toLowerCase());
		addBedRecipe(color1, color2, color3);
	}
}
