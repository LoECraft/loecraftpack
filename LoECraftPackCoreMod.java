package loecraftpack;

import java.lang.reflect.Field;

import java.io.File;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

public class LoECraftPackCoreMod extends DummyModContainer {

	public LoECraftPackCoreMod()
	{
		super(new ModMetadata());
		/* ModMetadata is the same as mcmod.info */
		ModMetadata myMeta = super.getMetadata();
		myMeta.authorList = Arrays.asList(new String[] { "Rundo0" });
		myMeta.description = "LoECraftPack's access transformer mod";
		myMeta.modId = "loecraftpackcore";
		myMeta.version = "1.0";
		myMeta.name = "LoECraft Pack Core";
        myMeta.url = "";
	}
	
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	/*
	 * Use this in place of @Init, @Preinit, @Postinit in the file.
	 */
	@Subscribe                 /* Remember to use the right event! */
	public void onServerStarting(FMLServerStartingEvent ev)
	{
		
	}
	
	

}
