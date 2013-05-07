package loecraftpack.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ResultSlotProjectTable extends Slot
{
    private final TileProjectTable craftMatrix;

    private EntityPlayer thePlayer;

    private int amountCrafted;

    public ResultSlotProjectTable(EntityPlayer par1EntityPlayer, TileProjectTable par2IInventory, IInventory par3IInventory, int par4, int par5, int par6)
    {
        super(par3IInventory, par4, par5, par6);
        this.thePlayer = par1EntityPlayer;
        this.craftMatrix = par2IInventory;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    public ItemStack decrStackSize(int par1)
    {
        if (this.getHasStack())
        {
            this.amountCrafted += Math.min(par1, this.getStack().stackSize);
        }

        return super.decrStackSize(par1);
    }

    protected void onCrafting(ItemStack par1ItemStack, int par2)
    {
        this.amountCrafted += par2;
        this.onCrafting(par1ItemStack);
    }
    
    protected void onCrafting(ItemStack par1ItemStack)
    {
        par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        this.amountCrafted = 0;

        if (par1ItemStack.itemID == Block.workbench.blockID)
        {
            this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
        }
        else if (par1ItemStack.itemID == Item.pickaxeWood.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
        }
        else if (par1ItemStack.itemID == Block.furnaceIdle.blockID)
        {
            this.thePlayer.addStat(AchievementList.buildFurnace, 1);
        }
        else if (par1ItemStack.itemID == Item.hoeWood.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildHoe, 1);
        }
        else if (par1ItemStack.itemID == Item.bread.itemID)
        {
            this.thePlayer.addStat(AchievementList.makeBread, 1);
        }
        else if (par1ItemStack.itemID == Item.cake.itemID)
        {
            this.thePlayer.addStat(AchievementList.bakeCake, 1);
        }
        else if (par1ItemStack.itemID == Item.pickaxeStone.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        }
        else if (par1ItemStack.itemID == Item.swordWood.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildSword, 1);
        }
        else if (par1ItemStack.itemID == Block.enchantmentTable.blockID)
        {
            this.thePlayer.addStat(AchievementList.enchantments, 1);
        }
        else if (par1ItemStack.itemID == Block.bookShelf.blockID)
        {
            this.thePlayer.addStat(AchievementList.bookcase, 1);
        }
    }

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
    	GameRegistry.onItemCrafted(par1EntityPlayer, par2ItemStack, craftMatrix);
        this.onCrafting(par2ItemStack);
        
        int[][] reagents = new int[3][3];
        
        for(int i = 0; i < 3; i++)
        {
        	for(int ii = 0; ii < 3; ii++)
        	{
        		ItemStack itemStack = this.craftMatrix.getStackInSlot(i + ii*3);
        		if (itemStack != null)
        			reagents[i][ii] = itemStack.itemID;
        		else
        			reagents[i][ii] = 0;
        	}
        }

        for (int i = craftMatrix.getSizeInventory()-1; i >= 0; --i)
        {
        	ItemStack item = this.craftMatrix.getStackInSlot(i);
            if (item != null)
            {
            	System.out.println(i + " - " + item.stackSize);
            	outerloop:
            	for(int n = 0; n < 3; n++)
                {
                	for(int nn = 0; nn < 3; nn++)
                	{
                		if (item.itemID == reagents[n][nn])
                		{
                			craftMatrix.decrStackSize(i, 1);
                			reagents[n][nn] = 0;
                			if (item.stackSize > 0)
                				i ++;
                			break outerloop;
                		}
                	}
                }

                if (item.getItem().hasContainerItem())
                {
                    ItemStack itemstack2 = item.getItem().getContainerItemStack(item);

                    if (itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage())
                    {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
                        itemstack2 = null;
                    }

                    if (itemstack2 != null && (!item.getItem().doesContainerItemLeaveCraftingGrid(item) || !this.thePlayer.inventory.addItemStackToInventory(itemstack2)))
                    {
                        if (this.craftMatrix.getStackInSlot(i) == null)
                        {
                            this.craftMatrix.setInventorySlotContents(i, itemstack2);
                        }
                        else
                        {
                            this.thePlayer.dropPlayerItem(itemstack2);
                        }
                    }
                }
            }
        }
        
        craftMatrix.eventHandler.onCraftMatrixChanged(craftMatrix);
    }
    
    public boolean hasReagents()
    {
    	return true;
    }
}
