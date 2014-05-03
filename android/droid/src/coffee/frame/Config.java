package coffee.frame;

import org.coffee.App;

import android.os.Environment;
import coffee.utils.log.Log;

public class Config {

	public final static String server_www = " ";
	/**
	 * 接口地址 -- 如果没有配置文件默认会从www上读数据 <br>
	 * {@linkplain }
	 */
	public static String SERVER_ADDRESS = server_www;
	public static boolean isPrintLog = true;
	// 为true表示 只用存储卡缓存数据
	private static boolean cacheOnlyInSD = false;

	public static String getServerUrl() {
		return SERVER_ADDRESS;
	}

	public final static String APP_BASE = Environment.getExternalStorageDirectory() + "/coffee";

	/**
	 * 从安全上考虑,只保存在如下目录<br>
	 * /data/data/com.pinzhi.activity/app_json <br>
	 */
	public final static String getJsonDir() {
		String dir = App.getContext().getDir("json", 0).getAbsolutePath();
		Log.d("config:json", dir);
		return dir;
	}

	/**
	 * sdcard缓存路径(图片)
	 */
	public final static String getCacheDir() {
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = APP_BASE + "/cache";
		} else {
			// /data/data/package/cache
			dir = App.getContext().getCacheDir().toString();
		}
		Log.d("config:cache", dir);
		return dir;
	}
	
	public final static String getBookDir(){
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = APP_BASE + "/book";
		} else {
			// /data/data/package/cache
			dir = App.getContext().getDir("book", 0).getAbsolutePath();
		}
		Log.d("config:cache", dir);
		return dir;
	}

	/**
	 * 拍照保存的目录
	 */
	public final static String getCaptureDir() {
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = APP_BASE + "/capture";
		} else {
			// /data/data/package/cache
			dir = App.getContext().getCacheDir().toString();
		}
		Log.d("config:capture", dir);
		return dir;
	}

	/**
	 * 应用崩溃日志
	 */
	public final static String getCrashDir() {
		String dir = "";
		if (cacheOnlyInSD || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = APP_BASE + "/crash";
		} else {
			// /data/data/package/app_crash
			dir = App.getContext().getDir("crash", 0).getAbsolutePath();
		}
		Log.d("config:crash", dir);
		return dir;
	}
}
