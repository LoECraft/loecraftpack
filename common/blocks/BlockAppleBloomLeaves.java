package loecraftpack.common.blocks;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import loecraftpack.LoECraftPack;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("all")
public class BlockAppleBloomLeaves extends BlockLeavesBase implements IShearable, cpw.mods.fml.common.Mod.Block
{
	public Item apple;
	public int appleType = 0;//item damage
	public int bloomStage = 2;
	protected int saplingDropRate = 50;
	protected double growthRate = 30;//average of 41 minutes(real time) to reach the next stage
	protected Icon icon[] = new Icon[4];
	/**flags, etc.**/
    protected int IconByGraphicsLevel;
	protected int[] adjacentTreeBlocks;
	protected static boolean sheared = false;
	
	
	public BlockAppleBloomLeaves(int id)
    {
        super(id, Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.setUnlocalizedName("leavesAppleBloom");
        this.apple = Item.appleRed;
    }
	
	//no color change
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int par1)
    {
        return 16777215;
    }
	
	//no color change
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 16777215;
    }
	
	/*
	//inventory 
	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderColor(int par1)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }
	
	//world
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;

        for (int l1 = -1; l1 <= 1; ++l1)
        {
            for (int i2 = -1; i2 <= 1; ++i2)
            {
                int j2 = par1IBlockAccess.getBiomeGenForCoords(par2 + i2, par4 + l1).getBiomeFoliageColor();
                i1 += (j2 & 16711680) >> 16;
                j1 += (j2 & 65280) >> 8;
                k1 += j2 & 255;
            }
        }

        return (i1 / 9 & 255) << 16 | (j1 / 9 & 255) << 8 | k1 / 9 & 255;
    }
    */
	
	@Override
	public void breakBlock(World world, int xCoord, int yCoord, int zCoord, int par5, int par6)
    {
        byte b0 = 1;
        int j1 = b0 + 1;

        if (world.checkChunksExist(xCoord - j1, yCoord - j1, zCoord - j1, xCoord + j1, yCoord + j1, zCoord + j1))
        {
            for (int k1 = -b0; k1 <= b0; ++k1)
            {
                for (int l1 = -b0; l1 <= b0; ++l1)
                {
                    for (int i2 = -b0; i2 <= b0; ++i2)
                    {
                        int j2 = world.getBlockId(xCoord + k1, yCoord + l1, zCoord + i2);

                        if (Block.blocksList[j2] != null)
                        {
                            Block.blocksList[j2].beginLeavesDecay(world, xCoord + k1, yCoord + l1, zCoord + i2);
                        }
                    }
                }
            }
        }
    }
	
	@Override
	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random)
    {
        if (!world.isRemote)
        {
            int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
            boolean flag = true;
            
            if ((meta & 8) != 0 && (meta & 4) == 0)
            {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.adjacentTreeBlocks == null)
                {
                    this.adjacentTreeBlocks = new int[b1 * b1 * b1];
                }

                int l1;

                if (world.checkChunksExist(xCoord - i1, yCoord - i1, zCoord - i1, xCoord + i1, yCoord + i1, zCoord + i1))
                {
                    int i2;
                    int j2;
                    int k2;

                    for (l1 = -b0; l1 <= b0; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                k2 = world.getBlockId(xCoord + l1, yCoord + i2, zCoord + j2);

                                Block block = Block.blocksList[k2];

                                if (block != null && block.canSustainLeaves(world, xCoord + l1, yCoord + i2, zCoord + j2))
                                {
                                    this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                                else if (block != null && block.isLeaves(world, xCoord + l1, yCoord + i2, zCoord + j2))
                                {
                                    this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                }
                                else
                                {
                                    this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                for (k2 = -b0; k2 <= b0; ++k2)
                                {
                                    if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1)
                                    {
                                        if (this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                        {
                                            this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                        {
                                            this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2)
                                        {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2)
                                        {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2)
                                        {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2)
                                        {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.adjacentTreeBlocks[k1 * j1 + k1 * b1 + k1];

                if (l1 >= 0)
                {
                    world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta & -9, 4);
                }
                else
                {
                    this.removeLeaves(world, xCoord, yCoord, zCoord);
                    flag = false;
                }
            }
            if (flag)
            {
            	attemptGrow(world, xCoord, yCoord, zCoord, random);
            }
        }
    }
	
	private void removeLeaves(World world, int xCoord, int yCoord, int zCoord)
    {
        this.dropBlockAsItem(world, xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord), 0);
        world.setBlockToAir(xCoord, yCoord, zCoord);
    }
	
	/**
	 * this is called during updateTick. it attempts to progress the leaves growth stage.
	 */
	public void attemptGrow(World world, int xCoord, int yCoord, int zCoord, Random random)
	{
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
    	if ( (meta & 4) == 0 && random.nextDouble()*(growthRate) <= 1 &&
    	     ((meta&3) + 1 ) < 4 && world.setBlock(xCoord, yCoord, zCoord, this.blockID, meta + 1, 2) )
    	{
			tellClientOfChange(world, xCoord, yCoord, zCoord, this.blockID);
    	}
	}
	
	/**
	 * Drop apple down thru the tree.  The wood and leaves IDs are passed by the class.
	 */
	public void dropAppleThruTree(World world, int xCoord, int yCoord, int zCoord, ItemStack itemStack)
	{
		dropAppleThruTree(world, xCoord, yCoord, zCoord, itemStack,
				new int[]{Block.wood.blockID,
				          LoECraftPack.blockAppleBloomLeaves.blockID});
	}
	
	protected void dropAppleThruTree(World world, int xCoord, int yCoord, int zCoord, ItemStack itemStack, int ids[])
	{
		int id;
		while (true)
		{
			id = world.getBlockId(xCoord, yCoord, zCoord);
			boolean flag = false;
			for (int i=0; i<ids.length; i++)
			{
				if (id == ids[i])
					flag=true;
			}
			if (flag)
				yCoord--;
			else
			{
				yCoord++;
				break;
			}
			if (!world.blockExists(xCoord, yCoord, zCoord))
			{
				yCoord++;
				break;
			}
		}
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = -0.2D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)xCoord + d0, (double)yCoord + d1, (double)zCoord + d2, itemStack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
	}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
		int meta = world.getBlockMetadata(x, y, z);
		if ((meta&3) >= bloomStage && !sheared && (meta&4)==0 )
		{
			return world.setBlock(x, y, z, this.blockID, meta&12, 2);
		}
        return world.setBlockToAir(x, y, z);
    }
	
	
	@Override
	public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return LoECraftPack.blockAppleBloomSapling.blockID;
    }
	
	@Override
	public void dropBlockAsItemWithChance(World world, int xCoord, int yCoord, int zCoord, int meta, float par6, int fortune)
    {
        if (!world.isRemote)
        {
        	int j1 = saplingDropRate;

            if (fortune > 0)
            {
                j1 -= (saplingDropRate/10) << fortune;

                if (j1 < 10)
                {
                    j1 = 10;
                }
            }

            if (world.rand.nextInt(j1) == 0)
            {
                int k1 = this.idDropped(meta, world.rand, fortune);
                this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, new ItemStack(k1, 1, this.damageDropped(meta)));
            }
            
            if (!sheared)
    		{
                if ((meta & 3) >= bloomStage)
                {
                	int j = world.rand.nextInt(fortune + 1) - 1;
                	
                    if (j < 0)
                    {
                        j = 0;
                    }
                    
                    this.dropAppleThruTree(world, xCoord, yCoord, zCoord, new ItemStack(apple, 1, appleType));
                }
    		}
            //reset sheared
            sheared = false;
        }
    }
	
	//TODO confirm if there is a issue with exploits
	@Override
	public boolean isOpaqueCube()
    {
		findGraphicsLevel();
        return !this.graphicsLevel;
    }
	
	public void setGraphicsLevel(boolean graphicsLevel)
    {
        this.graphicsLevel = graphicsLevel;
        this.IconByGraphicsLevel = graphicsLevel ? 0 : 1;
    }
	
	public void findGraphicsLevel()
    {
		setGraphicsLevel(Block.leaves.graphicsLevel);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		findGraphicsLevel();
		if ((meta&3) < bloomStage)
			return icon[IconByGraphicsLevel];
		return icon[IconByGraphicsLevel+2];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));//Normal
        par3List.add(new ItemStack(par1, 1, bloomStage));//Blossomed
    }
	
	@Override
	protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(this.blockID, 1, meta & 3);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon[0] = iconRegister.registerIcon("loecraftpack:trees/leaves");
		icon[1] = iconRegister.registerIcon("loecraftpack:trees/leaves_opaque");
		icon[2] = iconRegister.registerIcon("loecraftpack:trees/leaves_bloom");
		icon[3] = iconRegister.registerIcon("loecraftpack:trees/leaves_bloom_opaque");
	}
	
	@Override
	public boolean isShearable(ItemStack item, World world, int xCoord, int yCoord, int zCoord)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, World world, int xCoord, int yCoord, int zCoord, int fortune)
	{
		sheared = true;
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(xCoord, yCoord, zCoord) & 3));
        return ret;
	}
	
	@Override
    public void beginLeavesDecay(World world, int xCoord, int yCoord, int zCoord)
    {
        world.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord) | 8, 4);
    }

    @Override
    public boolean isLeaves(World world, int xCoord, int yCoord, int zCoord)
    {
        return true;
    }
    
    /**
     * used to tell the client that the block ID is changed
     */
  	public void tellClientOfChange(World world, int xCoord, int yCoord, int zCoord, int newID)
  	{
  		if (world != null && !world.isRemote)
  		{
  			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 64.0D, world.provider.dimensionId,
  					                               PacketHelper.Make("loecraftpack", PacketIds.appleBloomUpdate,
  					                                                 xCoord, yCoord, zCoord, newID));
  		}
  	}
  	
  	
  	/**  FML STUFF  **/
  	
  	@Override
	public Class<? extends Annotation> annotationType() {
		return null;
	}
  	
	@Override
	public String name() {
		return "AppleBloomLeaves";
	}

	@Override
	public Class<?> itemTypeClass() {
		return null;
	}

	
}
