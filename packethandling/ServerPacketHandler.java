package loecraftpack.packethandling;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.blocks.te.ColoredBedTileEntity;
import loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import loecraftpack.ponies.spells.projectiles.Fireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler
{
	@Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player player)
	{
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload.data));
            EntityPlayer sender = (EntityPlayer) player;
            
            try
            {
            	switch(data.readByte())
            	{
            		case PacketIds.fireball:
            			Fireball fireball = new Fireball(sender.worldObj, sender, sender.getLookVec().xCoord/10f, sender.getLookVec().yCoord/10f, sender.getLookVec().zCoord/10f);
            			sender.worldObj.spawnEntityInWorld(fireball);
            			break;
            			
            		case PacketIds.bedEdit:
            			int x = data.readInt(),
        			    	y = data.readInt(),
        			    	z = data.readInt();
            			ColoredBedTileEntity cb = (ColoredBedTileEntity)sender.worldObj.getBlockTileEntity(x, y, z);
            			if (cb != null)
            			{
            				int i = data.readInt();
            				int s = data.readInt();
            				cb.pairID = i;
            				cb.pairSide = s;
            				System.out.println("client bed update: " + cb.worldObj.isRemote + "  ID:"+ cb.id);
            			}
            			break;
            		case PacketIds.monolithEdit:
            				x = data.readInt();
            			    y = data.readInt();
            			    z = data.readInt();
            			ProtectionMonolithTileEntity te = (ProtectionMonolithTileEntity)sender.worldObj.getBlockTileEntity(x, y, z);
            			if (te != null)
            			{
            				int w = data.readInt();
            				int l = data.readInt();
            				int ox = data.readInt();
            				int oz = data.readInt();
            				te.width = w;
            				te.length = l;
            				te.offsetX = ox;
            				te.offsetZ = oz;
            				if (data.available() > 0)
            					te.setOwners(PacketHelper.readString(data));
            				sender.worldObj.updateTileEntityChunkAndDoNothing(x, y, z, te);
            				PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, x, y, z, te.width, te.length, te.offsetX, te.offsetZ, te.getOwners()), player);
            			}
            			break;
            		case PacketIds.monolithSetOwner:
	            		x = data.readInt();
	        			y = data.readInt();
	        			z = data.readInt();
	        			te = (ProtectionMonolithTileEntity)sender.worldObj.getBlockTileEntity(x, y, z);
	        			if (te != null)
	        				te.setOwners(PacketHelper.readString(data));
            			break;
            		case PacketIds.monolithUpdate:
            			x = data.readInt();
	        			y = data.readInt();
	        			z = data.readInt();
	        			te = (ProtectionMonolithTileEntity)sender.worldObj.getBlockTileEntity(x, y, z);
	        			if (te != null)
	        				PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, x, y, z, te.width, te.length, te.offsetX, te.offsetZ, te.getOwners()), player);
            			break;
            	}
            }
            catch(IOException e){}
    }
}
