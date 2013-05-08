package loecraftpack.common.logic;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.BlockProtectionMonolith;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.mechanics.MechanicTreeBucking;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.network.PacketDispatcher;

public class HandlerEvent
{
	/*@ForgeSubscribe
	public void onJump(LivingJumpEvent event)
	{
		event.entityLiving.motionY += 1;
	}*/
	
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
		for(TileProtectionMonolith te : BlockProtectionMonolith.monoliths)
		{
			if (te.xCoord == event.x && te.yCoord == event.y && te.zCoord == event.z)
				PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, event.x, event.y, event.z));
			if (te.Owners.size() > 0)
			{
				if (!te.Owners.contains(event.entityPlayer.username) && te.pointIsProtected(x, z))
				{
					//event.entityPlayer.skinUrl = "http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes("Tekner") + ".png";
					//Minecraft.getMinecraft().renderEngine.obtainImageData(event.entityPlayer.skinUrl, new ImageBufferDownload());
					//event.setCanceled(true);
					
					event.entityPlayer.setMoveForward(5);
				}
			}
			else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getDistanceSq(te.xCoord, te.yCoord, te.zCoord) <= 100)
			{
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
		for(TileProtectionMonolith te : BlockProtectionMonolith.monoliths)
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
				LogicDialog.AddMessage(event.message.substring(1).replaceAll("\n", "\n").split("\\`"));
			}
			else if (event.message.equals("next") || event.message.equals("done") || event.message.equals("accept") || event.message.equals("decline"))
				event.setCanceled(true);
		}
	}
	
	@ForgeSubscribe
	public void onBonemeal(BonemealEvent event)
	{
		if (!event.world.isRemote)
		{
			//event use on Apple-Bloom Sapling
			if (event.world.getBlockId(event.X, event.Y, event.Z) == LoECraftPack.blockAppleBloomSapling.blockID)
			{
			    if ((double)event.world.rand.nextFloat() < 0.45D)
			    {
			    	LoECraftPack.blockAppleBloomSapling.grow(event.world, event.X, event.Y, event.Z, event.world.rand);
			    }
			    event.setResult(Result.ALLOW);
			}
			
			//event use on Zap-Apple Sapling
			if (event.world.getBlockId(event.X, event.Y, event.Z) == LoECraftPack.blockZapAppleSapling.blockID)
			{
			    if ((double)event.world.rand.nextFloat() < 0.45D)
			    {
			    	LoECraftPack.blockZapAppleSapling.grow(event.world, event.X, event.Y, event.Z, event.world.rand);
			    }
			    event.setResult(Result.ALLOW);
			}
			
			//testing event: use on AppleLogs
			if (event.world.getBlockId(event.X, event.Y, event.Z) == LoECraftPack.blockZapAppleLog.blockID ||
				event.world.getBlockId(event.X, event.Y, event.Z) == LoECraftPack.blockAppleBloomLog.blockID)
			{
				System.out.println("BUCK");
				MechanicTreeBucking.buckTree(event.world, event.X, event.Y, event.Z, 0/*fortune*/);
			    event.setResult(Result.ALLOW);
			}
		}
	}
}
