package loecraftpack.common.logic;

import java.util.EnumSet;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.common.items.ItemAccessory;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.RenderHotBarOverlay;
import loecraftpack.ponies.abilities.mechanics.ModeHandler;
import loecraftpack.ponies.abilities.mechanics.MechanicHiddenOres;
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
	int autoEffectBuffer = 0;
	int autoEffectBufferMax = 20;//one sec
	
	int autoEffectBufferClient = 0;
	int autoEffectBufferClientMax = 20;//one sec
	
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
				        	
				        	if(autoEffectBuffer++==0)
							{
				        		//apply auto effect for accessories
					        	InventoryCustom inv = HandlerExtendedInventoryCommon.getInventory(player, InventoryId.EQUIPMENT);
								List<Integer> accessorySlotIds = HandlerExtendedInventoryCommon.getAccessorySlotIds(inv);
								if (accessorySlotIds!=null)
									for (Integer accessorySlotId : accessorySlotIds)
									{
										ItemStack accessory = inv.getStackInSlot(accessorySlotId);
										if (accessory != null)
											((ItemAccessory)accessory.getItem()).applyWornEffect(player, inv, accessorySlotId, accessory);
									}
								
								//apply energy Regen
								AbilityPlayerData data = Ability.map.get(player.username);
								data.setEnergy(data.energy+data.energyRegenNatural);
				        	}
							else if (autoEffectBuffer>=autoEffectBufferMax)
								autoEffectBuffer=0;
				        	
				    		if (Ability.map.containsKey(player.username))
				    		{
				    			for(Ability ability : Ability.map.get(player.username).abilities)
				    				ability.onUpdate(player);
				    		}
			        	}
			        	else //client
			        	{
			        		//clients player
			        		if (Minecraft.getMinecraft().thePlayer.entityId == player.entityId)
			        		{
				        		//hidden ore vision
				        		if (MechanicHiddenOres.revealHiddenGems)
					        	{
				        			
					        		if (player.isPotionActive(LoECraftPack.potionOreVision))
					        		{
					        			//update render?
					        			if (MechanicHiddenOres.bootUp)
					        			{
					        				MechanicHiddenOres.bootUp = false;
					        				MechanicHiddenOres.refreshRenderWithRange(player);
					        			}
					        			else if (MechanicHiddenOres.xPos != (int) player.posX ||
				        						 MechanicHiddenOres.yPos != (int) player.posY ||
				        						 MechanicHiddenOres.zPos != (int) player.posZ )
					        			{
					        				MechanicHiddenOres.refreshRenderWithRange(player);
					        			}
					        		}
					        		else
					        		{
					        			MechanicHiddenOres.bootUp = true;
					        			MechanicHiddenOres.revealHiddenGems = false;
					        			MechanicHiddenOres.refreshRenderWithRange(player);
					        		}
					        	}//hidden ore vision
			        		}//clients player
			        		
			    			for(Ability ability : Ability.abilitiesClient)
			    			{
			    				ability.onUpdate(player);
			    			}
			    			
			    			if(autoEffectBufferClient++==0)
							{
								//apply energy Regen
								Ability.setEnergy(Ability.energyClient + Ability.energyRegenNatural);
				        	}
			    			else if (autoEffectBufferClient>=autoEffectBufferClientMax)
								autoEffectBufferClient=0;
			    			
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
