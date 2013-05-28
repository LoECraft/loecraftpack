package loecraftpack.accessors;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * This class was created, to deal with vanilla Minecraft over-using private variables. (also to improve method access)
 */
public class PrivateAccessor {
	
	
	/**
	 * shortened form of getPrivateObject(Class sourceClass, Object instance, String name)
	 * the instances class must be where the Object/Variable is declared
	 */
	public static Object getPrivateObject(Object instance, String name)
	{
		return getPrivateObject(instance.getClass(), instance, name);
	}
	
	/**
	 * Acquires the contents of the Field
	 * @param sourceClass - where the variable/object is declared
	 * @param instance - the instance containing it
	 * @param name - name of the Field
	 * @return - contents of the Field
	 */
	public static Object getPrivateObject(Class sourceClass, Object instance, String name)
	{
		try 
		{
			return getPrivateField(sourceClass, name).get(instance);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Attempts to return the field it's-self so that you can interact with it freely.
	 * Use FieldAccessor and ValueAccessor to make using the field easier.
	 * @param sourceClass - where the Field is declared
	 * @param name - name of the Field
	 * @return - the Field
	 */
	public static Field getPrivateField(Class sourceClass, String name)
	{
		Field hold;
		try
		{
			hold = sourceClass.getDeclaredField(name);
			hold.setAccessible(true);
			return hold;
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
		
		return null;
	}
	
	/**
	 * Set the content of a Field
	 * @param sourceClass - where the Field is declared
	 * @param instance - the instance containing it
	 * @param varName - name of the Field
	 * @param value - the contents to set it to.
	 * @return - whether or not it succeeded
	 */
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
	
	public static Method getMethod(Class sourceClass, String methodName, Class... args)
	{
		try {
			Method hold = null;
			Class targetClass = sourceClass;
			while (targetClass != null)
			{
				try {
					hold = targetClass.getDeclaredMethod(methodName, args);
				}
				catch (NoSuchMethodException e) {}
				targetClass = targetClass.getSuperclass();
			}
			if (hold == null)
			{
				System.out.print("[Private Accessor] No Such Method: "+sourceClass+"."+methodName+"(");
				for(int i=0; i<args.length; i++)
				{
					System.out.print((i==0? "":", ")+args[i].toString());
				}
				System.out.println(")");
				return null;
			}
			hold.setAccessible(true);
			return hold;
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object invokeMethod(Method method, Object instance, Object... args)
	{
		try {
			return method.invoke(instance, args);
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
