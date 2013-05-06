package loecraftpack.proxies;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.blocks.render.RenderColoredBed;
import loecraftpack.common.blocks.render.RenderProtectionMonolith;
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
	}
}
