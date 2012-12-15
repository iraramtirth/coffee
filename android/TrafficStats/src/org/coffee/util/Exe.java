package org.coffee.util;

import java.lang.reflect.Method;

/**
 * 利用反射执行方法
 * 
 * @author coffee
 */
public class Exe {

	/**
	 * 
	 */
	public synchronized static void get() {

	}

	/**
	 * @param obj
	 *            ： 如果是静态类, 则传入 Class.toString()
	 * @param methodName
	 * @param paramValues
	 */
	public synchronized static Object run(Object obj, String methodName,
			Object... paramValues) {

		Object result = null;
		try {
			Class<?> objClass = null;
			if (obj instanceof String) {
				// 加载静态类
				objClass = Class.forName(obj + "");
			}
			else
			{
				objClass = obj.getClass();
			}

			Method[] methods = objClass.getDeclaredMethods();
			for (Method method : methods) {
				if (methodName.equals(method.getName())) {
					method.setAccessible(true);
					result = method.invoke(obj, paramValues);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
