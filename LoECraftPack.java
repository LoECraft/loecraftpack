package loecraftpack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import loecraftpack.blocks.BlockBedColor;
import loecraftpack.blocks.ProtectionMonolithBlock;
import loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import loecraftpack.items.Bits;
import loecraftpack.items.ItemBedColor;
import loecraftpack.logic.handlers.EventHandler;
import loecraftpack.logic.handlers.GuiHandler;
import loecraftpack.packethandling.ClientPacketHandler;
import loecraftpack.packethandling.ServerPacketHandler;
import loecraftpack.proxies.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@Mod(modid = "loecraftpack", name = "LoECraft Pack", version = "1.0")

@NetworkMod(clientSideRequired=true, serverSideRequired=false, clientPacketHandlerSpec = @SidedPacketHandler(channels = {"loecraftpack" }, packetHandler = ClientPacketHandler.class),
serverPacketHandlerSpec = @SidedPacketHandler(channels = {"loecraftpack" }, packetHandler = ServerPacketHandler.class))
public class LoECraftPack
{
	@Instance
    public static LoECraftPack instance = new LoECraftPack();
	
	ItemBedColor[] bedItems = new ItemBedColor[16];
	BlockBedColor[] bedBlocks = new BlockBedColor[16];
	String[] colors = new String[]
	{
		"White",
		"Orange",
		"Magenta",
		"Light Blue",
		"Yellow",
		"Lime",
		"Pink",
		"Gray",
		"Light Gray",
		"Cyan",
		"Purple",
		"Blue",
		"Brown",
		"Green",
		"Red",
		"Black"
	};
	
	
	@SidedProxy(clientSide = "loecraftpack.proxies.ClientProxy", serverSide = "loecraftpack.proxies.CommonProxy")
    public static CommonProxy proxy;
	
	public static CreativeTabs LoECraftTab = new CreativeTabs("LoECraftTab")
	{
        public ItemStack getIconItemStack()
        {
                return new ItemStack(Item.writableBook, 1, 0);
        }
	};
	
	public static final Bits bits = new Bits(667);
	public static final ProtectionMonolithBlock monolith = new ProtectionMonolithBlock(666);
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event)
	{
		for(int i = 0; i < 16; i++)
		{
			bedItems[i] = new ItemBedColor(670+i);
			bedBlocks[i] = new BlockBedColor(670+i);
			bedItems[i].block = bedBlocks[i];
			bedBlocks[i].item = bedItems[i];
		}
    }
	
	@Init
	public void load(FMLInitializationEvent e)
	{
		LanguageRegistry.instance().addStringLocalization("itemGroup.LoECraftTab", "LoECraft");
		
		for(int i = 0; i < Bits.names.length; i++ )
			LanguageRegistry.instance().addStringLocalization("item.itemBits." + Bits.iconNames[i] + ".name", Bits.names[i]);
		
		GameRegistry.registerBlock(monolith, "ProtectionMonolithBlock");
		LanguageRegistry.addName(monolith, "Protection Monolith");
		
		GameRegistry.registerTileEntity(ProtectionMonolithTileEntity.class, "ProtectionMonolithTileEntity");
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		
		proxy.doProxyStuff();
		
		//Bed Items
		for(int i = 0; i < 16; i++)
		{
			LanguageRegistry.addName(bedItems[i].setUnlocalizedName("BedColor"+i), "Bed : " + colors[i]);
			LanguageRegistry.addName(bedBlocks[i], "Bed : " + colors[i]);
			GameRegistry.registerBlock(bedBlocks[i], "bed" + colors[i]);
		}
		
        ///Update Recipes
        
        //get CraftingManager
    	CraftingManager cmi = CraftingManager.getInstance();
    	
    	//locate and remove old bed recipe
    	List recipes = cmi.getRecipeList();
    	Iterator r = recipes.iterator();
    	while (r.hasNext())
    	{
    		//test if recipe creates a bed
    		IRecipe ir = (IRecipe)r.next();
			if( ir != null && ir.getRecipeOutput() != null && ir.getRecipeOutput().itemID == Item.bed.itemID )
			{
				//clear old recipe and move on
				r.remove();
				break;//there really should only be one vanilla bed to remove
			}
    	}
    	
    	//add new recipes to replace the old one
    	for (int i = 0; i < 16; i++)
    	{
    		cmi.addRecipe(new ItemStack(bedItems[i]), "###", "XXX", '#', new ItemStack(Block.cloth, 1, i), 'X', Block.planks);
    	}
        
      ///Colored-Beds END///
		
	}
	
	@PostInit
	public void postLoad(FMLPostInitializationEvent e)
	{
		List recipeList = new ArrayList();
		recipeList.addAll(CraftingManager.getInstance().getRecipeList());
		
		for(int i = 0; i < recipeList.size(); i++)
		{
			IRecipe recipe = (IRecipe)recipeList.get(i);
			ItemStack output = recipe.getRecipeOutput();
			if (output != null && output.itemID == 130)
				CraftingManager.getInstance().getRecipeList().remove(recipe);
		}
	}
}
