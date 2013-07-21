package loecraftpack.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHelper
{
	public static Packet250CustomPayload Make(String channel, Object... objects)
	{
		int bufferSize = 0;
		
		for(Object o : objects)
		{
			Class clazz = o.getClass();
			if (clazz == Byte.class)
				bufferSize += 1;
			else if (clazz == Integer.class)
				bufferSize += 4;
			else if (clazz == Float.class)
				bufferSize += 4;
			else if (clazz == Double.class)
				bufferSize += 8;
			else if (clazz == Long.class)
				bufferSize += 8;
			else if (clazz == String.class)
				bufferSize += 2 + ((String)o).length()*2;
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bufferSize);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try
		{
			for(Object o : objects)
			{
				Class clazz = o.getClass();
				if (clazz == Byte.class)
					outputStream.writeByte((Byte)o);
				else if (clazz == Integer.class)
					outputStream.writeInt((Integer)o);
				else if (clazz == Float.class)
					outputStream.writeFloat((Float)o);
				else if (clazz == Double.class)
					outputStream.writeDouble((Double)o);
				else if (clazz == Long.class)
					outputStream.writeLong((Long)o);
				else if (clazz == String.class)
				{
					outputStream.writeShort(((String)o).length());
					outputStream.writeChars((String)o);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload(channel, bos.toByteArray());
		
		return packet;
	}
	
	public static String readString(DataInputStream data)
	{
		
		StringBuilder str = new StringBuilder();
        
        try
        {
        	if (data.available() < 2)
    			return "";
        	
        	short length = data.readShort();
        	
            for(int i = 0; i < length; i++)
            {
            	str.append(data.readChar());
            	if (data.available() < 2)
            		break;
            }
        	
            return str.toString();
        }
        catch(IOException e)
        {
        	return "";
        }
	}
}
