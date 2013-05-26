package loecraftpack.ponies.inventory;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StatCollector;

public class GuiEarthPonyInventory extends InventoryEffectRenderer {
	
	public GuiEarthPonyInventory(EntityPlayer entityPlayer)
    {
        super(new ContainerEarthInventory(entityPlayer));
        this.allowUserInput = true;
    }
	
	public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
    }
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("Extend Inventory"), 8, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/loecraftpack/ponies/inventory/earthInventory.png");
        int k = this.guiLeft;
        int l = this.guiTop;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
