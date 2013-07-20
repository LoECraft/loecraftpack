package loecraftpack.ponies.abilities;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.mechanics.MechanicAbilityCharge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
//Do: ItemAbility - Change variables to arrays so that ItemAbilities can be one item via metadata
public abstract class ItemAbility extends Item
{
	public ItemAbility instance;
	protected int Cooldown = 0;
	protected float cooldown = 0;
	protected int Casttime = 0;
	protected float casttime = 0;
	private boolean held;
	private boolean heldChanged;
	private long time;
	private long lastTime;
	protected Race race;
	
	public ItemAbility(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        RenderHotBarOverlay.abilities.add(this);
	}
	
	public ItemAbility(int par1, int cooldown)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        RenderHotBarOverlay.abilities.add(this);
        Cooldown = cooldown;
	}
	
	public ItemAbility(int par1, int cooldown, int casttime)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        RenderHotBarOverlay.abilities.add(this);
        Cooldown = cooldown;
        Casttime = casttime;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!LoECraftPack.statHandler.isRace(par3EntityPlayer, race))
		{
			par1ItemStack.stackSize = 0;
			return par1ItemStack;
		}
		
		instance.held = true;
		instance.time = System.currentTimeMillis();
		
		if (instance.cooldown <= 0)
		{
			if (instance.casttime >= instance.Casttime)
			{
				if (CastSpell(par3EntityPlayer, par2World))
				{
					instance.cooldown = instance.Cooldown;
					instance.casttime = 0;
					MechanicAbilityCharge.setCharge(par3EntityPlayer, 0);
				}
			}
			else if (!par2World.isRemote)
			{
				instance.casttime += 0.25f;
				MechanicAbilityCharge.charge(par3EntityPlayer, instance.casttime, instance.Casttime);
			}
		}
		return par1ItemStack;
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (!LoECraftPack.statHandler.isRace((EntityPlayer)par3Entity, race))
		{
			par1ItemStack.stackSize = 0;
			return;
		}
		
		if (instance.cooldown > 0)
		{
			if (!par2World.isRemote)
				instance.cooldown -= 0.05f;
		}
		
		
		if (instance.time != instance.lastTime || System.currentTimeMillis() - instance.lastTime > 250)
		{
			instance.lastTime = instance.time;
			if (instance.held)
			{
				instance.heldChanged = true;
			}
			else
			{
				if (instance.heldChanged)
				{
					instance.casttime = 0;
					MechanicAbilityCharge.setCharge((EntityPlayer)par3Entity, 0);
				}
				
				instance.heldChanged = false;
			}
			instance.held = false;
		}
	}
	
	public float GetCooldown()
	{
		if (instance.Cooldown == 0)
			return 0;
		
		return instance.cooldown / instance.Cooldown;
	}
	
	protected abstract boolean CastSpell(EntityPlayer player, World world);
}
