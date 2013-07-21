package loecraftpack.proxies;

import net.minecraft.server.MinecraftServer;

public class CommonProxy
{
	public void doProxyStuff()
	{
		
	}
	
	public void doProxyStuffPost()
	{
	}
	
	public boolean isSinglePlayer()
	{
		return !MinecraftServer.getServer().isDedicatedServer();
	}
}
