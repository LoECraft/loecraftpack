package loecraftpack.proxies;

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
	@Override
	public void doProxyStuff()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileProtectionMonolith.class, new RenderProtectionMonolith());
		KeyBindingRegistry.registerKeyBinding(new HandlerKey());
		RenderColoredBed cbr = new RenderColoredBed();
		RenderingRegistry.registerBlockHandler(cbr.renderID = RenderingRegistry.getNextAvailableRenderId(), (ISimpleBlockRenderingHandler)cbr);
		LoECraftPack.bedBlock.renderID = cbr.renderID;
		RenderHiddenOre hor = new RenderHiddenOre();
		RenderingRegistry.registerBlockHandler(hor.renderID = RenderingRegistry.getNextAvailableRenderId(), (ISimpleBlockRenderingHandler)hor);
		LoECraftPack.blockGemOre.renderID = hor.renderID;
		RenderingRegistry.registerEntityRenderingHandler(EntityElectricBlock.class, new RenderElectricBlock());
		RenderingRegistry.registerEntityRenderingHandler(EntityTimberWolf.class, new RenderTimberWolf(new ModelTimberWolf(), new ModelTimberWolf(), 0.5F));
	}
}
