package loecraftpack.common.blocks.render;

import java.util.ArrayList;
import java.util.List;

public class RenderSet
{
	public int xChunk;
	public int yChunk;
	public int zChunk;
	public boolean dirty;
	public boolean drop;
	List<int[]> blocks;
	
	RenderSet(int x, int y, int z)
	{
		xChunk = x/16;
		yChunk = y/16;
		zChunk = z/16;
		dirty = false;
		drop = false;
		blocks = new ArrayList<int[]>();
		
	}
	
	public void addBlock(int x, int y, int z)
	{
		if(dirty)
			clean();
		for(int[] coords : blocks)
		{
			if (coords != null && coords[0] == x && coords[1] == y && coords[2] == z)
			{
				System.out.println("keep");
				return;
			}
		}
		blocks.add(new int[]{x, y, z});
	}
	
	public void clean()
	{
		dirty=false;
		blocks.clear();
	}
}
