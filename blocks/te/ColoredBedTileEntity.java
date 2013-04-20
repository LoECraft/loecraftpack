package loecraftpack.blocks.te;

import loecraftpack.blocks.ColoredBedBlock;
import loecraftpack.enums.Dye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class ColoredBedTileEntity extends TileEntity
{
	private Dye color = Dye.White;
	ColoredBedBlock block;
	
	public ColoredBedTileEntity(ColoredBedBlock block)
	{
		this.block = block;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		color = Dye.values()[MathHelper.clamp_int(nbt.getInteger("color"), 0, 15)];
		block.color = color;
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
    {
		super.writeToNBT(nbt);
		nbt.setInteger("color", color.ordinal());
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
