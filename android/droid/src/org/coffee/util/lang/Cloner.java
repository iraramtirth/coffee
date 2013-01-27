package org.coffee.util.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 利用反射拷贝对象 深拷贝
 * 
 * @author coffee
 */
public class Cloner {

	@SuppressWarnings("unchecked")
	public static <T> T execute(T obj) {
		if (obj == null) {
			return obj;
		}
		T cloneObj = null;
		try {
			Constructor<?>[] cons = obj.getClass().getDeclaredConstructors();
			for (Constructor<?> c : cons) {
				c.setAccessible(true);
			}
			cloneObj = (T) obj.getClass().newInstance();
			for (Field field : obj.getClass().getFields()) {
				field.setAccessible(true);
				field.set(cloneObj, field.get(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cloneObj;
	}

	public static void main(String[] args) {
		// Object ob = new Object();
		// Clon
	}
}
