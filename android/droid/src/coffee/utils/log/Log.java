/*
 * @(#)Log.java 11-10-9 下午3:00 CopyRight 2011. All rights reserved
 */
package coffee.utils.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;

/**
 * 日志打印
 * 
 * @author coffee
 * 		2013-1-11下午3:15:58
 */
public abstract class Log {
	// 打印日志预设值
	private static boolean isPrintLog = false;

	// 打印日志到SDCard文件的预设值
	private static boolean isPrintLogSD = isPrintLog;

	//相对于sdcard的目录
	private static final String LOG_FILE = "Fetion/apad/log.txt";
	
	/**
	 * 日期格式
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	
	public static void d(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("d", tag, text);
		}
		if (isPrintLog) {
			android.util.Log.d(tag, text);
		}
	}
	
	/**
	 * @param obj 	: 可以傳入 Class/String等类型的tag 
	 * @param text
	 */
	public static void i(Object tag, String text) {
		if (isPrintLogSD) {
			storeLog("i", tag, text);
		}
		if (isPrintLog) {
			android.util.Log.i(String.valueOf(tag), text);
		}
	}

	public static void w(Object tag, String text) {
		if (isPrintLogSD) {
			storeLog("w", tag, text);
		}
		if (isPrintLog) {
			android.util.Log.w(String.valueOf(tag), text);
		}
	}

	public static void w(Object tag, String text, Throwable throwable) {
		if (isPrintLogSD) {
			storeLog("w", tag, text);
		}
		if (isPrintLog) {
			android.util.Log.w(String.valueOf(tag), text, throwable);
		}
	}

	public static void e(Object tag, String text) {
		if (isPrintLogSD) {
			storeLog("e", tag, text);
		}

		if (isPrintLog) {
			android.util.Log.e(String.valueOf(tag), text);
		}
	}

	public static void e(Object tag, String text, Throwable throwable) {
		if (isPrintLogSD) {
			storeLog("e", tag, text);
		}
		if (isPrintLog) {
			android.util.Log.e(String.valueOf(tag), text, throwable);
		}
	}

	public static void file(String tag, String text) {
		if (isPrintLogSD) {
			storeLog("f", tag, text);
		}
	}

	private static void storeLog(String type, Object tag, String strErrMsg) {
		File file = openFile(LOG_FILE);
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
				+ name;
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
	 
}
