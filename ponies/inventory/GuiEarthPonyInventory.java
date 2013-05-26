package loecraftpack.ponies.inventory;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.InventoryEffectRenderer;
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
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
	/*
	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        if (par1Slot != null)
        {
            par2 = par1Slot.slotNumber;
        }

        this.mc.playerController.windowClick(this.inventorySlots.windowId, par2, par3, par4, this.mc.thePlayer);
    }*/
}
