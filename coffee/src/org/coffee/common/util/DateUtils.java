package org.coffee.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
/**
 * 日期工具类
 * 主要负责处理时间java.util.Date与String类型的转换
 * @author coffee
 */
public class DateUtils {
	private static Logger log = Logger.getLogger(DateUtils.class.toString());
	/**
	 * 默认的格式化格式为
	 * yyyy-MM-dd HH:mm:ss
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 *  格式化日期类型 ，返回字符串
	 *  @param : 传入参数 java.util.Date
	 *  @return 返回一个字符串
	 */
	public static String format(Object date){
		try {
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 解析字符串,返回日期
	 * @param value : 传入一个字符串
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
					//exc.printStackTrace();
					log.warning("不能格式化指定的值: "+value);
				}
			} 
			return null;
		}
	}
	
	//获取当前日期-时间
	public String  getCurDateTime(String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		
		Date date =  new Date(1346643092000L);
		
		System.out.println(format(date));
	}
}
