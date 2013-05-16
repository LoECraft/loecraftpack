package loecraftpack.common.blocks.render;

import org.lwjgl.opengl.GL11;

import loecraftpack.LoECraftPack;
import loecraftpack.common.blocks.BlockHiddenOre;
import loecraftpack.ponies.abilities.mechanics.MechanicHiddenOres;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderHiddenOre implements ISimpleBlockRenderingHandler
{
	public int renderID;
	
	@Override
	public void renderInventoryBlock(Block blockBase, int metadata, int modelID,
			RenderBlocks renderer) {
		BlockHiddenOre block = (BlockHiddenOre)blockBase;
		
		Tessellator tessellator = Tessellator.instance;
		boolean flag = block.blockID == Block.grass.blockID;
		float par3 = 1.0f;//why isn't this passed to us?
		
		int j;
        float f1;
        float f2;
        float f3;

        if (renderer.useInventoryTint)
        {
            j = block.getRenderColor(metadata);

            if (flag)
            {
                j = 16777215;
            }

            f1 = (float)(j >> 16 & 255) / 255.0F;
            f2 = (float)(j >> 8 & 255) / 255.0F;
            f3 = (float)(j & 255) / 255.0F;
            GL11.glColor4f(f1 * par3, f2 * par3, f3 * par3, 1.0F);
        }
        
        renderer.setRenderBoundsFromBlock(block);
        int k;
        
		block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getHiddenBlockTextureFromSideAndMetadata(0, metadata));
        tessellator.draw();

        if (flag && renderer.useInventoryTint)
        {
            k = block.getRenderColor(metadata);
            f2 = (float)(k >> 16 & 255) / 255.0F;
            f3 = (float)(k >> 8 & 255) / 255.0F;
            float f7 = (float)(k & 255) / 255.0F;
            GL11.glColor4f(f2 * par3, f3 * par3, f7 * par3, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getHiddenBlockTextureFromSideAndMetadata(1, metadata));
        tessellator.draw();

        if (flag && renderer.useInventoryTint)
        {
            GL11.glColor4f(par3, par3, par3, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getHiddenBlockTextureFromSideAndMetadata(2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getHiddenBlockTextureFromSideAndMetadata(3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getHiddenBlockTextureFromSideAndMetadata(4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getHiddenBlockTextureFromSideAndMetadata(5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		Icon iconPre = renderer.overrideBlockTexture;
		
		if(revealed() && iconPre == null)
		{
			Icon icon;
			icon = ((BlockHiddenOre)block).getHiddenBlockTextureFromSideAndMetadata(0, world.getBlockMetadata(x, y, z));
			renderer.setOverrideBlockTexture(icon);
		}
		renderer.renderStandardBlock(block, x, y, z);
		
		renderer.overrideBlockTexture = iconPre;
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}
	
	protected boolean revealed()
	{
		return MechanicHiddenOres.revealHiddenGems;
	}

}
