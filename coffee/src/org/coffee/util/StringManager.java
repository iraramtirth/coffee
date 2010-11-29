package org.coffee.util;

public class StringManager {
	/**
	 * ����ĸСд
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
	public static String toUpperCaseFirstChar(String str){
		if(str == null){
			return null;
		}
		str = str.replaceFirst(".", str.substring(0, 1).toUpperCase());
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(StringManager.toLowerCaseFirstChar("Fser"));
		System.out.println(StringManager.toUpperCaseFirstChar("fser"));
	}
}
