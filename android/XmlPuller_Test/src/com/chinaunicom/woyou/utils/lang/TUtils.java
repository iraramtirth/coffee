package com.chinaunicom.woyou.utils.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * 反射用到的工具 
 * @author coffee
 */
public class TUtils {
	/**
	 * 判断给定的field名是否存在于指定的class
	 * @param fieldName
	 * @return
	 */
	public static boolean isField(Class<?> clazz, String fieldName){
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if(field != null){
				return true;
			}
		} catch (Exception e) {
		} 
		return false;
	}
	
	/**
	 * 根据字段名 "递归" 查找 Field
	 * 
	 * @param clazz
	 *            : 子/父类
	 * @param fieldName
	 *            ： 字段名
	 */
	public static Field getFieldByRecursion(Class<?> clazz, String fieldName) {
		try {
			try
			{
				Field field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException e) {
				// 如果当前类 不存在则 递归父类
				Class<?> superClazz = clazz.getSuperclass();
				if (superClazz != null) {
					return getFieldByRecursion(superClazz, fieldName);
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 获取某个字段的值
	 */
	public static <T> Object getValue(T obj, String fieldName){
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	/**
	 * 
	 * @param obj
	 * @param fieldName
	 * @param paramsValues
	 * @return
	 */
	public static <T> T addToCollection(T obj, String fieldName, Object... paramsValues)
	{
		Object listVal = getValue(obj, fieldName);
		if(listVal == null)
		{
			listVal = new ArrayList<T>();
		}
		//执行list的addAll方法
		execute(listVal, "addAll", paramsValues);
		//执行setXXXList
		setValue(obj, fieldName, listVal);
		return obj;
	}
	
	
	/**
	 * @param obj
	 * @param methodName
	 * @param paramValues
	 */
	public synchronized static Object execute(Object obj, String methodName, Object... paramValues) {
		Object result = null;
		try {
			Method method = null;
			if(paramValues.length > 0)
			{
				Class<?>[] paramTypes = new Class[paramValues.length];
				for (int i = 0; i < paramTypes.length; i++) {
					paramTypes[i] = paramValues[0].getClass();
				}
				method = obj.getClass().getDeclaredMethod(methodName, paramTypes);
			}
			else
			{
				method = obj.getClass().getDeclaredMethod(methodName);
			}
			method.setAccessible(true);
			result = method.invoke(obj, paramValues);
		}  catch (Exception e) {
			if(e instanceof NoSuchMethodException)
			{
				try {
					Method[] methods = obj.getClass().getDeclaredMethods();
					for(Method method : methods){
						/**
						 * 因为涉及到多态，而且好多参数的类型为父类，但实际传入的大都是子类的对象， 所以
						 * 在这里只比较参数的个数
						 */
						if(methodName.equals(method.getName())
								&& paramValues.length == method.getParameterTypes().length){
							method.setAccessible(true);
							result = method.invoke(obj, paramValues);
							break;
						}
					}
				}  catch (Exception e1) {
					e1.printStackTrace();
				}
				return result;
			}
		}
		return result;
	}
	
	public static Object setValue(Object obj, String fieldName, Object value) {
		try {
			if(obj == null || value == null){
				return obj;
			}
			Field field = getFieldByRecursion(obj.getClass(), fieldName);
			if (field != null) {
				Object newVal = value;
				if(field.getType().isPrimitive()){
					String type = field.getType().toString();
					if(type.contains("long")){
						newVal = Long.valueOf(value + "");
					}
					else if(type.contains("int")){
						newVal = Integer.valueOf(value + "");
					}
					else if(type.contains("float")){
						newVal = Float.valueOf(value + "");
					}
					else if(type.contains("double")){
						newVal = Double.valueOf(value + "");
					}
				}
				String methodName = "set" + fieldName.substring(0,1).toUpperCase()
						+ fieldName.substring(1);
				Method method = obj.getClass().getMethod(methodName, new Class[]{field.getType()});
				method.invoke(obj, newVal);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obj;
	}
	
}
