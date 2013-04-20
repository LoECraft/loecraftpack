package loecraftpack.blocks;

import loecraftpack.blocks.te.ColoredBedTileEntity;
import loecraftpack.enums.Dye;
import loecraftpack.items.ColoredBedItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ColoredBedBlock extends BlockBed implements ITileEntityProvider {

	public ColoredBedItem item;
	public Dye color;
	
	/** Maps the foot-of-bed block to the head-of-bed block. */
    public static final int[][] footBlockToHeadBlockMap = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};
    @SideOnly(Side.CLIENT)
    private Icon[] bedend;//end
    @SideOnly(Side.CLIENT)
    private Icon[] bedside;//side
    @SideOnly(Side.CLIENT)
    private Icon[] bedtop;//top
    
    
    // TODO  update constructor to accept Icons, to simply changing render.
	public ColoredBedBlock(int par1) {
		super(par1);
	}
	
	@SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.bedtop = new Icon[] {par1IconRegister.registerIcon("bed_feet_top"), par1IconRegister.registerIcon("bed_head_top")};
        this.bedend = new Icon[] {par1IconRegister.registerIcon("bed_feet_end"), par1IconRegister.registerIcon("bed_head_end")};
        this.bedside = new Icon[] {par1IconRegister.registerIcon("bed_feet_side"), par1IconRegister.registerIcon("bed_head_side")};
    }
	
	@SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par1 == 0)
        {
            return Block.planks.getBlockTextureFromSide(par1);
        }
        else
        {
            int k = getDirection(par2);
            int l = Direction.bedDirection[k][par1];
            int i1 = isBlockHeadOfBed(par2) ? 1 : 0;
            return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? this.bedtop[i1] : this.bedside[i1]) : this.bedend[i1];
        }
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
		return new ItemStack(item);
    }
	
	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLiving player)
    {
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new ColoredBedTileEntity(this);
	}
}
