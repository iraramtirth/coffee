package coffee.im.bluetooth.logic.base;

import java.util.HashSet;
import java.util.Set;

import android.os.Handler;
import android.os.Message;

/**
 * Logic的父类
 * 
 * 基本方法有:消息的发送
 * 
 * @author wangtaoyfx 2013-1-14下午2:17:20
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
	
	/******************** 以下代码为了兼容之前的架构 *********************/
	// protected void addListenerToAgent(int... args) {
	// if (args != null && args.length > 0) {
	// for (int i = 0; i < args.length; i++) // 根据不同的数据类型,选择不同的注册方式
	// {
	// switch (args[i]) {
	// case SysConstants.REGISTER_DATA_STATE: {
	// FetionLib.getFetionAgent().addFetionStateListener(this);
	// }
	// break;
	//
	// case Constants.DATA_CONTACT: // 各种数据变化监听
	// case Constants.DATA_BLOCK_CONTACT:
	// case Constants.DATA_CLUSTER:
	// case Constants.DATA_GROUP:
	// case Constants.DATA_CONVERSATION:
	// case Constants.DATA_PIM:
	// case Constants.DATA_PIMFROMSERVER:
	// case Constants.DATA_HTTP_DATA:
	// case Constants.DATA_AMS:
	// case Constants.DATA_USER_PROPERTIES:
	// case Constants.DATA_CONFIG:
	// case Constants.DATA_CLUSTER_CATEGORY:
	// case Constants.DATA_PORTRAIT: {
	// FetionLib.getFetionAgent().addDataEventListener(this,
	// args[i]);
	// }
	// break;
	// default:
	// break;
	// }
	// }
	// }
	// }
	//
	// @Override
	// public void handleDataEvent(BaseDataElement bde, int dataType, int
	// eventType) {
	// Log.i("handleDataEvent", bde + " " + dataType + " " + eventType);
	// }
	//
	// @Override
	// public void handleDataEvent(BaseDataElement[] bdes, int dataType,
	// int eventType) {
	// Log.i("handleDataEvent", bdes + " " + dataType + " " + eventType);
	// }
	//
	// @Override
	// public void onFetionEvent(int eventType, Object param) {
	// Log.i("handleDataEvent", eventType + " " + param);
	// }
}
