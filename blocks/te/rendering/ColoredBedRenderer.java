package loecraftpack.blocks.te.rendering;

import loecraftpack.blocks.ColoredBedBlock;
import loecraftpack.blocks.te.ColoredBedTileEntity;
import loecraftpack.enums.Dye;
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
public class ColoredBedRenderer implements ISimpleBlockRenderingHandler
{
    
	public int renderID;
	
	public Icon getBlockIcon(ColoredBedBlock block ,int side, int meta, int id)
    {
        if (side == 0)
        {
            return Block.planks.getBlockTextureFromSide(side);
        }
        else
        {
            int k = meta & 3;//get direction
            int l = Direction.bedDirection[k][side];
            int i1 = ( (meta & 8) != 0  ) ? 1 : 0; //isBlockHeadOfBed
            return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? block.bedtop.get(id)[i1] : block.bedside.get(id)[i1]) : block.bedend.get(id)[i1];
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
		if(colorTile instanceof ColoredBedTileEntity)
			id = ((ColoredBedTileEntity)colorTile).id;
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
        Icon icon = this.getBlockIcon((ColoredBedBlock)block, 0, metadata, id);
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
        icon = this.getBlockIcon((ColoredBedBlock)block, 1, metadata, id);
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
        int k1 = Direction.headInvisibleFace[i1];

        if (flag)
        {
            k1 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[i1]];
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
            renderer.renderEastFace(block, (double)x, (double)y, (double)z, getBlockIcon((ColoredBedBlock)block, 2, metadata, id));
        }

        if (k1 != 3 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y, z + 1, 3)))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? j1 : block.getMixedBrightnessForBlock(world, x, y, z + 1));
            tessellator.setColorOpaque_F(f2, f2, f2);
            renderer.flipTexture = b0 == 3;
            renderer.renderWestFace(block, (double)x, (double)y, (double)z, getBlockIcon((ColoredBedBlock)block, 3, metadata, id));
        }

        if (k1 != 4 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x - 1, y, z, 4)))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? j1 : block.getMixedBrightnessForBlock(world, x - 1, y, z));
            tessellator.setColorOpaque_F(f3, f3, f3);
            renderer.flipTexture = b0 == 4;
            renderer.renderNorthFace(block, (double)x, (double)y, (double)z, getBlockIcon((ColoredBedBlock)block, 4, metadata, id));
        }

        if (k1 != 5 && (renderer.renderAllFaces || block.shouldSideBeRendered(world, x + 1, y, z, 5)))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? j1 : block.getMixedBrightnessForBlock(world, x + 1, y, z));
            tessellator.setColorOpaque_F(f3, f3, f3);
            renderer.flipTexture = b0 == 5;
            renderer.renderSouthFace(block, (double)x, (double)y, (double)z, getBlockIcon((ColoredBedBlock)block, 5, metadata, id));
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



/*
public boolean renderBlockBed(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i1 = par1Block.getBedDirection(blockAccess, par2, par3, par4);
        boolean flag = par1Block.isBedFoot(blockAccess, par2, par3, par4);
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        int j1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
        tessellator.setBrightness(j1);
        tessellator.setColorOpaque_F(f, f, f);
        Icon icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
        if (hasOverrideBlockTexture()) icon = overrideBlockTexture; //BugFix Proper breaking texture on underside
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getMaxU();
        double d2 = (double)icon.getMinV();
        double d3 = (double)icon.getMaxV();
        double d4 = (double)par2 + this.renderMinX;
        double d5 = (double)par2 + this.renderMaxX;
        double d6 = (double)par3 + this.renderMinY + 0.1875D;
        double d7 = (double)par4 + this.renderMinZ;
        double d8 = (double)par4 + this.renderMaxZ;
        tessellator.addVertexWithUV(d4, d6, d8, d0, d3);
        tessellator.addVertexWithUV(d4, d6, d7, d0, d2);
        tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
        tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
        tessellator.setColorOpaque_F(f1, f1, f1);
        icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1);
        if (hasOverrideBlockTexture()) icon = overrideBlockTexture; //BugFix Proper breaking texture on underside
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

        double d12 = (double)par2 + this.renderMinX;
        double d13 = (double)par2 + this.renderMaxX;
        double d14 = (double)par3 + this.renderMaxY;
        double d15 = (double)par4 + this.renderMinZ;
        double d16 = (double)par4 + this.renderMaxZ;
        tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
        tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
        tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
        tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
        int k1 = Direction.headInvisibleFace[i1];

        if (flag)
        {
            k1 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[i1]];
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

        if (k1 != 2 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)))
        {
            tessellator.setBrightness(this.renderMinZ > 0.0D ? j1 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
            tessellator.setColorOpaque_F(f2, f2, f2);
            this.flipTexture = b0 == 2;
            this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2));
        }

        if (k1 != 3 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)))
        {
            tessellator.setBrightness(this.renderMaxZ < 1.0D ? j1 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
            tessellator.setColorOpaque_F(f2, f2, f2);
            this.flipTexture = b0 == 3;
            this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
        }

        if (k1 != 4 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)))
        {
            tessellator.setBrightness(this.renderMinZ > 0.0D ? j1 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
            tessellator.setColorOpaque_F(f3, f3, f3);
            this.flipTexture = b0 == 4;
            this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4));
        }

        if (k1 != 5 && (this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)))
        {
            tessellator.setBrightness(this.renderMaxZ < 1.0D ? j1 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
            tessellator.setColorOpaque_F(f3, f3, f3);
            this.flipTexture = b0 == 5;
            this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5));
        }

        this.flipTexture = false;
        return true;
    }
    */
