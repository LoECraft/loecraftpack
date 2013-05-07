package loecraftpack.common.blocks;

import java.util.Random;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiIds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockProjectTable extends BlockContainer
{
	@SideOnly(Side.CLIENT)
    private Icon topIcon;
    @SideOnly(Side.CLIENT)
    private Icon frontIcon;
	
    private final Random random = new Random();
    
	public BlockProjectTable(int id)
	{
		super(id, Material.wood);
		this.setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 == 1 ? topIcon : (par1 == 0 ? Block.planks.getBlockTextureFromSide(par1) : (par1 != 2 && par1 != 4 ? this.blockIcon : frontIcon));
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("workbench_side");
        this.topIcon = par1IconRegister.registerIcon("workbench_top");
        this.frontIcon = par1IconRegister.registerIcon("workbench_front");
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        	player.openGui(LoECraftPack.instance, GuiIds.ProjectTable, world, x, y, z);
        
        return true;
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta)
    {
        TileProjectTable te = (TileProjectTable)world.getBlockTileEntity(x, y, z);

        if (te != null)
        {
            for (int j1 = 0; j1 < te.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = te.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
                    {
                        int k1 = this.random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }
        }

        super.breakBlock(world, x, y, z, id, meta);
    }
	
	@Override
	public TileEntity createNewTileEntity(World world)
    {
        return  new TileProjectTable();
    }
}
