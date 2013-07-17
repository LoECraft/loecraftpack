package loecraftpack.common.asm;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class LoEAccessTransformer extends AccessTransformer
{
    public LoEAccessTransformer() throws IOException
    {
    	super("LoECraftPack_at.cfg");
	}
}
