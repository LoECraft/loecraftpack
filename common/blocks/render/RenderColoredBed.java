package loecraftpack.common.blocks.render;

import loecraftpack.common.blocks.BlockColoredBed;
import loecraftpack.common.blocks.TileColoredBed;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderColoredBed implements ISimpleBlockRenderingHandler
{
    
	public int renderID;
	
	public Icon getBlockIcon(BlockColoredBed block ,int side, int meta, int id, int pairSide)
    {
        if(side == 0)
        {
            return Block.planks.getBlockTextureFromSide(side);
        }
        else
        {
            int k = meta & 3;//get direction
            int l = Direction.bedDirection[k][side];
            int i1 = ( (meta & 8) != 0  ) ? 1 : 0; //isBlockHeadOfBed
            if (pairSide == 0)//normal
            {
            	return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? block.bedtop.get(id)[i1] : block.bedside.get(id)[i1]) : block.bedend.get(id)[i1];
            }
            else if (pairSide == 1)//right pair
            {
            	return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? block.bedPairTopRight.get(id)[i1] : block.bedPairSideRight.get(id)[i1]) : block.bedPairEndRight.get(id)[i1];
            }
            else//left pair
            {
            	return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? block.bedPairTopLeft.get(id)[i1] : block.bedPairSideLeft.get(id)[i1]) : block.bedPairEndLeft.get(id)[i1];
            }
        		
        }
    }

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer)
	{
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer)
	{
		TileEntity colorTile = world.getBlockTileEntity(x, y, z);
		int id = 0;//set by tile entity
		int pairSide = 0;
		if(colorTile != null && colorTile instanceof TileColoredBed)
		{			
			if( ((TileColoredBed)colorTile).pairID == -1 )
				id = ((TileColoredBed)colorTile).id;
			else
			{
				pairSide = ((TileColoredBed)colorTile).pairSide;
				id = ((TileColoredBed)colorTile).pairID;
			}
		}
		int metadata = world.getBlockMetadata(x, y, z);
		
		
		//altered Vanilla bed code//
		Tessellator tessellator = Tessellator.instance;
        int i1 = block.getBedDirection(world, x, y, z);
        boolean flag = block.isBedFoot(world, x, y, z);
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        int j1 = block.getMixedBrightnessForBlock(world, x, y, z);
        tessellator.setBrightness(j1);
        tessellator.setColorOpaque_F(f, f, f);
        Icon icon = this.getBlockIcon((BlockColoredBed)block, 0, metadata, id, pairSide);
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getMaxU();
        double d2 = (double)icon.getMinV();
        double d3 = (double)icon.getMaxV();
        double d4 = (double)x + renderer.renderMinX;
        double d5 = (double)x + renderer.renderMaxX;
        double d6 = (double)y + renderer.renderMinY + 0.1875D;
        double d7 = (double)z + renderer.renderMinZ;
        double d8 = (double)z + renderer.renderMaxZ;
        tessellator.addVertexWithUV(d4, d6, d8, d0, d3);
        tessellator.addVertexWithUV(d4, d6, d7, d0, d2);
        tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
        tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y + 1, z));
        tessellator.setColorOpaque_F(f1, f1, f1);
        icon = this.getBlockIcon((BlockColoredBed)block, 1, metadata, id, pairSide);
        d0 = (double)icon.getMinU();
        d1 = (double)icon.getMaxU();
        d2 = (double)icon.getMinV();
        d3 = (double)icon.getMaxV();
        d4 = d0;
        d5 = d1;
        d6 = d2;
        d7 = d2;
        d8 = d0;
        double d9 = d1;
        double d10 = d3;
        double d11 = d3;

        if (i1 == 0)
        {
            d5 = d0;
            d6 = d3;
            d8 = d1;
            d11 = d2;
        }
        else if (i1 == 2)
        {
            d4 = d1;
            d7 = d3;
            d9 = d0;
            d10 = d2;
        }
        else if (i1 == 3)
        {
            d4 = d1;
            d7 = d3;
            d9 = d0;
            d10 = d2;
            d5 = d0;
            d6 = d3;
            d8 = d1;
            d11 = d2;
        }

        double d12 = (double)x + renderer.renderMinX;
        double d13 = (double)x + renderer.renderMaxX;
        double d14 = (double)y + renderer.renderMaxY;
        double d15 = (double)z + renderer.renderMinZ;
        double d16 = (double)z + renderer.renderMaxZ;
        tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
        tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
        tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
        tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
        int k1 = Direction.directionToFacing[i1];

        if (flag)
        {
            k1 = Direction.directionToFacing[Direction.rotateOpposite[i1]];
        }

        byte b0 = 4;

        switch (i1)
        {
            case 0:
                b0 = 5;
                break;
            case 1:
                b0 = 3;
            case 2:
            default:
                break;
            case 3:
                b0 = 2;
        }

        if (k1 != 2 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y, z - 1, 2)))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? j1 : block.getMixedBrightnessForBlock(world, x, y, z - 1));
            tessellator.setColorOpaque_F(f2, f2, f2);
            renderer.flipTexture = b0 == 2;
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, getBlockIcon((BlockColoredBed)block, 2, metadata, id, pairSide));
        }

        if (k1 != 3 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y, z + 1, 3)))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? j1 : block.getMixedBrightnessForBlock(world, x, y, z + 1));
            tessellator.setColorOpaque_F(f2, f2, f2);
            renderer.flipTexture = b0 == 3;
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, getBlockIcon((BlockColoredBed)block, 3, metadata, id, pairSide));
        }

        if (k1 != 4 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x - 1, y, z, 4)))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? j1 : block.getMixedBrightnessForBlock(world, x - 1, y, z));
            tessellator.setColorOpaque_F(f3, f3, f3);
            renderer.flipTexture = b0 == 4;
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, getBlockIcon((BlockColoredBed)block, 4, metadata, id, pairSide));
        }

        if (k1 != 5 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x + 1, y, z, 5)))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? j1 : block.getMixedBrightnessForBlock(world, x + 1, y, z));
            tessellator.setColorOpaque_F(f3, f3, f3);
            renderer.flipTexture = b0 == 5;
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, getBlockIcon((BlockColoredBed)block, 5, metadata, id, pairSide));
        }

        renderer.flipTexture = false;
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}
	
}
