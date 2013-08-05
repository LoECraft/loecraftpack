package loecraftpack.ponies.abilities.active;

import java.io.DataInputStream;
import java.io.IOException;

import loecraftpack.enums.Race;
import loecraftpack.packet.PacketHelper;
import loecraftpack.packet.PacketIds;
import loecraftpack.ponies.abilities.Ability;
import loecraftpack.ponies.abilities.AbilityPlayerData;
import loecraftpack.ponies.abilities.ActiveAbility;
import loecraftpack.ponies.abilities.projectiles.Fireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class AbilityFireball extends ActiveAbility
{
	public AbilityFireball()
	{
		super("Fireball", Race.UNICORN, 100, 3, 1);
	}

	@Override
	protected boolean castSpellClient(EntityPlayer player, World world)
	{
		AbilityPlayerData.clientData.addEnergy(-energyCost, true);
		AbilityPlayerData.clientData.addAfterImage(energyCost);
		return true;
	}
	
	@Override
	protected boolean castSpellServer(EntityPlayer player, World world) {
		energyCost = (int)(this.getEnergyCost(player));
		System.out.println("FireBall: "+energyCost+" "+playerData.energy);
		if(playerData.energy>=energyCost)
		{
			Fireball fireball = new Fireball(player.worldObj, player, player.getLookVec().xCoord/10f, player.getLookVec().yCoord/10f, player.getLookVec().zCoord/10f);
			player.worldObj.spawnEntityInWorld(fireball);
			playerData.addEnergy(-energyCost, false);
			
			return true;
		}
		return false;
	}
}
