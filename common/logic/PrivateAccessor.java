package loecraftpack.common.logic;

import java.lang.reflect.Field;

public class PrivateAccessor {
	
	public static Object getPrivateObject(Object instance, String name)
	{
		return getPrivateObject(instance.getClass(), instance, name);
	}
	
	public static Object getPrivateObject(Class sourceClass, Object instance, String name)
	{
		Field hold;
		try
		{
			hold = sourceClass.getDeclaredField(name);
			hold.setAccessible(true);
			return hold.get(instance);
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean setPrivateVariable(Class sourceClass, Object instance, String varName, Object value)
	{
		try
		{
			if(!sourceClass.isAssignableFrom(instance.getClass())) throw new IllegalArgumentException("invalid Class");
			Field hold = sourceClass.getDeclaredField(varName);
			hold.setAccessible(true);
			hold.set(instance, value);
			return true;
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
