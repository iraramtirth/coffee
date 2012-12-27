package com.chinaunicom.woyou.utils.xml.parser;

import java.lang.reflect.Field;

import com.chinaunicom.woyou.utils.lang.TUtils;
import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlElement.DEFAULT;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;

/**
 * 该方法是基于注解的
 */
public class TXmlUtils extends TUtils {

	final static String TAG = "TUtils";

	/**
	 * 判断节点是否是Root元素; 对应的是class级别的
	 * 
	 * @param clazz
	 * @param elemName
	 *            : xml元素的节点名
	 */
	public static boolean isRootElement(Class<?> clazz, String elemName) {
		XmlRootElement root = clazz.getAnnotation(XmlRootElement.class);
		if (root != null && root.name().equals(elemName)) {
			return true;
		}
		// 如果没有XmlRootElement注解，则继续。。
		if (clazz.getSimpleName().equalsIgnoreCase(elemName)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断节点是否是Root元素; 对应的是class.field（Class）级别的
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static boolean isBeanElement(Class<?> clazz, String fieldName) {
		try {
			Field field = getFieldByRecursion(clazz, fieldName);

			XmlElement xmlElem = field.getAnnotation(XmlElement.class);
			if (xmlElem != null && field.getType() != java.util.List.class
					&& field.getType() != java.util.Map.class) {
				return xmlElem.type() != DEFAULT.class;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断接点名是否是普通元素; 对应的是field级别的
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static boolean isElement(Class<?> clazz, String fieldName) {
		try {
			Field field = getFieldByRecursion(clazz, fieldName);

			XmlElement xmlElem = field.getAnnotation(XmlElement.class);
			if (xmlElem != null || field != null) {
				return true;
			}

		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断相关的字段是否是List
	 */
	public static boolean isListElement(Class<?> clazz, String fieldName) {
		if (fieldName != null) {
			try {
				Field field = getFieldByRecursion(clazz, fieldName);
				if (field.getType() == java.util.List.class) {
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
				Field field = getFieldByRecursion(clazz, fieldName);
				if (field.getType().getSimpleName().contains("Map")) {
					return true;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获取某一个元素所对应的Field
	 * 
	 * @param 类名
	 * @param elem
	 *            : xml元素的节点名称
	 */
	public static Field getField(Class<?> clazz, String elem) {
		if (elem == null) {
			return null;
		}
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			XmlElement xmlElem = field.getAnnotation(XmlElement.class);
			if (xmlElem != null) {
				if (elem.equals(xmlElem.name())) {
					return field;
				}
			}
		}
		// 如果没有注解， 则直接返回elem对应的field
		try {
			try {
				Field field = clazz.getDeclaredField(elem);
				return field;
			} catch (NoSuchFieldException e) {
				// 如果当前类 不存在则 递归父类
				Class<?> superClazz = clazz.getSuperclass();
				if (superClazz != null) {
					return getField(superClazz, elem);
				} else {
					return null;
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
}
