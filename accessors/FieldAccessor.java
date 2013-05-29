package loecraftpack.accessors;

import java.lang.reflect.Field;


public class FieldAccessor<T>
{
	protected Field field;
	
	/**
	 * create a FieldAccessor to manage access to the field
	 * @param sourceClass - the class it's declared in.
	 * @param name - the name of the field
	 */
	public FieldAccessor(Class sourceClass, String name)
	{
		field = PrivateAccessor.getPrivateField(sourceClass, name);
	}
	
	public void set(Object instance, Object value)
	{
		try 
		{
			field.set(instance, value);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public T get(Object instance)
	{
		try 
		{
			return (T)field.get(instance);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Usable by: Integer, Float, Double, Long
	 */
	public T Increment(Object instance)
	{
		T result = get(instance);
		if (result instanceof Integer)
			set(instance, ((Integer)result)+1 );
		else if (result instanceof Float)
			set(instance, ((Float)result)+1 );
		else if (result instanceof Double)
			set(instance, ((Double)result)+1 );
		else if (result instanceof Long)
			set(instance, ((Long)result)+1 );
		return get(instance);
	}
	
	/**
	 * Usable by: Integer, Float, Double, Long
	 */
	public T Decrement(Object instance)
	{
		T result = get(instance);
		if (result instanceof Integer)
			set(instance, ((Integer)result)-1 );
		else if (result instanceof Float)
			set(instance, ((Float)result)-1 );
		else if (result instanceof Double)
			set(instance, ((Double)result)-1 );
		else if (result instanceof Long)
			set(instance, ((Long)result)-1 );
		return get(instance);
	}
	
	
	
	

}
