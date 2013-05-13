package loecraftpack.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import loecraftpack.LoECraftPack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.Icon;

public class ItemPickaxeGem extends ItemTool {
	
	public static final Block[] blocksEffectiveAgainst = new Block[] {Block.cobblestone,        Block.stoneDoubleSlab,
																	  Block.stoneSingleSlab,    Block.stone,
																	  Block.sandStone,          Block.cobblestoneMossy,
																	  Block.oreIron,            Block.blockSteel,
																	  Block.oreCoal,            Block.blockGold,
																	  Block.oreGold,            Block.oreDiamond,
																	  Block.blockDiamond,       Block.ice, 
																	  Block.netherrack,         Block.oreLapis,
																	  Block.blockLapis,         Block.oreRedstone,
																	  Block.oreRedstoneGlowing, Block.rail,
																	  Block.railDetector,       Block.railPowered,
																	  Block.railActivator,      LoECraftPack.blockGemOre};
	
	public ItemPickaxeGem(int par1)
    {
        this(par1, EnumToolMaterial.IRON);
    }
	
	public ItemPickaxeGem(int par1, EnumToolMaterial par2EnumToolMaterial)
    {
        this(par1, par2EnumToolMaterial, blocksEffectiveAgainst);
    }
	
	public ItemPickaxeGem(int par1, EnumToolMaterial par2EnumToolMaterial, Block[] blocksEffectiveAgainst) {
		super(par1, 2, par2EnumToolMaterial, blocksEffectiveAgainst);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int index)
	{
		return iconIndex;
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
    	iconIndex = iconRegister.registerIcon("loecraftpack:tools/gemPickaxe");
	}
	
	
	public boolean canHarvestBlock(Block block)
    {
		return block == LoECraftPack.blockGemOre ?
				true :
				block == Block.obsidian ?
					this.toolMaterial.getHarvestLevel() == 3 :
					(block != Block.blockDiamond && block != Block.oreDiamond ?
						(block != Block.oreEmerald && block != Block.blockEmerald ?
							(block != Block.blockGold && block != Block.oreGold ?
								(block != Block.blockSteel && block != Block.oreIron ?
									(block != Block.blockLapis && block != Block.oreLapis ?
										(block != Block.oreRedstone && block != Block.oreRedstoneGlowing ?
											(block.blockMaterial == Material.rock ?
												true :
												(block.blockMaterial == Material.iron ?
													true :
													block.blockMaterial == Material.anvil
												)
											):
											this.toolMaterial.getHarvestLevel() >= 2
										):
										this.toolMaterial.getHarvestLevel() >= 1
									):
									this.toolMaterial.getHarvestLevel() >= 1
								):
								this.toolMaterial.getHarvestLevel() >= 2
							):
							this.toolMaterial.getHarvestLevel() >= 2
						): 
						this.toolMaterial.getHarvestLevel() >= 2
					);
    }
	
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        return par2Block != null && (par2Block.blockMaterial == Material.iron || par2Block.blockMaterial == Material.anvil || par2Block.blockMaterial == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(par1ItemStack, par2Block);
    }
}
