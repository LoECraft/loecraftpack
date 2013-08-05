package loecraftpack.common.logic;

import java.util.EnumSet;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.common.items.ItemAccessory;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.PassiveAbility;
import loecraftpack.ponies.abilities.RenderHotBarOverlay;
import loecraftpack.ponies.abilities.mechanics.ModeHandler;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryCommon;
import loecraftpack.ponies.inventory.InventoryCustom;
import loecraftpack.ponies.inventory.InventoryId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class HandlerTick implements ITickHandler
{
	//used for server ticks
	int autoEffectBufferS = 0;
	int autoEffectBufferC = 0;
	final int autoEffectBufferMax = 20;//one sec
	
	//randomly set falling value, that discerns when changingPlayerStat updates occur.
	int changingPlayerStatUpdateDelay = 100;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}
	
	@Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
		if (type.contains(TickType.PLAYER))
		{
			if (tickData != null)
			{
				for (Object entry : tickData)
				{
			        if (entry instanceof EntityPlayer)
			        {
			        	EntityPlayer player = (EntityPlayer)entry;
			        	
			        	if (!player.worldObj.isRemote)//server
			        	{
				        	//check if player is fallen thru the bottom of skyland.
				        	if (player.dimension == LoECraftPack.SkylandDimensionID && player.posY<-0.1)
				        	{
				        		System.out.println("dimension "+player.worldObj.getWorldInfo().getDimension());
				        		//TeleporterCustom.refreshTeleporter(TeleporterCustom.teleporterSkyLandsFalling, 0);
								//TeleporterCustom.teleporterSkyLandsFalling.travelToDimension(player);
				        	}
				        	else if (player.dimension == 0 && player.posY > player.worldObj.getHeight()-0.5)
				        	{
				        		System.out.println("dimension "+player.worldObj.getWorldInfo().getDimension());
				        		//TeleporterCustom.refreshTeleporter(TeleporterCustom.teleporterSkyLandsRising, LoECraftPack.SkylandDimensionID);
								//TeleporterCustom.teleporterSkyLandsRising.travelToDimension(player);
				        	}
				        	
				        	if(autoEffectBufferS++ >= autoEffectBufferMax)
							{
				        		autoEffectBufferS = 0;
				        		//apply auto effect for accessories
					        	InventoryCustom inv = HandlerExtendedInventoryCommon.getInventory(player, InventoryId.EQUIPMENT);
								List<Integer> accessorySlotIds = HandlerExtendedInventoryCommon.getAccessorySlotIds(inv);
								if (accessorySlotIds!=null)
								{
									for (Integer accessorySlotId : accessorySlotIds)
									{
										ItemStack accessory = inv.getStackInSlot(accessorySlotId);
										if (accessory != null)
											((ItemAccessory)accessory.getItem()).applyWornEffect(player, inv, accessorySlotId, accessory);
									}
								}
				        	}
				        	
				        	AbilityPlayerData data = AbilityPlayerData.Get(player.username);
				        	if (data != null)
				        	{
				        		data.onUpdateSERVER(player);
				        		
				        		if (0==--changingPlayerStatUpdateDelay)
				    			{
				    				data.sendChangingPlayerStatPacket();
				    				changingPlayerStatUpdateDelay = 1000;
				    			}
				        	}
			        	}
			        	else //client
			        	{
			        		//clients player
			        		if (Minecraft.getMinecraft().thePlayer.entityId == player.entityId)
			        		{
			        			//empty for now
			        		}//clients player
			        		
			        		
			        		AbilityPlayerData data = AbilityPlayerData.clientData;
			    			for(ActiveAbility ability : data.activeAbilities)
			    				ability.onUpdate(player);
			    			
			    			for(PassiveAbility ability : data.passiveAbilities)
			    				ability.onTick(player);
			    			
			    			if(autoEffectBufferC++ >= autoEffectBufferMax)
							{
			    				autoEffectBufferC = 0;
				        	}
			    			
			    			data.onUpdateCLIENT(player);
			    			
			    			
			        	}//client side
			        }//entry instanceof EntityPlayer
				}//Object entry : tickData
			}//tickData != null
		}
		if (type.contains(TickType.SERVER))
		{
			//sync ability mode changes
			ModeHandler.retryAllRemaining();
		}
		if (type.contains(TickType.RENDER) && Minecraft.getMinecraft().theWorld != null && (Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen instanceof GuiChat))
			RenderHotBarOverlay.instance.renderHotBarOverlay(type, tickData);
    }
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER, TickType.SERVER, TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "LoeCraftPackTickHandler";
	}

}
