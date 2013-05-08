package loecraftpack.common.items;

import loecraftpack.LoECraftPack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMusicDisc extends ItemRecord
{
	private static int idNum = 2012;
	private String composer;
	private String unformattedRecordName;
	
	public ItemMusicDisc(String composer, String recordName, int id)
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
		this.iconIndex = iconRegister.registerIcon("loecraftpack:records/record_" + this.recordName);
	}
	
	public static void AddMusicDisc(String composer, String recordName)
	{
		ItemMusicDisc disc = new ItemMusicDisc(composer, recordName, idNum++);
		LanguageRegistry.instance().addStringLocalization("item.record.record_" + disc.recordName + ".name", "Music Disc");
	}
}
