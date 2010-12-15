package org.coffee.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 *  格式化日期类型 
	 *  @param : 传入参数
	 *  @return 返回一个字符串
	 */
	public static String format(Object value){
		try {
			return sdf.format(value);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 解析字符串
	 * @param 
	 * @return 返回日期
	 */
	public static Date parse(Object value){
		try {
			return sdf.parse(value.toString());
		} catch (Exception e) {
			try{
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(value.toString());
			} catch(Exception ex){
				try{
					sdf = new SimpleDateFormat("HH:mm:ss");
					return sdf.parse(value.toString());
				} catch(Exception exc){
					exc.printStackTrace();
				}
			} 
			return null;
		}
	}
}
