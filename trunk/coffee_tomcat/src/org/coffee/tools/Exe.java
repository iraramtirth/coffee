package  org.coffee.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Exe {
	
	/**
	 * 获取字段值[static field]
	 */
	public synchronized static Object getValue(Class<?> clazz, String fieldName){
		Object result = null;
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if(field.isAccessible() == false){
				result = field.get(null);
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @param obj
	 * @param methodName
	 * @param paramValues
	 */
	public synchronized static Object run(Object obj, String methodName, Object... paramValues) {
		Object result = null;
		try {
			Method[] methods = obj.getClass().getDeclaredMethods();
			for(Method method : methods){
				if(methodName.equals(method.getName())){
					method.setAccessible(true);
					result = method.invoke(obj, paramValues);
					break;
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 调用静态方法
	 */
	public synchronized static Object run(Class<?> clazz, String methodName, Object... paramValues) {
		Object result = null;
		Method[] methods = clazz.getDeclaredMethods();
		try {
			for(Method method : methods){
				if("test".equals(method.getName())){
					method.setAccessible(true);
					result = method.invoke(null, new Object[]{});
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
