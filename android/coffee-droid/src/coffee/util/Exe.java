package coffee.util;

import java.lang.reflect.Method;

public class Exe {
	
	/**
	 * 
	 */
	public synchronized static void get(){
		
	}
	
	/**
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
}
