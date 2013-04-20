package loecraftpack.blocks;

import loecraftpack.LoECraftPack;
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

	// TODO change 16 to a value dependent on the number of types
	public static int bedTypes = 16;
	public Dye color = Dye.White;//TODO make this more stable
	
	/** Maps the foot-of-bed block to the head-of-bed block. */
    public static final int[][] footBlockToHeadBlockMap = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};
    @SideOnly(Side.CLIENT)
    protected static Icon[][] bedend = new Icon[bedTypes][];//end
    @SideOnly(Side.CLIENT)
    protected static Icon[][] bedside = new Icon[bedTypes][];//side
    @SideOnly(Side.CLIENT)
    protected static Icon[][] bedtop = new Icon[bedTypes][];//top
    
    
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
		for(int i=0; i<bedTypes; i++)
		{
			this.bedtop[i] = new Icon[] {par1IconRegister.registerIcon("bed_"+Dye.values()[i]+"_feet_top"), par1IconRegister.registerIcon("bed_"+Dye.values()[i]+"_head_top")};
			this.bedend[i] = new Icon[] {par1IconRegister.registerIcon("bed_"+Dye.values()[i]+"_feet_end"), par1IconRegister.registerIcon("bed_"+Dye.values()[i]+"_head_end")};
			this.bedside[i] = new Icon[] {par1IconRegister.registerIcon("bed_"+Dye.values()[i]+"_feet_side"), par1IconRegister.registerIcon("bed_"+Dye.values()[i]+"_head_side")};
		}
    }
	
	@SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        if (side == 0)
        {
            return Block.planks.getBlockTextureFromSide(side);
        }
        else
        {
            int k = getDirection(meta);
            int l = Direction.bedDirection[k][side];
            int i1 = isBlockHeadOfBed(meta) ? 1 : 0;
            return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? this.bedtop[color.ordinal()][i1] : this.bedside[color.ordinal()][i1]) : this.bedend[color.ordinal()][i1];
        }
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
		return new ItemStack(LoECraftPack.bedItems, 1, color.ordinal());
    }
	
	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLiving player)
    {
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		System.out.println("create tile");
		return new ColoredBedTileEntity(this);
	}
}
