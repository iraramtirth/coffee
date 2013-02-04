package org.coffee.util.xml.parser;

import java.lang.reflect.Field;

import org.coffee.util.sqlite.TUtils;
import org.coffee.util.xml.parser.annotation.GenericType;
import org.coffee.util.xml.parser.annotation.XmlElement;
import org.coffee.util.xml.parser.annotation.XmlRootElement;

/**
 * 该方法是基于注解的
 */
public class TXmlUtils extends TUtils{
	/**
	 * 判断节点是否是Root元素; 对应的是class级别的
	 * 
	 * @param clazz
	 * @param elemName
	 * @return
	 */
	public static boolean isRootElement(Class<?> clazz, String elemName) {
		XmlRootElement root = clazz.getAnnotation(XmlRootElement.class);
		if (root != null) {
			String name = root.name();
			if (name.equals(elemName)) {
				return true;
			} else {
				return false;
			}
		}
		// 如果没有XmlRootElement注解，则继续。。
		if (clazz.getSimpleName().equalsIgnoreCase(elemName)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断接点名是否是普通元素; 对应的是field级别的
	 * @param clazz
	 * @param elemName
	 * @return
	 */
	public static boolean isElement(Class<?> clazz, String elemName) {
		try{
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields){
				XmlElement xmlElem = field.getAnnotation(XmlElement.class);
				if(xmlElem != null){ 
					if(elemName.equals(xmlElem.name())){
						return true;
					}
				}//如果没有注解， 则直接返回elem
			}
			if (clazz.getDeclaredField(elemName) != null) {
				return true;
			}
		}catch (NoSuchFieldException e) {
		} catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取某一个元素所对应的Field名字 
	 */
	public static String getFieldName(Class<?> clazz, String elem){
		if(elem == null){
			return null;
		}
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			XmlElement xmlElem = field.getAnnotation(XmlElement.class);
			if(xmlElem != null){ 
				if(elem.equals(xmlElem.name())){
					return field.getName();
				}
			}//如果没有注解， 则直接返回elem
		}
		return elem;
	}
	
	/**
	 * 判断相关的字段是否是List
	 */
	public static boolean isListElement(Class<?> clazz, String fieldName) {
		if (fieldName != null) {
			try {
				if (clazz.getDeclaredField(fieldName).getType().getSimpleName()
						.contains("List")) {
					return true;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 判断相关的字段是否是Map
	 */
	public static boolean isMapElement(Class<?> clazz, String fieldName) {
		if (fieldName != null) {
			try {
				if (clazz.getDeclaredField(fieldName).getType().getSimpleName()
						.contains("Map")) {
					return true;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 判断List<T>中T的类型 
	 */
	public static Class<?> getGenericType(Class<?> clazz, String fieldName){
		try {
			GenericType type = clazz.getDeclaredField(fieldName).getAnnotation(
					GenericType.class);
			if (type != null) {
				return type.type();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
