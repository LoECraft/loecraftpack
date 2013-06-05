package loecraftpack.packet;

public class PacketIds
{
	//TODO make this pass thru a function, to confirm both: valid changes, and valid transactions
	/**TO SERVER: Client Edited Values*/ 
	public static final byte monolithEdit = -128;
	
	//TODO remove this and have the owner set by the server side, during block placement
	/**TO SERVER: Client Set Owner*/
	public static final byte monolithSetOwner = -127;
	
	/**TO BOTH: Custom Tile Entity Updates*/
	public static final byte monolithUpdate = -126;
	
	//TODO remove this and use the useAbility packet instead
	/**TO SERVER: Client Spawned FireBall*/
	public static final byte fireball = -125;
	
	/**TO CLIENT: Bed Pair Data Change*/
	public static final byte bedUpdate = -124;
	
	/**TO CLIENT: Custom Leaf Data Change*/
	public static final byte appleBloomUpdate = -123;
	
	/**TO SERVER: Use Player Ability*/
	public static final byte useAbility = -122;
	
	/**TO SERVER: Cycle Thru Inventory*/
	public static final byte subInventory = -121;
	
	//TODO remove this and have only server apply potion effects.
	/**TO SERVER: Client Directly Applied Potion Effect; For Testing Purposes */
	public static final byte applyPotionEffect = -120;
}
