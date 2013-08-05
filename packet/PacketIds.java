package loecraftpack.packet;

public class PacketIds
{
	//Do: PacketIds - make this pass thru a function, to confirm both: valid changes, and valid transactions
	/**TO SERVER: Client Edited Values for MonoLith<p>
	 * <b> MonoLith Location vars:</b><br>
	 * - X-Coord (Int)<br>
	 * - Y-Coord (Int)<br>
	 * - Z-Coord (Int)<br>
	 * <b> Protection Zone vars:</b><br>
	 * - Width (Int)<br>
	 * - Length (Int)<br>
	 * - X-Offset (Int)<br>
	 * - Z-Offset (Int)<br>
	 * <b> Other vars:</b><br>
	 * - Owners (String)
	 */ 
	public static final byte monolithEdit = -128;
	
	/**TO BOTH: Custom Tile Entity Updates For MonoLith <p>
	 *  TO SERVER:<br>
	 * <b> MonoLith Location vars:</b><br>
	 * - X-Coord (Int)<br>
	 * - Y-Coord (Int)<br>
	 * - Z-Coord (Int)<p>
	 *  TO CLIENT:<br>
	 * <b> Location vars:</b><br>
	 * - X-Coord (Int)<br>
	 * - Y-Coord (Int)<br>
	 * - Z-Coord (Int)<br>
	 * <b> Protection Zone vars:</b><br>
	 * - Width (Int)<br>
	 * - Length (Int)<br>
	 * - X-Offset (Int)<br>
	 * - Z-Offset (Int)<br>
	 * <b> Other vars:</b><br>
	 * - Owners (String)
	 */
	public static final byte monolithUpdate = -127;
	
	/**TO CLIENT: Bed Pair Data Change <p>
	 * <b> Location vars:</b><br>
	 * - X-Coord (Int)<br>
	 * - Y-Coord (Int)<br>
	 * - Z-Coord (Int)<br>
	 * <b> Pair Info vars:</b><br>
	 * - pairID (Int)<br>
	 * - pairSide (Int)
	 */
	public static final byte bedUpdate = -126;
	
	/**TO CLIENT: Custom Leaf Data Change <p>
	 * <b> Location vars:</b><br>
	 * - X-Coord (Int)<br>
	 * - Y-Coord (Int)<br>
	 * - Z-Coord (Int)<br>
	 * <b> Other vars:</b><br>
	 * - New block Id (int)
	 */
	public static final byte appleBloomUpdate = -125;
	
	/**TO BOTH: Use Player Ability <p>
	 *  TO SERVER:<br>
	 * - activeId (Int)<br>
	 * - undefined variables (handle case by case)<p>
	 *  TO CLIENT: (deny packet)<br>
	 * - activeId (Int)<br>
	 * - energy (float)<br>
	 */
	public static final byte useAbility = -124;
	
	/**TO CLIENT: Set Player Ability State <p>
	 * - ability (int)<br>
	 * - mode (int)
	 */
	public static final byte modeAbility = -123;
	
	/**TO SERVER: Cycle Thru Inventory <p>
	 * - Current Gui (int)
	 */
	public static final byte subInventory = -122;
	
	/**TO CLIENT: Apply Custom Stat information to a player <p>
	 * <b> Stat vars:</b><br>
	 * - Race (int)<br>
	 * <b> Other vars:</b><br>
	 * - Player (String)
	 */
	public static final byte applyStats = -121;
	public static final byte addPlayer = -120;
	public static final byte statUpdate = -119;
	
	//Do: PacketIds - applyPotionEffect - remove this and have only server apply potion effects.
	/**TO SERVER: Client Directly Applied Potion Effect; For Testing Purposes */
	public static final byte applyPotionEffect = -118;
		
	//Do: PacketIds - monolithSetOwner - remove this and have the owner set by the server side, during block placement
	/**TO SERVER: Client Set Owner*/
	public static final byte monolithSetOwner = -117;

	
}
