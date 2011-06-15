package org.coffee.common.util;

public class StringUtils {
	/**
	 * 首字母小写
	 * @param str
	 * @return
	 */
	public static String toLowerCaseFirstChar(String str){
		if(str == null){
			return null;
		}
		str = str.replaceFirst(".", str.substring(0, 1).toLowerCase());
		return str;
	}
	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String toUpperCaseFirstChar(String str){
		if(str == null){
			return null;
		}
		str = str.replaceFirst(".", str.substring(0, 1).toUpperCase());
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.toLowerCaseFirstChar("Fser"));
		System.out.println(StringUtils.toUpperCaseFirstChar("fser"));
	}
}
