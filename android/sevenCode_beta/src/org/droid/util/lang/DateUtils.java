package org.droid.util.lang;


public class DateUtils extends org.droid.util.sqlite.DateUtils{
	/**
	 * 返回当天是星期几
	 * @param dayOfWeek ： 参数为 cal.get(Calendar.DAY_OF_WEEK)
	 * @return
	 */
	public static String getWeek(int dayOfWeek){
		switch(dayOfWeek){
			case 1: return "日";
			case 2: return "一";
			case 3: return "二";
			case 4: return "三";
			case 5: return "四";
			case 6: return "五";
			case 7: return "六";
		}
		return "";
	}
}
