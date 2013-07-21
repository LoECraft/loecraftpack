package loecraftpack.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.AbilityList;
import loecraftpack.ponies.abilities.mechanics.AbilityModeHandler;
import loecraftpack.ponies.abilities.projectiles.Fireball;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
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
            		case PacketIds.useAbility:
            			switch(data.readByte())
            			{
            				case AbilityList.Teleport:
            					sender.setPositionAndUpdate(data.readDouble(), data.readDouble(), data.readDouble());
            					break;
            			}
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
            			try
            			{
            				if (guiId < GuiIds.values().length)
            				{
            					GuiIds clientGui = GuiIds.values()[guiId];
            					if (HandlerExtendedInventoryServer.compareInvId(sender, clientGui))
            					{
            						//set id for exception exception
            						guiId = HandlerExtendedInventoryServer.getNextInv(sender, clientGui).ordinal();
            						if (sender.capabilities.isCreativeMode && guiId==GuiIds.MAIN_INV.ordinal())
            						{
                    					sender.openContainer = sender.inventoryContainer;
                    					PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.subInventory),player);
            						}
            						else
            						{
		            					sender.openGui(LoECraftPack.instance,
		            							guiId,
		            			                MinecraftServer.getServer().worldServerForDimension(sender.dimension),
		            			                (int)sender.posX,
		            			                (int)sender.posY,
		            			                (int)sender.posZ);
            						}
            					}
            				}
            			}
            			catch(IllegalArgumentException e)
            			{
            				//this allows the exception for reloading crafting inventory without closing it first, to be ignored.
            				//currently only one crafting inventory that applies.
            				if (! (guiId == GuiIds.MAIN_INV.ordinal()))
            					throw e;
            			}
            			break;
            		case PacketIds.modeAbility:
            			int ability = data.readInt();
            			int state = data.readInt();
            			AbilityModeHandler.clearSuccess(sender, Ability.values()[ability], state);
            	}
            }
            catch(IOException e){}
    }
}
