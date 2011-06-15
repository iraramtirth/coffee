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
	
	/**
	 * 字符串替换
	 * 主要适用于http的queryString,替换字符串连接+
	 * @param content : queryString [name=?&pwd=?&id=?]
	 * @param rep ：
	 * @return
	 */
	public static String replace(String content, String[] rep){
		@SuppressWarnings("unused")
		int index = -1;
		int i = 0;
		while((index = content.indexOf('?')) != -1){
			content = content.replaceFirst("\\?", rep[i]);
			i++;
		}
		return content;
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(StringUtils.toLowerCaseFirstChar("Fser"));
		System.out.println(StringUtils.toUpperCaseFirstChar("fser"));
		
		String res = replace("name=?&pwd=?&id=?", new String[]{"12","23","34"});
		System.out.println(res);
	}
}
