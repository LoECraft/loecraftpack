package loecraftpack.common.items;

import java.util.List;

import loecraftpack.enums.LivingEventId;
import loecraftpack.ponies.inventory.HandlerExtendedInventoryCommon;
import loecraftpack.ponies.inventory.InventoryCustom;
import loecraftpack.ponies.inventory.InventoryId;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public abstract class ItemAccessory extends Item {

	public ItemAccessory(int par1) {
		super(par1);
		this.setMaxStackSize(1);
	}
	
	public static void applyLivingEvent(LivingEvent event, LivingEventId methodId)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			InventoryCustom inv = HandlerExtendedInventoryCommon.getInventory(player, InventoryId.Equipment);
			List<Integer> accessorySlotIds = HandlerExtendedInventoryCommon.getAccessorySlotIds(inv);
			if (accessorySlotIds!=null)
				switch(methodId)
				{
				case LivingFall:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onFall((LivingFallEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case LivingAttack:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onAttacked((LivingAttackEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case LivingHurt:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onDamaged((LivingHurtEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case LivingDeath:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onDeath((LivingDeathEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case LivingSpawn:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onSpawn((LivingSpawnEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case EntityItemPickup:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onPickup((EntityItemPickupEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case PlayerSleepInBed:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onSleep((PlayerSleepInBedEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case EntityInteract:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onSocialize((EntityInteractEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case AttackEntity:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onBrutalize((AttackEntityEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case UseHoe:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onHoe((UseHoeEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case Bonemeal:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onGreenThumb((BonemealEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case FillBucket:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onBucket((FillBucketEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case PlayerDestroyItem:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onBreakItem((PlayerDestroyItemEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case PlayerFlyableFall:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onGentleFall((PlayerFlyableFallEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case PlayerDrops:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onDrops((PlayerDropsEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case ArrowLoose:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onArrowLoose((ArrowLooseEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				case ArrowNock:
					for (Integer accessorySlotId : accessorySlotIds)
					{
						ItemStack accessory = inv.getStackInSlot(accessorySlotId);
						((ItemAccessory)accessory.getItem()).onArrowNock((ArrowNockEvent)event, player, inv, accessorySlotId, accessory);
					}
					break;
				}
		}
	}
	
	

	/**
	 * periodic effect while wearing this accessory
	 */
	public void applyWornEffect(EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player lands on the ground, while wearing this accessory
	 */
	public void onFall(LivingFallEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player is attacked, while wearing this accessory
	 */
	public void onAttacked(LivingAttackEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player takes damage, while wearing this accessory
	 */
	public void onDamaged(LivingHurtEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player dies, while wearing this accessory
	 */
	public void onDeath(LivingDeathEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player spawns, while wearing this accessory
	 */
	public void onSpawn(LivingSpawnEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player picks up an item, while wearing this accessory
	 */
	public void onPickup(EntityItemPickupEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player sleeps in a bed, while wearing this accessory
	 */
	public void onSleep(PlayerSleepInBedEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player interacts with another entity, while wearing this accessory
	 */
	public void onSocialize(EntityInteractEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player interacts with another entity, while wearing this accessory
	 */
	public void onBrutalize(AttackEntityEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player uses a hoe, while wearing this accessory
	 */
	public void onHoe(UseHoeEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player uses bone-meal, while wearing this accessory
	 */
	public void onGreenThumb(BonemealEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	
	/**
	 * what, if anything, occurs when the player uses a bucket, while wearing this accessory
	 */
	public void onBucket(FillBucketEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	
	/**
	 * what, if anything, occurs when the player breaks an item, while wearing this accessory
	 */
	public void onBreakItem(PlayerDestroyItemEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	
	/**
	 * what, if anything, occurs when the player falls while able to fly, while wearing this accessory
	 */
	public void onGentleFall(PlayerFlyableFallEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	
	/**
	 * what, if anything, occurs when the player drops their inventory, while wearing this accessory
	 */
	public void onDrops(PlayerDropsEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	
	/**
	 * what, if anything, occurs when the player fires an arrow, while wearing this accessory
	 */
	public void onArrowLoose(ArrowLooseEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	
	/**
	 * what, if anything, occurs when the player loads an arrow, while wearing this accessory
	 */
	public void onArrowNock(ArrowNockEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack) {}
	

}
