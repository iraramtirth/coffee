/*
 * @(#)Log.java 11-10-9 下午3:00 CopyRight 2011. All rights reserved
 */
package org.coffee.os;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

/**
 * 日志打印
 * 
 */
public abstract class Log {
	/**
	 * 日期格式
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// 打印日志预设值
	private static boolean isPrintLog = true;

	// 打印日志到SDCard文件的预设值
	private static boolean isPrintLogSD = isPrintLog;


	/**
	 * 打印verbose级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void verbose(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("v", tag, text);
		}

		if (isPrintLog) {
			android.util.Log.v(tag, text);
		}
	}

	private static long currentTime = System.currentTimeMillis();

	public static long getCurrentTime() {
		currentTime = System.currentTimeMillis();
		return currentTime;
	}

	public static long getConsumeTime() {
		long consumeTime = System.currentTimeMillis() - currentTime;
		currentTime = System.currentTimeMillis();
		return consumeTime;
	}

	/**
	 * 
	 * 打印debug级别的日志<BR>
	 * [功能详细描述]
	 * 
	 * @param obj
	 *            tag标记，传入当前调用的类对象即可，方法会转化为该对象对应的类名
	 * @param text
	 *            日志内容
	 */
	public static void debug(Object obj, String text) {
		if (isPrintLogSD) {
			storeLog("d", obj.getClass().getSimpleName(), text);
		}

		if (obj != null && isPrintLog) {
			debug(obj.getClass().getSimpleName(), text);
		}
	}

	/**
	 * 打印debug级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void debug(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("d", tag, text);
		}

		if (isPrintLog) {
			android.util.Log.d(tag, text);
		}
	}

	/**
	 * 打印info级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void info(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("i", tag, text);
		}

		if (isPrintLog) {
			text = text.replaceAll("[\\n\\r]", "");
			android.util.Log.i(tag, text);
		}
	}

	/**
	 * 打印warn级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void warn(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("w", tag, text);
		}

		if (isPrintLog) {
			android.util.Log.w(tag, text);
		}
	}

	/**
	 * 打印warn级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 * @param throwable
	 *            异常信息
	 */
	public static void warn(String tag, String text, Throwable throwable) {
		if (isPrintLogSD) {
			storeLog("w", tag, text);
		}

		if (isPrintLog) {
			text = text.replaceAll("[\\n\\r]", "");
			android.util.Log.w(tag, text, throwable);
		}
	}

	/**
	 * 打印error级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void error(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("e", tag, text);
		}

		if (isPrintLog) {
			text = text.replaceAll("[\\n\\r]", "");
			android.util.Log.e(tag, text);
		}
	}

	/**
	 * 打印error级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 * @param throwable
	 *            异常信息
	 */
	public static void error(String tag, String text, Throwable throwable) {
		if (isPrintLogSD) {
			storeLog("e", tag, text);
		}

		if (isPrintLog) {
			android.util.Log.e(tag, text, throwable);
		}
	}

	/**
	 * 打印file日志，不打到log，直接输出到sd卡文件
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void file(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("f", tag, text);
		}
	}

	/**
	 * 
	 * [用于存取错误日志信息] [功能详细描述]
	 * 
	 * @param type
	 *            type
	 * @param strModule
	 *            module
	 * @param strErrMsg
	 *            message
	 */
	public static void storeLog(String type, String tag, String strErrMsg) {
		File file = openFile("uim.log");

		if (file == null) {
			return;
		}

		try {
			// 输出
			FileOutputStream fos = new FileOutputStream(file, true);
			PrintWriter out = new PrintWriter(fos);
			Date dateNow = new Date();
			String dateNowStr = dateFormat.format(dateNow);
			if (type.equals("e")) {
				out.println(dateNowStr + " Error:>>" + tag + "<<  " + strErrMsg
						+ '\r');
			} else if (type.equals("d")) {
				out.println(dateNowStr + " Debug:>>" + tag + "<<  " + strErrMsg
						+ '\r');
			} else if (type.equals("i")) {
				out.println(dateNowStr + " Info:>>" + tag + "<<   " + strErrMsg
						+ '\r');
			} else if (type.equals("w")) {
				out.println(dateNowStr + " Warning:>>" + tag + "<<   "
						+ strErrMsg + '\r');
			} else if (type.equals("v")) {
				out.println(dateNowStr + " Verbose:>>" + tag + "<<   "
						+ strErrMsg + '\r');
			} else if (type.equals("f")) {
				out.println(dateNowStr + " File:>>" + tag + "<<   " + strErrMsg
						+ '\r');
			}

			out.flush();
			out.close();
			out = null;

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param name
	 *            文件名
	 * @return 返回文件
	 */
	private static File openFile(String name) {
		String logFileDir = Environment.getExternalStorageDirectory()
				+ "/coffee/log.txt";

		File fileDir = new File(logFileDir);

		// 判断目录是否已经存在
		if (!fileDir.exists()) {
			android.util.Log.i("Log", "fileDir is no exists!");
			if (!fileDir.mkdirs()) {
				return null;
			}
		}

		return new File(logFileDir, name);
	}

	/**
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param string
	 * @param string2
	 */
	public static void start(String tag, String text) {
		currentTime = System.currentTimeMillis();
		info(tag, text + "[start]");
	}

	/**
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param string
	 * @param string2
	 */
	public static void end(String tag, String text) {
		info(tag, text + "[end]-[耗时]-"
				+ (System.currentTimeMillis() - currentTime) + "ms");
	}
}
