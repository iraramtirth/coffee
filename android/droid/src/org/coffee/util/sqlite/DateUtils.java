package org.coffee.util.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
	 *  @param : 传入参数
	 *  @return 返回一个字符串
	 */
	public static String format(Object value){
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(value);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 解析字符串,返回日期
	 * @param 
	 * @return 返回日期
	 */
	public static Date parse(String value){
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.parse(value);
		} catch (Exception e) {
			try{
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
				return sdf.parse(value.toString());
			} catch(Exception ex){
				try{
					sdf = new SimpleDateFormat("HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
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
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			return null;
		}
	}
}
