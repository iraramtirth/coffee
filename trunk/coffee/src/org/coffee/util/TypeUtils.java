package org.coffee.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class TypeUtils {
	public enum Type {
		Byte,
		Character,
		Short,
		Integer,
		Long,
		Float,
		Double,
		Boolean,
		String,
		Date
	}
	
	// 获取Field被映射的类型
	public static Type getMappedType(PropertyDescriptor prop) throws Exception{
		return getMappedType(prop.getPropertyType().getSimpleName());
	}
	
	public static  Type getMappedType(Field field) throws Exception{
		return getMappedType(field.getType().getSimpleName());
	}
	// 支持基本数据类型以及其封装类型
	public static Type getMappedType(String name) throws Exception{
		name = name.toLowerCase();
		if(name.contains("long")){
			return Type.Long;
		}
		if(name.contains("int")){
			return Type.Integer;
		}
		if(name.equals("date")){
			return Type.Date;
		}
		return Type.String;
	}
	
	/**
	 *  格式化日期类型 
	 */
	public static String parseDate(Object value){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(value);
		} catch (Exception e) {
			return null;
		}
	}
	
}
