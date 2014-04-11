package coffee.im.bluetooth.activity.base;

import java.util.Hashtable;

import android.os.Handler;
import android.os.Message;

/**
 * 管理项目中用到的Handler
 * 
 * @author coffee
 * 
 *         2014年1月8日上午11:45:04
 */
public class HandlerMgr {
	/**
	 * key: 一般以Activity名字命名
	 */
	private static Hashtable<String, Handler> map = new Hashtable<String, Handler>();

	public static void put(String activityName, Handler handler) {
		map.put(activityName, handler);
	}

	/**
	 * 
	 * @param activityName
	 * @return
	 */
	public static Handler get(String activityName) {
		return map.get(activityName);
	}

	public static void remove(String activityName) {
		map.remove(activityName);
	}

	public static void sendMessageDelayed(int what, Object obj, int delayMillis) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		for (Handler handler : map.values()) {
			boolean result = handler.sendMessageDelayed(msg, delayMillis);
			System.out.println(result);
		}
	}

	public static void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		for (Handler handler : map.values()) {
			handler.sendMessage(msg);
		}
	}

}
