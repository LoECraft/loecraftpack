package loecraftpack.ponies.abilities;

import loecraftpack.LoECraftPack;
import loecraftpack.enums.Race;
import loecraftpack.ponies.abilities.mechanics.MechanicAbilityCharge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
//Do: ItemAbility - CREATE CHARGE GRAPHIC
public abstract class ItemAbility extends Item
{
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
		this.setMaxDamage(101);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	public ItemAbility(int par1, int cooldown)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        Cooldown = cooldown;
	}
	
	public ItemAbility(int par1, int cooldown, int casttime)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(100);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
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
		
		held = true;
		time = System.currentTimeMillis();
		
		if (cooldown <= 0)
		{
			if (casttime >= Casttime)
			{
				if (CastSpell(par3EntityPlayer, par2World))
				{
					cooldown = Cooldown;
					casttime = 0;
					MechanicAbilityCharge.setCharge(par3EntityPlayer, 0);
					setDamage(par1ItemStack, (int)((1-cooldown/Cooldown)*100));
				}
			}
			else if (!par2World.isRemote)
			{
				casttime += 0.25f;
				MechanicAbilityCharge.charge(par3EntityPlayer, casttime, Casttime);
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
		
		if (cooldown > 0)
		{
			setDamage(par1ItemStack, (int)((cooldown/Cooldown)*100));
			if (!par2World.isRemote)
				cooldown -= 0.05f;
		}
		else if (casttime > 0 && casttime < Casttime)
		{
			setDamage(par1ItemStack, (int)((1-casttime/Casttime)*100));
		}
		else
			setDamage(par1ItemStack, 0);
		
		
		if (time != lastTime || System.currentTimeMillis() - lastTime > 250)
		{
			lastTime = time;
			if (held)
			{
				heldChanged = true;
			}
			else
			{
				if (heldChanged)
				{
					casttime = 0;
					MechanicAbilityCharge.setCharge((EntityPlayer)par3Entity, 0);
					if (cooldown <= 0)
						setDamage(par1ItemStack, 0);
				}
				
				heldChanged = false;
			}
			held = false;
		}
	}
	
	protected abstract boolean CastSpell(EntityPlayer player, World world);
}
