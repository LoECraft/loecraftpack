package loecraftpack.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import loecraftpack.LoECraftPack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockAppleLog extends Block 
{
	String iconNameSide;
	String iconNameTop;
	Icon iconSide;
	Icon iconTop;
	
	public BlockAppleLog(int par1, String iconNameSide, String iconNameTop)
    {
        super(par1, Material.wood);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.iconNameSide = iconNameSide;
        this.iconNameTop = iconNameTop;
    }
	
	public int getRenderType()
    {
        return 31;
    }
	
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
    
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }
    
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        byte b0 = 4;
        int j1 = b0 + 1;

        if (par1World.checkChunksExist(par2 - j1, par3 - j1, par4 - j1, par2 + j1, par3 + j1, par4 + j1))
        {
            for (int k1 = -b0; k1 <= b0; ++k1)
            {
                for (int l1 = -b0; l1 <= b0; ++l1)
                {
                    for (int i2 = -b0; i2 <= b0; ++i2)
                    {
                        int j2 = par1World.getBlockId(par2 + k1, par3 + l1, par4 + i2);

                        if (Block.blocksList[j2] != null)
                        {
                            Block.blocksList[j2].beginLeavesDecay(par1World, par2 + k1, par3 + l1, par4 + i2);
                        }
                    }
                }
            }
        }
    }
    
    /**used by ItemBlock **/
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = par9 & 3;
        byte b0 = 0;

        switch (par5)
        {
            case 0:
            case 1:
                b0 = 0;
                break;
            case 2:
            case 3:
                b0 = 8;
                break;
            case 4:
            case 5:
                b0 = 4;
        }
        
        b0 += 2;//player placed log
        
        return j1 | b0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        int k = par2 & 12;
        int l = par2 & 3;
        return k == 0 && (par1 == 1 || par1 == 0) ? this.iconTop : (k == 4 && (par1 == 5 || par1 == 4) ? this.iconTop : (k == 8 && (par1 == 2 || par1 == 3) ? this.iconTop : this.iconSide));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconTop = par1IconRegister.registerIcon(iconNameTop);
        this.iconSide = par1IconRegister.registerIcon(iconNameSide);
    }
    
    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean isWood(World world, int x, int y, int z)
    {
        return true;
    }

}
