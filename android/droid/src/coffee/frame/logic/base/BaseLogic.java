package coffee.frame.logic.base;

import java.util.HashSet;
import java.util.Set;

import android.os.Handler;
import android.os.Message;

/**
 * Logic的父类
 * 
 * 基本方法有:消息的发送
 * 
 * @author coffee <br>
 *         2013-1-14下午2:17:20
 */
public class BaseLogic {

	private Set<Handler> handlers = new HashSet<Handler>();

	public void addHandler(Handler handler) {
		if (handler == null) {
			return;
		}
		handlers.add(handler);
	}

	public void removeHandler(Handler handler) {
		if (handler == null) {
			return;
		}
		handlers.remove(handler);
	}

	public void clearHandler() {
		handlers.clear();
	}

	public void sendMessageDelayed(int what, Object obj, int delayMillis) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		for (Handler handler : handlers) {
			boolean result = handler.sendMessageDelayed(msg, delayMillis);
			System.out.println(result);
		}
	}

	public void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		for (Handler handler : handlers) {
			handler.sendMessage(msg);
		}
	}
}
