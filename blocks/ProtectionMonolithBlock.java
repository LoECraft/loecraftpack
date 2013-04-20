package loecraftpack.blocks;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;
import loecraftpack.blocks.te.ProtectionMonolithTileEntity;
import loecraftpack.gui.GuiIds;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ProtectionMonolithBlock extends Block implements ITileEntityProvider
{
	public static List<ProtectionMonolithTileEntity> monoliths = new ArrayList<ProtectionMonolithTileEntity>();
	
	public ProtectionMonolithBlock(int id)
	{
		super(id, Material.rock);
		setUnlocalizedName("Protection Monolith");
		setCreativeTab(LoECraftPack.LoECraftTab);
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
		int i;
		for(i = 0; i < monoliths.size(); i++)
		{
			ProtectionMonolithTileEntity te = monoliths.get(i);
			if (te.xCoord == par2 &&
				te.yCoord == par3 &&
				te.zCoord == par4)
			{
				monoliths.remove(i);
				break;
			}
		}
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer playerEntity, int w, float px, float py, float pz)
	{
		if (((ProtectionMonolithTileEntity)world.getBlockTileEntity(x, y, z)).isOwner(playerEntity.username))
			playerEntity.openGui(LoECraftPack.instance, GuiIds.Monolith, world, x, y, z);
		return false;
	}
	
    public TileEntity createNewTileEntity(World world)
    {
    	ProtectionMonolithTileEntity te = new ProtectionMonolithTileEntity();
    	if (world.isRemote)
    		monoliths.add(te);
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
