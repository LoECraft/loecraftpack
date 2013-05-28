package loecraftpack.accessors;

public class ValueAccessor<T>
{
	protected FieldAccessor<T> field;
	protected Object instance;
	
	ValueAccessor(Object instance, FieldAccessor<T> field)
	{
		this.instance = instance;
		this.field = field;
	}
	
	//for static fields
	ValueAccessor(Class sourceClass, String name)
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
	
	public T Increment()
	{
		return field.Increment(instance);
	}
	
	public T Decrement()
	{
		return field.Decrement(instance);
	}
}
