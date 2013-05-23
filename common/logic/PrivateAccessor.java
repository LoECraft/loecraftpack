package loecraftpack.common.logic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * This class was created, to deal with vanilla Minecraft over-using private variables. (also to improve method access)
 */
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
