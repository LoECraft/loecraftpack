package tekner.loecraftpack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import tekner.loecraftpack.blocks.ProtectionMonolith;
import tekner.loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import tekner.loecraftpack.items.Bits;
import tekner.loecraftpack.logic.handlers.EventHandler;
import tekner.loecraftpack.logic.handlers.GuiHandler;
import tekner.loecraftpack.packethandling.CPacketHandler;
import tekner.loecraftpack.packethandling.SPacketHandler;
import tekner.loecraftpack.proxies.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@Mod(modid = "loecraftpack", name = "LoECraft Pack", version = "1.0")

@NetworkMod(clientSideRequired=true, serverSideRequired=false, clientPacketHandlerSpec = @SidedPacketHandler(channels = {"loecraftpack" }, packetHandler = CPacketHandler.class),
serverPacketHandlerSpec = @SidedPacketHandler(channels = {"loecraftpack" }, packetHandler = SPacketHandler.class))
public class LoECraftPack
{
	@Instance
    public static LoECraftPack instance = new LoECraftPack();
	
	@SidedProxy(clientSide = "tekner.loecraftpack.main.ClientProxy", serverSide = "tekner.loecraftpack.main.CommonProxy")
    public static CommonProxy proxy;
	
	public static CreativeTabs LoECraftTab = new CreativeTabs("LoECraftTab")
	{
        public ItemStack getIconItemStack() {
                return new ItemStack(Item.writableBook, 1, 0);
        }
	};
	
	public static final Bits bits = new Bits(667);
	public static final ProtectionMonolith monolith = new ProtectionMonolith(666);
	
	@Init
	public void load(FMLInitializationEvent e)
	{
		LanguageRegistry.instance().addStringLocalization("itemGroup.LoECraftTab", "en_US", "LoECraft");
		
		for(int i = 0; i < Bits.names.length; i++ )
			LanguageRegistry.instance().addStringLocalization("item.itemBits." + Bits.iconNames[i] + ".name", "en_US", Bits.names[i]);
		
		GameRegistry.registerBlock(monolith, "ProtectionMonolith");
		LanguageRegistry.addName(monolith, "Protection Monolith");
		
		GameRegistry.registerTileEntity(ProtectionMonolithTileEntity.class, "ProtectionMonolithTileEntity");
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		
		proxy.doProxyStuff();
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
