package loecraftpack.ponies.abilities;


public class EnergyUsePiece<T> {
	
	public int id;
	public T cost;
	public int timestamp;
	
	EnergyUsePiece(int id, T cost, int timestamp)
	{
		this.id = id;
		this.cost = cost;
		this.timestamp = timestamp;
	}

}
