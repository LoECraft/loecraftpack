package loecraftpack.ponies.abilities;


public class EnergyUsePiece<T> {
	
	public int id;
	public T cost;
	public long timestamp;
	
	EnergyUsePiece(int id, T cost, long timestamp)
	{
		this.id = id;
		this.cost = cost;
		this.timestamp = timestamp;
	}

}
