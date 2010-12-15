package org.coffee.util;

import java.beans.PropertyDescriptor;

import org.coffee.hibernate.dao.util.Configuration.MappedType;
/**
 * 
 * @author wangtao
 *
 */
public class ClassUtils {
	/**
	 * 获取指定属性的Field类型 
	 * @param <T>
	 * @param prop
	 */
	public static <T> MappedType getMappedType(PropertyDescriptor prop) throws Exception{
		if(prop.getPropertyType().getSimpleName().equals("Long")){
			return MappedType.Long;
		}
		if(prop.getPropertyType().getSimpleName().equals("Integer")){
			return MappedType.Integer;
		}
		if(prop.getPropertyType().getSimpleName().equals("Date")){
			return MappedType.Date;
		}
		return MappedType.String;
	}
}
