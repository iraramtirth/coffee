package coffee.frame.activity.base;

import java.util.HashMap;

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
	private static HashMap<String, Handler> map = new HashMap<String, Handler>();

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
		for (Handler handler : map.values()) {
			Message msg = Message.obtain();
			msg.what = what;
			msg.obj = obj;
			boolean result = handler.sendMessageDelayed(msg, delayMillis);
			System.out.println(result);
		}
	}

	public static void sendMessage(int what, Object obj) {
		for (Handler handler : map.values()) {
			Message msg = Message.obtain();
			msg.what = what;
			msg.obj = obj;
			handler.sendMessage(msg);
		}
	}

}
