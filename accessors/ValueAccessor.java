package loecraftpack.accessors;

public class ValueAccessor<T>
{
	protected FieldAccessor<T> field;
	protected Object instance;
	
	/**
	 * create non-static value access
	 * @param instance - instance it's instantiated in.
	 * @param field - static FieldAccessor.
	 */
	public ValueAccessor(Object instance, FieldAccessor<T> field)
	{
		this.instance = instance;
		this.field = field;
	}
	
	/**
	 * create static value access
	 * @param sourceClass - the class it's declared in.
	 * @param name - the name of the field
	 */
	public ValueAccessor(Class sourceClass, String name)
	{
		this.instance = null;
		this.field = new FieldAccessor<T>(sourceClass, name);
	}
	
	public void set(Object value)
	{
		field.set(instance, value);
	}
	
	public T get()
	{
		return field.get(instance);
	}
	
	/**
	 * Usable by: Integer, Float, Double, Long
	 */
	public T Increment()
	{
		return field.Increment(instance);
	}
	
	/**
	 * Usable by: Integer, Float, Double, Long
	 */
	public T Decrement()
	{
		return field.Decrement(instance);
	}
}
