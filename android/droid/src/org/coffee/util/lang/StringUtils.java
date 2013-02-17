package org.coffee.util.lang;

/**
 * 数值型变量工具类
 *  
 * @author coffee
 */
public class StringUtils {
	
	
	/**
	 * 如果[[非空]]则返回true 
	 */
	public static boolean isNotEmpty(String ... strs){
		for(String s : strs){
			if(s == null || s.trim().length() == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 如果[[非空]]则返回true 
	 */
	public static boolean isEmpty(String ... strs){
		for(String s : strs){
			if(s == null || s.trim().length() == 0){
				return true;
			}
		}
		return false;
	}
	
	
	
	public static float toFloat(String num, float... defValue){
		try{
			return Float.valueOf(num);
		}catch(Exception e){
			if(defValue.length > 0){
				return defValue[0];
			}else{
				return 0f;
			}
		}
	}
	
	public static int toInt(String num, int... defValue){
		try{
			return Integer.valueOf(num);
		}catch(Exception e){
			if(defValue.length > 0){
				return defValue[0];
			}else{
				return 0;
			}
		}
	}
	
}
