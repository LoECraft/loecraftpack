package loecraftpack.proxies;

import loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import loecraftpack.blocks.te.rendering.ColoredBedRenderer;
import loecraftpack.blocks.te.rendering.ProtectionMonolithRenderer;
import loecraftpack.logic.handlers.LoECraftKeyHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy
{
	@Override
	public void doProxyStuff()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(ProtectionMonolithTileEntity.class, new ProtectionMonolithRenderer());
		KeyBindingRegistry.registerKeyBinding(new LoECraftKeyHandler());
		ColoredBedRenderer cbr = new ColoredBedRenderer();
		RenderingRegistry.registerBlockHandler(cbr.RenderID = RenderingRegistry.getNextAvailableRenderId(), (ISimpleBlockRenderingHandler)cbr);
		
	}
}
