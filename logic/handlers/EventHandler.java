package loecraftpack.logic.handlers;

import loecraftpack.blocks.ProtectionMonolith;
import loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import loecraftpack.logic.DialogLogic;
import loecraftpack.packethandling.PacketHelper;
import loecraftpack.packethandling.PacketIds;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EventHandler
{
	@ForgeSubscribe
	public void onBlock(PlayerInteractEvent event)
	{
		int x = event.x, z = event.z;
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			switch(event.face)
			{
				case 2:
					z --;
					break;
				case 3:
					z ++;
					break;
				case 4:
					x --;
					break;
				case 5:
					x ++;
					break;
			}
		}
		for(ProtectionMonolithTileEntity te : ProtectionMonolith.monoliths)
		{
			if (te.xCoord == event.x && te.yCoord == event.y && te.zCoord == event.z)
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, event.x, event.y, event.z));
			if (te.Owners.size() > 0)
			{
				if (!te.Owners.contains(event.entityPlayer.username) && te.pointIsProtected(x, z))
				{
					//event.setCanceled(true);
				}
			}
			else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) <= 100)
			{
				System.out.println("Adding player");
				te.Owners.add(event.entityPlayer.username);
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.monolithSetOwner, te.xCoord, te.yCoord, te.zCoord, te.getOwners()));
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, event.x, event.y, event.z));
			}
		}
	}
	
	@ForgeSubscribe
	public void onBucket(FillBucketEvent event)
	{
		int x = event.target.blockX, z = event.target.blockZ;
		
		if (event.target.typeOfHit.ordinal() == 0)
		{
			switch(event.target.sideHit)
			{
				case 2:
					z --;
					break;
				case 3:
					z ++;
					break;
				case 4:
					x --;
					break;
				case 5:
					x ++;
					break;
			}
		}
		for(ProtectionMonolithTileEntity te : ProtectionMonolith.monoliths)
		{
			if (te.Owners.size() > 0)
			{
				if (!te.Owners.contains(event.entityPlayer.username) && te.pointIsProtected(x, z))
					event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void onChatReceived(ClientChatReceivedEvent event)
	{
		if (event.message.equals("npcchat"))
			event.setCanceled(true);
		else if (event.message.length() > 0)
		{
			if (event.message.substring(0, 1).equals("`"))
			{
				event.setCanceled(true);
				DialogLogic.AddMessage(event.message.substring(1).replaceAll("\n", "\n").split("\\`"));
			}
			else if (event.message.equals("next") || event.message.equals("done") || event.message.equals("accept") || event.message.equals("decline"))
				event.setCanceled(true);
		}
	}
}
