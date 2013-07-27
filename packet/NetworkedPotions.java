package loecraftpack.packet;

import java.util.HashMap;

import loecraftpack.LoECraftPack;
import cpw.mods.fml.common.network.PacketDispatcher;

public class NetworkedPotions
{
	
	public static final byte oreVision = -128;
	
	public static HashMap potions = new HashMap<Byte, Integer>(){{
		//EX: put(oreVision, LoECraftPack.potionOreVision.id);
	}};
	
	public static void applyEffect(byte potionId, int duration, int strength)
	{
		PacketDispatcher.sendPacketToServer(PacketHelper.Make("loecraftpack", PacketIds.applyPotionEffect, potionId, duration, (byte)strength));
	}
}
