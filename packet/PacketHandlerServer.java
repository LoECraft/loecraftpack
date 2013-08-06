package loecraftpack.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.gui.GuiIds;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.mechanics.ModeHandler;
import loecraftpack.ponies.abilities.mechanics.Modes;
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
    public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player sender)
	{
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload.data));
            EntityPlayer player = (EntityPlayer) sender;
            
            try
            {
            	switch(data.readByte())
            	{
            		case PacketIds.useAbility:
            			CastSpellForPlayer(data.readInt(), sender, data);
            			break;
            			
            		case PacketIds.monolithEdit:
            			int x = data.readInt(),
            			    y = data.readInt(),
            			    z = data.readInt();
            			TileProtectionMonolith te = (TileProtectionMonolith)player.worldObj.getBlockTileEntity(x, y, z);
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
            				player.worldObj.updateTileEntityChunkAndDoNothing(x, y, z, te);
            				PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, x, y, z, te.width, te.length, te.offsetX, te.offsetZ, te.getOwners()), sender);
            			}
            			break;
            		case PacketIds.monolithSetOwner:
	            		x = data.readInt();
	        			y = data.readInt();
	        			z = data.readInt();
	        			te = (TileProtectionMonolith)player.worldObj.getBlockTileEntity(x, y, z);
	        			if (te != null)
	        				te.setOwners(PacketHelper.readString(data));
            			break;
            		case PacketIds.monolithUpdate:
            			x = data.readInt();
	        			y = data.readInt();
	        			z = data.readInt();
	        			te = (TileProtectionMonolith)player.worldObj.getBlockTileEntity(x, y, z);
	        			if (te != null)
	        				PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.monolithUpdate, x, y, z, te.width, te.length, te.offsetX, te.offsetZ, te.getOwners()), sender);
            			break;
            		case PacketIds.applyPotionEffect:
            			player.addPotionEffect(new PotionEffect((Integer)NetworkedPotions.potions.get(data.readByte()), data.readInt(), data.readByte()));
            			break;
            		case PacketIds.subInventory:
            			int guiId = data.readInt();
            			try
            			{
            				if (guiId < GuiIds.values().length)
            				{
            					GuiIds clientGui = GuiIds.values()[guiId];
            					if (HandlerExtendedInventoryServer.compareInvId(player, clientGui))
            					{
            						//set id for exception exception
            						guiId = HandlerExtendedInventoryServer.getNextInv(player, clientGui).ordinal();
            						if (player.capabilities.isCreativeMode && guiId==GuiIds.MAIN_INV.ordinal())
            						{
                    					player.openContainer = player.inventoryContainer;
                    					PacketDispatcher.sendPacketToPlayer(PacketHelper.Make("loecraftpack", PacketIds.subInventory),sender);
            						}
            						else
            						{
		            					player.openGui(LoECraftPack.instance,
		            							guiId,
		            			                MinecraftServer.getServer().worldServerForDimension(player.dimension),
		            			                (int)player.posX,
		            			                (int)player.posY,
		            			                (int)player.posZ);
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
            			ModeHandler.clearSuccess(player, Modes.values()[ability], state);
            	}
            }
            catch(IOException e){}
    }
	
	private void CastSpellForPlayer(int id, Player sender, DataInputStream data) throws IOException
	{
		AbilityPlayerData playerData = AbilityPlayerData.Get(((EntityPlayer)sender).username);
		
		playerData.activeAbilities[id].castSpellServerByHandler(sender, data);
		
		//Debug: the following is more stable, but... un-neccesary if the client and server are using compatible versions.
		/*
		String abilityName = ActiveAbility.abilityNames[id];
		
		for(ActiveAbility ability : playerData.activeAbilities) 
		{
			if (ability.name.equals(abilityName))
			{
				ability.castSpellServerVisable(sender, data);
				break;
			}
		}
		*/
	}
}
