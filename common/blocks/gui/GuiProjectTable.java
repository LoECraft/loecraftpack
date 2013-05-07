package loecraftpack.common.blocks.gui;

import loecraftpack.common.blocks.ContainerProjectTable;
import loecraftpack.common.blocks.TileProjectTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiProjectTable extends GuiContainer
{
	public GuiProjectTable(InventoryPlayer inventoryPlayer, World world, int x, int y, int z)
    {
        super(new ContainerProjectTable(inventoryPlayer, (TileProjectTable)world.getBlockTileEntity(x, y, z), world, x, y, z));
        ySize = 215;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("Project Table"), 28, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/loecraftpack/common/blocks/gui/projecttable.png");
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
