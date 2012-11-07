package coffee.database.core;

import java.lang.reflect.Field;

/**
 * 反射用到的工具
 * 
 * @author coffee
 */
public class TUtils {

	/**
	 * 判断给定的field名是否存在于指定的class
	 * 
	 * @param fieldName
	 * @return
	 */
	public static boolean isField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if (field != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取某个字段的值
	 */
	public static <T> Object getValue(T obj, String fieldName) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object setValue(Object obj, String fieldName,
			Object fieldValue) {
		try {
			if (obj == null || fieldValue == null) {
				return obj;
			}
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (field != null) {
				Object newVal = fieldValue;
				if (field.getType().isPrimitive()) {
					String type = field.getType().toString();
					if (type.contains("long")) {
						newVal = Long.valueOf(fieldValue + "");
					} else if (type.contains("int")) {
						newVal = Integer.valueOf(fieldValue + "");
					} else if (type.contains("float")) {
						newVal = Float.valueOf(fieldValue + "");
					} else if (type.contains("double")) {
						newVal = Double.valueOf(fieldValue + "");
					}
				}
				field.setAccessible(true);
				field.set(obj, newVal);
			}
		} catch (NoSuchFieldException se) {
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}
