package loecraftpack.common.items;

import loecraftpack.ponies.inventory.InventoryCustom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public abstract class ItemAccessory extends Item {

	public ItemAccessory(int par1) {
		super(par1);
	}
	
	/**
	 * periodic effect while wearing this accessory
	 */
	public void applyWornEffect(EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player lands on the ground while wearing this accessory
	 */
	public void onFall(LivingFallEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player is attacked while wearing this accessory
	 */
	public void onAttacked(LivingAttackEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player takes damage while wearing this accessory
	 */
	public void onDamaged(LivingHurtEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player dies while wearing this accessory
	 */
	public void onDeath(LivingDeathEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player dies while wearing this accessory
	 */
	public void onSpawn(LivingSpawnEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player picks up an item while wearing this accessory
	 */
	public void onPickup(EntityItemPickupEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player sleeps in a bed while wearing this accessory
	 */
	public void onSleep(PlayerSleepInBedEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player interacts with another entity while wearing this accessory
	 */
	public void onSocialize(EntityInteractEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player interacts with another entity while wearing this accessory
	 */
	public void onBrutalize(AttackEntityEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}
	
	/**
	 * what, if anything, occurs when the player uses a hoe while wearing this accessory
	 */
	public void onHoe(UseHoeEvent event, EntityPlayer player, InventoryCustom inv, int slot, ItemStack itemStack){}

}
