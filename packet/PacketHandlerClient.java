package loecraftpack.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.TileColoredBed;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.enums.Race;
import loecraftpack.ponies.stats.StatHandlerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerClient implements IPacketHandler
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
	        			TileProtectionMonolith te = (TileProtectionMonolith)Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);
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
            			TileColoredBed cb = (TileColoredBed)Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);
            			if (cb != null)
            			{
            				int i = data.readInt();
            				int s = data.readInt();
            				cb.pairID = i;
            				cb.pairSide = s;
            			}
            			break;
            		case PacketIds.appleBloomUpdate:
            			x = data.readInt();
    			    	y = data.readInt();
    			    	z = data.readInt();
    			    	int newID = data.readInt();
    			    	Minecraft.getMinecraft().theWorld.setBlockAndMetadataAndInvalidate(x, y, z, newID, 0);
    			    	break;
            		case PacketIds.subInventory:
            			Minecraft.getMinecraft().displayGuiScreen(new GuiContainerCreative(Minecraft.getMinecraft().thePlayer));
            			break;
            		case PacketIds.applyStats:
            			int race = data.readInt();
            			String username = PacketHelper.readString(data);
            			StatHandlerClient.updatePlayerData(username, Race.values()[race]);
            			break;
            	}
            }
            catch(IOException e){}
    }
}
