package loecraftpack.common.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileProtectionMonolith extends TileEntity
{
	public int offsetX
			 , offsetZ
			 , width = 8
			 , length = 8
			 , offsetXInc = 0
			 , offsetZInc = 0
			 , widthInc = 0
			 , lengthInc = 0;
			 
	public List<String> Owners = new ArrayList<String>();
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
        offsetX = nbt.getInteger("ox");
        offsetZ = nbt.getInteger("oz");
        width = nbt.getInteger("w");
        length = nbt.getInteger("l");
        setOwners(nbt.getString("owners"));
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
    {
		super.writeToNBT(nbt);
        nbt.setInteger("ox", offsetX);
        nbt.setInteger("oz", offsetZ);
        nbt.setInteger("w", width);
        nbt.setInteger("l", length);
        nbt.setString("owners", getOwners());
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
	
	@SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }
	
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }
	
	public boolean pointIsProtected(int x, int z)
	{
		return (x >= (xCoord+offsetX-Math.ceil(width/2)) && x < (xCoord+offsetX+Math.floor(width/2))) &&
			   (z >= (zCoord+offsetZ-Math.ceil(length/2)) && z < (zCoord+offsetZ+Math.floor(length/2)));
	}
	
	public String getOwners()
	{
		String owners = "";
        for(int i = 0; i < Owners.size(); i++ )
        	owners += (i==0?"":"|")+Owners.get(i);
        return owners;
	}
	
	public void setOwners(String owners)
	{
		if (owners.equals(""))
        	Owners = new ArrayList(0);
        else if (owners.contains("|"))
        	Owners = new ArrayList(Arrays.asList(owners.split("\\|")));
        else
        	Owners = new ArrayList(Arrays.asList(owners));
	}
	
	public boolean isOwner(String username)
	{
		return Owners.contains(username);
	}
}
