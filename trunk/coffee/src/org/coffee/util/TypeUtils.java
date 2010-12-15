package org.coffee.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

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
	
	/**
	 * 获取Field的数据类型
	 * @param prop : 	
	 * @return  : 返回一个 Type的数据类型
	 */
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
	
}
