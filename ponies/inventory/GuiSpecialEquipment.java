package loecraftpack.ponies.inventory;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StatCollector;

public class GuiSpecialEquipment extends InventoryEffectRenderer {

	public GuiSpecialEquipment(EntityPlayer entityPlayer)
    {
        super(new ContainerSpecialEquipment(entityPlayer));
        this.allowUserInput = true;
    }
	
	public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
    }
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	/*
        this.fontRenderer.drawString(StatCollector.translateToLocal("Special Equipment"), 8, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        */
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/loecraftpack/ponies/inventory/specialEquipment.png");
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

}
