package loecraftpack.blocks.te;

import loecraftpack.enums.Dye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class ColoredBedTileEntity extends TileEntity
{
	public int id = 0;
	
	public ColoredBedTileEntity()
	{}
	
	public ColoredBedTileEntity(int id)
	{
		this.id = id;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		id = nbt.getInteger("bedId");
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
    {
		super.writeToNBT(nbt);
		nbt.setInteger("bedId", id);
    }
	
	@Override
	public Packet getDescriptionPacket()
    {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
    }
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
	{
		readFromNBT(packet.customParam1);
	}
}
