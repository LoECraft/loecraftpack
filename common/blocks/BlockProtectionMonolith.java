package loecraftpack.common.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiIds;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockProtectionMonolith extends Block implements ITileEntityProvider
{
	public static Map<Integer, List<TileProtectionMonolith>> monoliths = new HashMap<Integer, List<TileProtectionMonolith>>();
	
	public BlockProtectionMonolith(int id)
	{
		super(id, Material.rock);
		setUnlocalizedName("Protection Monolith");
		setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
		int dim = par1World.getWorldInfo().getDimension();
		List<TileProtectionMonolith> list = monoliths.get(dim);
		if (list == null)
			list = new ArrayList<TileProtectionMonolith>();
		else
			monoliths.remove(dim);
		
		for(int i = 0; i < list.size(); i++)
		{
			TileProtectionMonolith te = list.get(i);
			if (te.xCoord == par2 &&
				te.yCoord == par3 &&
				te.zCoord == par4)
			{
				list.remove(i);
				break;
			}
		}
		
		monoliths.put(dim, list);
		
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer playerEntity, int w, float px, float py, float pz)
	{
		if (((TileProtectionMonolith)world.getBlockTileEntity(x, y, z)).isOwner(playerEntity.username))
			playerEntity.openGui(LoECraftPack.instance, GuiIds.MONOLITH.ordinal(), world, x, y, z);
		return false;
	}
	
    public TileEntity createNewTileEntity(World world)
    {
    	TileProtectionMonolith te = new TileProtectionMonolith();
    	if (world.isRemote && monoliths.get(world.getWorldInfo().getDimension()) != null)
    		monoliths.get(world.getWorldInfo().getDimension()).add(te);
    	
    	return te;
    }
    
    public int getRenderType()
    {
        return -1;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }
}
