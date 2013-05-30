package loecraftpack.proxies;

import net.minecraft.client.Minecraft;
import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.blocks.render.RenderColoredBed;
import loecraftpack.common.blocks.render.RenderHiddenOre;
import loecraftpack.common.blocks.render.RenderProtectionMonolith;
import loecraftpack.common.entity.EntityElectricBlock;
import loecraftpack.common.entity.EntityTimberWolf;
import loecraftpack.common.entity.render.ModelTimberWolf;
import loecraftpack.common.entity.render.RenderElectricBlock;
import loecraftpack.common.entity.render.RenderTimberWolf;
import loecraftpack.common.logic.HandlerKey;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy
{
	public static RenderElectricBlock renderEleBlock;
	public static RenderHiddenOre renderHiddenOre;
	
	@Override
	public void doProxyStuff()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileProtectionMonolith.class, new RenderProtectionMonolith());
		KeyBindingRegistry.registerKeyBinding(new HandlerKey());
		RenderColoredBed cbr = new RenderColoredBed();
		RenderingRegistry.registerBlockHandler(cbr.renderID = RenderingRegistry.getNextAvailableRenderId(), (ISimpleBlockRenderingHandler)cbr);
		LoECraftPack.bedBlock.renderID = cbr.renderID;
		renderHiddenOre = new RenderHiddenOre();
		RenderingRegistry.registerBlockHandler(renderHiddenOre.renderID = RenderingRegistry.getNextAvailableRenderId(), (ISimpleBlockRenderingHandler)renderHiddenOre);
		LoECraftPack.blockGemOre.renderID = renderHiddenOre.renderID;
		RenderingRegistry.registerEntityRenderingHandler(EntityElectricBlock.class, renderEleBlock = new RenderElectricBlock());
		RenderingRegistry.registerEntityRenderingHandler(EntityTimberWolf.class, new RenderTimberWolf(new ModelTimberWolf(), new ModelTimberWolf(), 0.5F));
	}
	
	public void doProxyStuffPost()
	{
		Minecraft.getMinecraft().gameSettings.keyBindJump = HandlerKey.jump; //KeysHandler overrides the default jump keybind, which disables jumping. This gets around that.
	}
}
