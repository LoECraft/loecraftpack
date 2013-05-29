package loecraftpack.common.logic;

import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.BlockProtectionMonolith;
import loecraftpack.common.blocks.TileProtectionMonolith;
import loecraftpack.common.items.ItemAccessory;
import loecraftpack.dimensionaltransfer.TeleporterCustom;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.mechanics.MechanicTreeBucking;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryCommon;
import loecraftpack.ponies.inventory.InventoryCustom;
import loecraftpack.ponies.inventory.InventoryId;
import loecraftpack.proxies.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
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

		List<TileProtectionMonolith> list = BlockProtectionMonolith.monoliths.get(event.entityPlayer.worldObj.getWorldInfo().getDimension());
		if (list != null)
		for(TileProtectionMonolith te : list)
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

		List<TileProtectionMonolith> list = BlockProtectionMonolith.monoliths.get(event.entityPlayer.worldObj.getWorldInfo().getDimension());
		if (list != null)
		for(TileProtectionMonolith te : list)
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
	
	
	@ForgeSubscribe
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		EntityPlayer entityPlayer = event.entityPlayer;
		System.out.print("click - ");
		//test code
		if (entityPlayer.getHeldItem() != null)
		{
			if (entityPlayer.getHeldItem().itemID == LoECraftPack.itemPickaxeGem.itemID)
			{
				if (event.action == Action.RIGHT_CLICK_AIR)
				{
					if (!entityPlayer.worldObj.isRemote)
					{
						PotionEffect effect = entityPlayer.getActivePotionEffect(LoECraftPack.potionOreVision);
						if (effect!= null)
						{
							System.out.println("hi");
							
							entityPlayer.addPotionEffect(new PotionEffect(LoECraftPack.potionOreVision.id, 1200, effect.getAmplifier()+1));
						}
						else
							entityPlayer.addPotionEffect(new PotionEffect(LoECraftPack.potionOreVision.id, 1200, 0));
					}
				}
				if (event.action == Action.RIGHT_CLICK_BLOCK)
				{
					int countR = 0;
					int countC = 0;
					Chunk chunk = entityPlayer.worldObj.getChunkFromChunkCoords(entityPlayer.chunkCoordX, entityPlayer.chunkCoordZ);
					for (int y = 0; y<256; y++)
					{
						for (int x = 0; x<16; x++)
						{
							for (int z = 0; z<16; z++)
							{
								if (chunk.getBlockID(x, y, z)==LoECraftPack.blockGemOre.blockID)
								{
									if (chunk.getBlockMetadata(x, y, z)>7)
										countR++;
									else
										countC++;
								}
							}
						}
					}
					System.out.println("common ores: "+countC);
					System.out.println("rare   ores: "+countR);
				}
			}
			else if (entityPlayer.getHeldItem().itemID == Item.stick.itemID &&
					 entityPlayer.worldObj.getBlockId(event.x, event.y, event.z) == Block.beacon.blockID &&
					 event.action == Action.LEFT_CLICK_BLOCK)
			{
				TeleporterCustom.refreshTeleporter(TeleporterCustom.teleporterSkyLands, LoECraftPack.SkylandDimensionID);
				TeleporterCustom.teleporterSkyLands.travelToDimension(event.entityPlayer);
				event.setResult(Result.DENY);
			}
		}
		System.out.println();
	}
	
	
	/*
	 * NOTE (WorldGenMinable):
	 * the value number is actually 2 greater than the amount that can be generated in a cluster
	 * so compensate for that.
	 */
	@ForgeSubscribe
	public void onDecorateWorldPre(DecorateBiomeEvent.Pre event)
	{
		BiomeGenBase biome = event.world.getBiomeGenForCoords(event.chunkX, event.chunkZ);
		
		if(biome.biomeID != BiomeGenBase.hell.biomeID)
		{
			//rare - lower elevation
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 10, 3, Block.stone.blockID), 17,  40);//Laughter
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 11, 3, Block.stone.blockID), 17,  40);//Generosity
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 12, 3, Block.stone.blockID), 17,  40);//Kindness
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 13, 3, Block.stone.blockID), 17,  40);//Magic
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 14, 3, Block.stone.blockID), 17,  40);//Loyalty
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 15, 3, Block.stone.blockID), 17,  40);//Honesty
			
			//rare - higher elevation
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 10, 3, Block.stone.blockID), 41, 128);//Laughter
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 11, 3, Block.stone.blockID), 41, 128);//Generosity
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 12, 3, Block.stone.blockID), 41, 128);//Kindness
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 13, 3, Block.stone.blockID), 41, 128);//Magic
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 14, 3, Block.stone.blockID), 41, 128);//Loyalty
			generateOre(1, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID, 15, 3, Block.stone.blockID), 41, 128);//Honesty
			
			//rare - general
			generateOre(3, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  9, 3, Block.stone.blockID), 17, 128);//Heart
			
		}
	}
	
	@ForgeSubscribe
	public void onDecorateWorldPost(DecorateBiomeEvent.Post event)
	{
		BiomeGenBase biome = event.world.getBiomeGenForCoords(event.chunkX, event.chunkZ);
		
		if(biome.biomeID != BiomeGenBase.hell.biomeID)
		{
			//Common
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  0, 6, Block.stone.blockID),  5, 128);//Sapphire
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  1, 4, Block.stone.blockID),  5, 128);//Fire Ruby
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  2, 4, Block.stone.blockID),  5, 128);
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  3, 4, Block.stone.blockID),  5, 128);
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  4, 4, Block.stone.blockID),  5, 128);
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  5, 4, Block.stone.blockID),  5, 128);
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  6, 4, Block.stone.blockID),  5, 128);
			generateOre(7, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  7, 6, Block.stone.blockID),  5, 128);//Onyx
			
			//Tom
			generateOre(3, event, new WorldGenMinable(LoECraftPack.blockGemOre.blockID,  8, 5, Block.stone.blockID),  5,  16);//Tom
			
			//random apple blooms
			if (biome.biomeID != 0 && biome.biomeID != BiomeGenBase.desert.biomeID && biome.biomeID != BiomeGenBase.frozenOcean.biomeID)
			{
				if (event.rand.nextInt(20)==0)
		        {
		            int k = event.chunkX + event.rand.nextInt(16) + 8;
		            int l = event.chunkZ + event.rand.nextInt(16) + 8;
		            WorldGenerator worldgenerator = LoECraftPack.worldGeneratorAppleBloom;
		            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
		            worldgenerator.generate(event.world, event.rand, k, event.world.getHeightValue(k, l), l);
		        }
			}
		}
	}
	
	protected void generateOre(int sets, DecorateBiomeEvent event, WorldGenerator worldGenerator, int minHeight, int maxHeight)
	{
		for(int i=0; i<sets; i++)
		{
			int x = event.chunkX + event.rand.nextInt(16);
	        int y = event.rand.nextInt(maxHeight - minHeight) + minHeight;
	        int z = event.chunkZ + event.rand.nextInt(16);
	        worldGenerator.generate(event.world, event.rand, x, y, z);
		}
	}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		TeleporterCustom.buildTeleporters(event.world);
	}
	
	@ForgeSubscribe
	public void onWorldUnload(WorldEvent.Unload event)
	{
		TeleporterCustom.clearTeleporters(event.world);
	}
	
	@ForgeSubscribe
	public void onfinalRender(RenderWorldLastEvent event)
	{
        ClientProxy.renderHiddenOre.drawBlockPhantomTexture(event);
	}
	
	@ForgeSubscribe
	public void onDeathEvent(LivingDeathEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			InventoryCustom inv = HandlerExtendedInventoryCommon.getInventory(player, InventoryId.Equipment);
			List<Integer> accessorySlotIds = HandlerExtendedInventoryCommon.getAccessorySlotIds(player, inv);
			if (accessorySlotIds!=null)
				for (Integer accessorySlotId : accessorySlotIds)
				{
					ItemStack accessory = inv.getStackInSlot(accessorySlotId);
					if (accessory != null)
						((ItemAccessory)accessory.getItem()).onDeath(event, player, inv, accessorySlotId, accessory);
				}
		}
	}
	
	
}
