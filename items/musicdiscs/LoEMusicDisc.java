package loecraftpack.items.musicdiscs;

import java.util.ArrayList;
import java.util.List;

import loecraftpack.LoECraftPack;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LoEMusicDisc extends ItemRecord
{
	private static int idNum = 2012;
	private String composer;
	private String unformattedRecordName;
	
	public LoEMusicDisc(String composer, String recordName, int id)
	{
		super(id, recordName.replace(" ", "").toLowerCase());
		unformattedRecordName = recordName;
		this.setCreativeTab(LoECraftPack.LoECraftTab);
		this.setUnlocalizedName("record");
		this.composer = composer;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getRecordTitle()
	{
		return composer + " - " + unformattedRecordName;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack iconNamestack)
	{
		return super.getUnlocalizedName() + ".record_" + this.recordName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.registerIcon("loecraftpack:record_" + this.recordName);
	}
	
	public static void AddMusicDisc(String composer, String recordName)
	{
		LoEMusicDisc disc = new LoEMusicDisc(composer, recordName, idNum++);
		LanguageRegistry.instance().addStringLocalization("item.record.record_" + disc.recordName + ".name", "Music Disc");
	}
}
