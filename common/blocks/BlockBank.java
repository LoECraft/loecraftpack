package loecraftpack.common.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loecraftpack.LoECraftPack;
import loecraftpack.common.gui.GuiIds;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBank extends Block
{
	public BlockBank(int id)
	{
		super(id, Material.circuits);
		setUnlocalizedName("Bank");
		setCreativeTab(LoECraftPack.LoECraftTab);
		this.blockResistance = 2000;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer playerEntity, int w, float px, float py, float pz)
	{
		if (playerEntity.getCurrentEquippedItem() == null || playerEntity.getCurrentEquippedItem().itemID != 923)
			playerEntity.openGui(LoECraftPack.instance, GuiIds.BANK.ordinal(), world, x, y, z);
		else if (playerEntity.getCurrentEquippedItem().itemID == 923)
			/*Deposit Bit Code*/;
		
		return false;
	}
}
