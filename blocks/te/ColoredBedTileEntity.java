package loecraftpack.blocks.te;

import loecraftpack.logic.handlers.ColoredBedHandler;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ColoredBedTileEntity extends TileEntity
{
	public int id = 0;
	public String pairName = "";
	
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
		if (nbt.getString("pair").equals(""))
		{
			updatePairName();
			if (!pairName.equals(""))
				updateAdjacentBedPairName();
		}
		else
			pairName = nbt.getString("pair");
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
    {
		updatePairName();
		updateAdjacentBedPairName();
		
		super.writeToNBT(nbt);
		nbt.setInteger("bedId", id);
		nbt.setString("pair", pairName);
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
	
	public void updateAdjacentBedPairName()
	{
		int dir = BlockDirectional.getDirection(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		
		if (++dir > 3) //rotate left
        	dir = 0;
        
		TileEntity te = worldObj.getBlockTileEntity(xCoord - BlockBed.footBlockToHeadBlockMap[dir][0], yCoord, zCoord - BlockBed.footBlockToHeadBlockMap[dir][1]);
        
        if (te instanceof ColoredBedTileEntity)
        	((ColoredBedTileEntity)te).updatePairName();
        
        dir -= 2;
        
        if (dir < 0) //rotate right
        	dir += 4;
        
        te = worldObj.getBlockTileEntity(xCoord - BlockBed.footBlockToHeadBlockMap[dir][0], yCoord, zCoord - BlockBed.footBlockToHeadBlockMap[dir][1]);
        
        if (te instanceof ColoredBedTileEntity)
        	((ColoredBedTileEntity)te).updatePairName();
	}
	
	public void updatePairName()
	{
		if (worldObj == null)
			return;
		pairName = getPairName();
		System.out.println("Pair (" + worldObj.isRemote + "): " + pairName);
	}
	
	private String getPairName()
	{
		if (worldObj == null)
			return "";
		
        int dir = BlockDirectional.getDirection(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		
		String name; //This bed's name
		String pairName = ""; //Adjacent bed's name
		
        TileEntity te = worldObj.getBlockTileEntity(xCoord - BlockBed.footBlockToHeadBlockMap[dir][0], yCoord, zCoord - BlockBed.footBlockToHeadBlockMap[dir][1]);
        if (te != null)
        	name = ColoredBedHandler.iconNames.get(((ColoredBedTileEntity)te).id);
        else
        	name = "";
        
        
        if (++dir > 3) //rotate left
        	dir = 0;
        
        te = worldObj.getBlockTileEntity(xCoord - BlockBed.footBlockToHeadBlockMap[dir][0], yCoord, zCoord - BlockBed.footBlockToHeadBlockMap[dir][1]);
        
        if (te instanceof ColoredBedTileEntity)
        	pairName = ColoredBedHandler.getPairName(ColoredBedHandler.iconNames.get(((ColoredBedTileEntity)te).id), name);
        
        dir -= 2;
        
        if (dir < 0) //rotate right
        	dir += 4;
        
        te = worldObj.getBlockTileEntity(xCoord - BlockBed.footBlockToHeadBlockMap[dir][0], yCoord, zCoord - BlockBed.footBlockToHeadBlockMap[dir][1]);
        
        if (te instanceof ColoredBedTileEntity)
        	pairName = ColoredBedHandler.getPairName(name, ColoredBedHandler.iconNames.get(((ColoredBedTileEntity)te).id));
        
        return pairName;
	}
}
