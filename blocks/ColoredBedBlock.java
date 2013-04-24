package loecraftpack.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import loecraftpack.LoECraftPack;
import loecraftpack.blocks.te.ColoredBedTileEntity;
import loecraftpack.enums.Dye;
import loecraftpack.logic.handlers.ColoredBedHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ColoredBedBlock extends BlockBed implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
    public List<Icon[]> bedend = new ArrayList<Icon[]>();
    @SideOnly(Side.CLIENT)
    public List<Icon[]> bedside = new ArrayList<Icon[]>();
    @SideOnly(Side.CLIENT)
    public List<Icon[]> bedtop = new ArrayList<Icon[]>();
    
    public int renderID = 14;
    
	public ColoredBedBlock(int par1) {
		super(par1);
	}
	
	/**
     * The type of render function that is called for this block
     */
	@Override
    public int getRenderType()
    {
        return renderID;
    }
	
	@SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.bedtop.clear();
		this.bedend.clear();
		this.bedside.clear();
		
		for(String name : ColoredBedHandler.customBedIconNames)
		{
			this.bedtop.add( new Icon[] {par1IconRegister.registerIcon("loecraftpack:bed_"+name+"_feet_top"), par1IconRegister.registerIcon("loecraftpack:bed_"+name+"_head_top")} );
			this.bedend.add( new Icon[] {par1IconRegister.registerIcon("loecraftpack:bed_"+name+"_feet_end"), par1IconRegister.registerIcon("loecraftpack:bed_"+name+"_head_end")} );
			this.bedside.add( new Icon[] {par1IconRegister.registerIcon("loecraftpack:bed_"+name+"_feet_side"), par1IconRegister.registerIcon("loecraftpack:bed_"+name+"_head_side")} );
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
		return Block.planks.getBlockTextureFromSide(par1);
    }
		
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        
        int damageValue=0;
        int direct = this.getDirection(metadata);
        TileEntity tile;
        if( isBlockHeadOfBed(metadata) )
        	tile = world.getBlockTileEntity(x + footBlockToHeadBlockMap[direct][0], y, z + footBlockToHeadBlockMap[direct][1]);
        else
        	tile = world.getBlockTileEntity(x, y, z);
        System.out.println("d:"+direct);
        System.out.println("0:"+footBlockToHeadBlockMap[direct][0]);
        System.out.println("1:"+footBlockToHeadBlockMap[direct][1]);
        for(int ix = x-1; ix< x+2; ix++)
        {
        	for(int iz = z-1; iz< z+2; iz++)
        	{
        		System.out.println("x:"+ ix + " z:"+ iz +" exist:"+ (world.getBlockTileEntity(ix, y, iz)!=null) );
        	}
        }
        
        if (tile instanceof ColoredBedTileEntity)
        {
        	damageValue = ((ColoredBedTileEntity)tile).id;
        }
        ret.add(new ItemStack(idDropped(metadata, world.rand, fortune), 1, damageValue));
        return ret;
    }
	
	@Override
	public int idDropped(int meta, Random par2Random, int par3)
    {
        return isBlockHeadOfBed(meta) ? 0 : LoECraftPack.bedItems.itemID;
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if( tile instanceof ColoredBedTileEntity)
			return new ItemStack(LoECraftPack.bedItems, 1, ((ColoredBedTileEntity)tile).id);
		else
		    return new ItemStack(LoECraftPack.bedItems, 1, Dye.White.ordinal());
    }
	
	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLiving player)
    {
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new ColoredBedTileEntity(0);
	}
}
