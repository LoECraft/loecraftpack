package loecraftpack.packethandling;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.LoECraftPack;
import loecraftpack.blocks.te.ColoredBedTileEntity;
import loecraftpack.blocks.te.ProtectionMonolithTileEntity;


import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler
{
	@Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player player)
	{
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload.data));
            
            try
            {
            	switch(data.readByte())
            	{
            		case PacketIds.monolithUpdate:
            			int x = data.readInt(),
        			    y = data.readInt(),
        			    z = data.readInt();
	        			ProtectionMonolithTileEntity te = (ProtectionMonolithTileEntity)Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);
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
	        				te.setOwners(PacketHelper.readString(data));
	        			}
            			break;
            		case PacketIds.bedUpdate:
        			 	x = data.readInt();
    			    	y = data.readInt();
    			    	z = data.readInt();
            			ColoredBedTileEntity cb = (ColoredBedTileEntity)Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);
            			if (cb != null)
            			{
            				int i = data.readInt();
            				int s = data.readInt();
            				cb.pairID = i;
            				cb.pairSide = s;
            			}
            			break;
            		case PacketIds.zapAppleUpdate:
            			x = data.readInt();
    			    	y = data.readInt();
    			    	z = data.readInt();
    			    	int newID = data.readInt();
    			    	Minecraft.getMinecraft().theWorld.setBlockAndMetadataAndInvalidate(x, y, z, newID, 0);
    			    	break;
            	}
            }
            catch(IOException e){}
    }
}
