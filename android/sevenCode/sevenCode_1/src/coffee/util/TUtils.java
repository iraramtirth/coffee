package coffee.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 反射用到的工具 
 * @author wangtao
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
 
	public static Object setValue(Object obj, String fieldName, Object value) {
		try {
			if(obj == null || value == null){
				return obj;
			}
			Field field = obj.getClass().getDeclaredField(fieldName);
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
