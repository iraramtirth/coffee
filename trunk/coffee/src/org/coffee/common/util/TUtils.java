package org.coffee.common.util;

import java.lang.reflect.Field;


/**
 * 反射用到的工具 
 * @author 王涛
 */
public class TUtils {
	/**
	 * 判断给定的field名是否存在于指定的class
	 * @param filedString
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
	 * @param <T>
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static <T> Object getValue(T obj, String fieldName){
		String firstChar = fieldName.charAt(0)+"";
		try {
			String methodName = "get" + fieldName.replaceFirst(".", firstChar.toUpperCase());
			return obj.getClass().getMethod(methodName, new Class[]{}).invoke(obj, new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 给某字段赋值:返回赋值后的object
	 * @param <T>
	 * @param obj ：要赋值的对象
	 * @param fieldName : 要赋值的对象的字段
	 * @param value	: ....字段值
	 * @param valueClass : 如果要赋的值为基本数据类型， 则该值必须指定，如 int.class
	 * @return
	 */
	public static <T> Object setValue(T obj, String fieldName, T value, Class<?>... valueClass){
		String firstChar = fieldName.charAt(0)+"";
		try {
			String methodName = "set" + fieldName.replaceFirst(".", firstChar.toUpperCase());
			Class<?>[] paramClass = new Class[]{value.getClass()};
			if(valueClass.length > 0){
				// 对于基本数据类型，必须指定
				paramClass = new Class[]{valueClass[0]};
			}
			obj.getClass().getMethod(methodName,paramClass ).invoke(obj, new Object[]{value});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	static class User{
		private int id;
		private String username;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
	}
	
	public static void main(String[] args) {
		User user = new User();
		user.setId(1111);
		user.setUsername("xxxxxx");
		
		System.out.println(TUtils.isField(User.class, "sss"));
		
		System.out.println(TUtils.getValue(user, "username"));
		
		TUtils.setValue(user, "id", (int)2222, int.class);
		
		System.out.println(TUtils.getValue(user, "id"));
	}
}
