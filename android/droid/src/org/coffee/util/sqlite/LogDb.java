package com.fetion.util.sqlite;

/**
 * 数据库的log代理类
 * 
 * @author wangtaoyfx 2013-1-22下午2:04:03
 */
public class LogDb {

	public static void d(String tag, String msg) {
		com.fetion.util.log.Log.d(tag, msg);
	}

	public static void w(String tag, String msg, Throwable throwable) {
		com.fetion.util.log.Log.w(tag, msg, throwable);
	}
}
