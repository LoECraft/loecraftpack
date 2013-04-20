package loecraftpack.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShopLogic
{
	public static String questTitle = "Error - No Title";
	public static String questTask = "Error - No Data"; //The task is the explicit instruction of what to do 
	public static String questText = "Error - No Data"; //Text is the story behind it.
	public static List<ItemStack> rewards = new ArrayList<ItemStack>();
	public static int bitReward = 0;
	
	public static void parseShop(String[] params)
	{
		
	}
}
