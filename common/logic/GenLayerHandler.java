package loecraftpack.common.logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import loecraftpack.common.worldgen.CustomBiomeGenLayer;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;


/***********************************************/
/***This class handles our custom gen. layers***/
/******requires edit to core files to work******/
/***********************************************/

public class GenLayerHandler {
	
	protected static List<Class> customBiomeLayers = new ArrayList<Class>();
	protected static List<Class> customBiomeSubLayers = new ArrayList<Class>();
	
	public static boolean addCustomBiomeLayerClass(Class layerClass)
	{
		if(!CustomBiomeGenLayer.class.isAssignableFrom(layerClass))
		{
			System.out.println("-----!!!!!---invaled biomelayer--"+layerClass.getName()+"---!!!!-----");
			return false;
		}
		return customBiomeLayers.add(layerClass);
	}
	
	public static boolean addCustomBiomeSubLayerClass(Class layerClass)
	{
		if(!CustomBiomeGenLayer.class.isAssignableFrom(layerClass))
		{
			System.out.println("-----!!!!!---invaled biomelayer--"+layerClass.getName()+"---!!!!-----");
			return false;
		}
		return customBiomeSubLayers.add(layerClass);
	}
	
	public static boolean hasBiomeLayerClass(Class layer)
	{
		return customBiomeLayers.contains(layer);
	}
	
	public static boolean hasBiomeSubLayerClass(Class layer)
	{
		return customBiomeSubLayers.contains(layer);
	}
	
	public static GenLayer applyCustomBiomeLayers(long par1 ,GenLayer layers, WorldType worldType)
	{
		for(Class layerClass : customBiomeLayers)
		{
			System.out.print("adding GenLayer: "+layerClass.getName());
			try 
			{
				Constructor con = layerClass.getConstructor(long.class, GenLayer.class, WorldType.class);
				layers = (CustomBiomeGenLayer)con.newInstance(par1, layers, worldType);
				System.out.println("  -GOOD");
			} catch (InstantiationException e) {
				System.out.println("  -BAD1");
			} catch (IllegalAccessException e) {
				System.out.println("  -BAD2");
			} catch (IllegalArgumentException e) {
				System.out.println("  -BAD3");
			} catch (InvocationTargetException e) {
				System.out.println("  -BAD4");
			} catch (NoSuchMethodException e) {
				System.out.println("  -BAD5");
				e.printStackTrace();
			} catch (SecurityException e) {
				System.out.println("  -BAD6");
			}
			
		}
		return layers;
	}
	
	public static GenLayer applyCustomBiomeSubLayers(long par1 ,GenLayer layers, WorldType worldType)
	{
		for(Class layerClass : customBiomeSubLayers)
		{
			System.out.print("adding GenLayer: "+layerClass.getName());
			try 
			{
				Constructor con = layerClass.getConstructor(long.class, GenLayer.class, WorldType.class);
				layers = (CustomBiomeGenLayer)con.newInstance(par1, layers, worldType);
				System.out.println("  -GOOD");
			} catch (InstantiationException e) {
				System.out.println("  -BAD1");
			} catch (IllegalAccessException e) {
				System.out.println("  -BAD2");
			} catch (IllegalArgumentException e) {
				System.out.println("  -BAD3");
			} catch (InvocationTargetException e) {
				System.out.println("  -BAD4");
			} catch (NoSuchMethodException e) {
				System.out.println("  -BAD5");
				e.printStackTrace();
			} catch (SecurityException e) {
				System.out.println("  -BAD6");
			}
			
		}
		return layers;
	}

}
