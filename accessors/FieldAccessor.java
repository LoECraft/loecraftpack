package loecraftpack.accessors;

import java.lang.reflect.Field;


public class FieldAccessor<T>
{
	protected Field field;
	
	FieldAccessor(Class sourceClass, String name)
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
