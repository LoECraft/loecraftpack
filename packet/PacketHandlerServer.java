package loecraftpack.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.ponies.abilities.projectiles.Fireball;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerServer implements IPacketHandler
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
            			
            		case PacketIds.monolithEdit:
            			int x = data.readInt(),
            			    y = data.readInt(),
            			    z = data.readInt();
            			TileProtectionMonolith te = (TileProtectionMonolith)sender.worldObj.getBlockTileEntity(x, y, z);
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
	        			te = (TileProtectionMonolith)sender.worldObj.getBlockTileEntity(x, y, z);
	        			if (te != null)
	        				te.setOwners(PacketHelper.readString(data));
            			break;
            		case PacketIds.monolithUpdate:
            			x = data.readInt();
	        			y = data.readInt();
	        			z = data.readInt();
	        			te = (TileProtectionMonolith)sender.worldObj.getBlockTileEntity(x, y, z);
	        			if (te != null)
	        				PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, x, y, z, te.width, te.length, te.offsetX, te.offsetZ, te.getOwners()), player);
            			break;
            		case PacketIds.applyPotionEffect:
            			sender.addPotionEffect(new PotionEffect((Integer)NetworkedPotions.potions.get(data.readByte()), data.readInt(), data.readByte()));
            			break;
            		case PacketIds.subInventory:
            			int guiId = data.readInt();
            			EntityPlayer ePlayer = (EntityPlayer)player;
            			try
            			{
            			ePlayer.openGui(LoECraftPack.instance,
            			                guiId,
            			                MinecraftServer.getServer().worldServerForDimension(ePlayer.dimension),
            			                (int)ePlayer.posX,
            			                (int)ePlayer.posY,
            			                (int)ePlayer.posZ);
            			}
            			catch(IllegalArgumentException e)
            			{
            				//this allows the exception for reloading crafting inventory without closing it first, to be ignored.
            				//currently only one crafting inventory that applies.
            				if (guiId != GuiIds.mainInv.ordinal())
            					throw e;
            			}
            			break;
            	}
            }
            catch(IOException e){}
    }
}
