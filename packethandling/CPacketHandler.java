package tekner.loecraftpack.packethandling;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import tekner.loecraftpack.blocks.te.ProtectionMonolithTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CPacketHandler implements IPacketHandler
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
            	}
            }
            catch(IOException e){}
    }
}
