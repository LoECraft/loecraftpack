package loecraftpack.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import loecraftpack.LoECraftPack;
import loecraftpack.packethandling.PacketHelper;
import loecraftpack.packethandling.PacketIds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockZapAppleLeaves extends BlockLeavesBase implements IShearable
{
	@SideOnly(Side.CLIENT)
    private int IconByGraphicsLevel;
	private Icon icon[] = new Icon[4];
	int[] adjacentTreeBlocks;
	protected static boolean sheared = false;
	
	public BlockZapAppleLeaves(int id)
    {
        super(id, Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(LoECraftPack.LoECraftTab);
        this.setUnlocalizedName("leavesZap");
    }
	
	public int getcolor(int meta)
	{
		int r = 20+10*(meta&3);
		int g = 20+10*(meta&3);
		int b = 20+10*(meta&3);
		return (b & 255) << 16 | (g & 255) << 8 | r & 255;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
    {
        return getcolor(meta);//TODO determine foliage color
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int xCoord, int yCoord, int zCoord)
    {
		return getcolor(par1IBlockAccess.getBlockMetadata(xCoord, yCoord, zCoord));//TODO determine foliage color
    }
	
	@Override
	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random)
    {
        if (!world.isRemote)
        {
        	System.out.println("tick");
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
            if(flag)
            {
            	attemptGrow(world, xCoord, yCoord, zCoord, random);
            }
        }
    }
	
	public void attemptGrow(World world, int xCoord, int yCoord, int zCoord, Random random)
	{
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		if( (meta & 4) == 0 )
        {
        	//chance to grow
        	if( random.nextInt(10) == 0 )
        	{
        		System.out.println("growth "+((meta&3) + 1 ));
        		if(((meta&3) + 1 ) == 4)
        			zapGrow(world, xCoord, yCoord, zCoord);
        		else
        		{
        			if(world.setBlock(xCoord, yCoord, zCoord, this.blockID, meta + 1, 2))
        				tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID);
        		}
        	}
        }
	}
	
	public void zapGrow(World world, int xCoord, int yCoord, int zCoord)
	{
		System.out.println("ZAP");
		boolean flag = false;
		int reach = -1;
		int idTemp;
		while(true)
		{
			reach += 1;
			if(!world.blockExists(xCoord, yCoord+reach, zCoord))break;
			idTemp = world.getBlockId(xCoord, yCoord+reach, zCoord);
			if(idTemp == LoECraftPack.blockZapAppleLeaves.blockID || idTemp == LoECraftPack.blockZapAppleLeavesCharged.blockID)
				continue;
			else if(world.canLightningStrikeAt(xCoord, yCoord+reach, zCoord))
			{
				System.out.println("EYUP");
				flag = true;
				break;
			}
			else
			{
				System.out.println("ENOPE");
				break;
			}
		}
		if(flag)
		{
			world.addWeatherEffect(new EntityLightningBolt(world, xCoord, yCoord+reach, zCoord));
			if(world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID, 0, 2))
				tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeavesCharged.blockID);
			
			for(int xMod = -3; xMod <4; xMod++)
			{
				for(int yMod = -3; yMod <4; yMod++)
				{
					for(int zMod = -3; zMod <4; zMod++)
					{
						if(world.blockExists(xCoord+xMod, yCoord+yMod, zCoord+zMod) 
								&& world.getBlockId(xCoord+xMod, yCoord+yMod, zCoord+zMod) == LoECraftPack.blockZapAppleLeaves.blockID)
						{
							int meta = world.getBlockMetadata(xCoord+xMod, yCoord+yMod, zCoord+zMod);
							if( (meta&3) != 0 && (meta & 4) == 0)
							{
								if(world.setBlock(xCoord+xMod, yCoord+yMod, zCoord+zMod, LoECraftPack.blockZapAppleLeavesCharged.blockID, 0, 2))
									tellClientOfChange(world, xCoord+xMod, yCoord+yMod, zCoord+zMod, LoECraftPack.blockZapAppleLeavesCharged.blockID);
							}
						}
					}
				}
			}
		}
	}
	
	public void buckLeaf(World world, int xCoord, int yCoord, int zCoord)
	{
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord);
		
		if((meta&4)==1)return;//placed by player
		if(world.getBlockId(xCoord, yCoord, zCoord) == LoECraftPack.blockZapAppleLeaves.blockID && (meta&3) == 0) return;//no apples
		
		this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, new ItemStack(LoECraftPack.itemZapApple, 1, 0));
		if(world.setBlock(xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID, 0, 2))
			tellClientOfChange(world, xCoord, yCoord, zCoord, LoECraftPack.blockZapAppleLeaves.blockID);
	}
	
	private void removeLeaves(World world, int xCoord, int yCoord, int zCoord)
    {
		System.out.println("REMOVE");
        this.dropBlockAsItem(world, xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord), 0);
        world.setBlockToAir(xCoord, yCoord, zCoord);
    }
	
	@Override
	public void breakBlock(World world, int xCoord, int yCoord, int zCoord, int par5, int par6)
    {
		System.out.println("BREAK");
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
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
		System.out.println("DESTROY");
		int meta = world.getBlockMetadata(x, y, z);
		if((meta&3) != 0 && !sheared)
		{
			System.out.println("REPLACE");
			return world.setBlock(x, y, z, this.blockID, meta&12, 2);
		}
        return world.setBlockToAir(x, y, z);
    }
	
	
	//TODO adjust drop rate appropriately
	@Override
	public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return LoECraftPack.blockZapAppleSapling.blockID;
    }
	
	@Override
	public void dropBlockAsItemWithChance(World world, int xCoord, int yCoord, int zCoord, int par5, float par6, int par7)
    {
		System.out.println("DROP");
        if (!world.isRemote)
        {
            int j1 = 20;

            if ((par5 & 3) == 3)
            {
                j1 = 40;
            }

            if (par7 > 0)
            {
                j1 -= 2 << par7;

                if (j1 < 10)
                {
                    j1 = 10;
                }
            }

            if (world.rand.nextInt(j1) == 0)
            {
                int k1 = this.idDropped(par5, world.rand, par7);
                this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, new ItemStack(k1, 1, this.damageDropped(par5)));
            }

            j1 = 200;

            if (par7 > 0)
            {
                j1 -= 10 << par7;

                if (j1 < 40)
                {
                    j1 = 40;
                }
            }
            
            if (!sheared)
    		{
                if ((par5 & 3) != 0 || world.rand.nextInt(j1) == 0)
                {
                    this.dropBlockAsItem_do(world, xCoord, yCoord, zCoord, new ItemStack(LoECraftPack.itemZapApple, 1, 0));
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
	
	@SideOnly(Side.CLIENT)
	public void setGraphicsLevel(boolean graphicsLevel)
    {
        this.graphicsLevel = graphicsLevel;
        this.IconByGraphicsLevel = graphicsLevel ? 0 : 1;
    }

	@SideOnly(Side.CLIENT)
	public void findGraphicsLevel()
    {
		setGraphicsLevel(Block.leaves.graphicsLevel);
    }
	
	//TODO  might change this for charged appearance
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		findGraphicsLevel();
		return icon[IconByGraphicsLevel];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));//Normal
        par3List.add(new ItemStack(par1, 1, 1));//Blossomed
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
		icon[0] = iconRegister.registerIcon("loecraftpack:leaves_zapapple");
		icon[1] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_opaque");
		icon[2] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_bloom");
		icon[3] = iconRegister.registerIcon("loecraftpack:leaves_zapapple_bloom_opaque");
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
    
  //used to tell the client that the block ID is changed
  	private void tellClientOfChange(World world, int xCoord, int yCoord, int zCoord, int newID)
  	{
  		if (world != null && !world.isRemote)
  		{
  			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 64.0D, world.provider.dimensionId,
  					                               PacketHelper.Make("loecraftpack", PacketIds.zapAppleUpdate,
  					                                                 xCoord, yCoord, zCoord, newID));
  		}
  	}
}
