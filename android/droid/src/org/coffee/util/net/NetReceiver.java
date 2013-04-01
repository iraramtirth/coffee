package org.coffee.util.net;

import java.util.Locale;

import org.coffee.App;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

/**
 * 网络连接的状态
 * 
 * @author coffee<br>
 *         2013-1-22上午10:28:22
 */
public class NetReceiver extends BroadcastReceiver {

	private static final String TAG = "NetConnectionReceiver";
	/**
	 * 网络类型—3G
	 */
	private static boolean is3G = false;
	/**
	 * 网络类型—WIFI
	 */
	private static boolean isWifi = false;
	/**
	 * 网络状态 {@link NetState}
	 */
	private static int netState;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent != null) { //
			/**
			 * 每次网络状态改变都重置所有的变量 is3G isWifi netState currentIp
			 */
			checknet();
			// 开机
			if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

			}
			// 关机
			else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {

			}// 网络变更
			else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {
				Log.d(TAG, "NET_网络有变更");
			}
		}

	}

	public static int checknet() {
		netState = NetState.STATE_AVAILABLE_NO;
		// 将所有变量的值都重置, 该变量只有在需要改变的地方在设置(即需要设置true的地方)，使代码逻辑更清晰
		isWifi = false;
		is3G = false;
		// 获取网络服务对象
		ConnectivityManager conManager = ((ConnectivityManager) App
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
		if (conManager == null) {
			// 如果获取网络服务失败，则认为当前无网络
			return NetState.STATE_AVAILABLE_NO;
		}
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()
				&& networkInfo.isConnected()) {
			Log.d("NET_State", "网络可用");
			netState = NetState.STATE_AVAILABLE_YES;
			if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				// 该方法里会设置变量is3G的值
				check2G3G(networkInfo);
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				isWifi = true;
			}
		} else {
			Log.d("NET_Satte", "网络不可用");
		}
		return netState;
	}

	private static void check2G3G(NetworkInfo networkInfo) {
		Cursor cursor = null;
		try {
			cursor = App
					.getContext()
					.getContentResolver()
					.query(Uri.parse("content://telephony/carriers/preferapn"),
							null, null, null, null);
			// 电信的某些3G cursor.getCount()==0
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				String apn = cursor.getString(cursor.getColumnIndex("apn"));
				if (apn != null && apn.length() > 0) {
					apn = cursor.getString(cursor.getColumnIndex("user"));
				}
				if (apn != null && apn.length() > 0) {
					apn = apn.toLowerCase(Locale.CHINA);
					if (apn.contains("wap")) {
						Log.d(TAG, "当前网络为wap网络");
					} else {
						Log.d(TAG, "当前网络为net网络");
					}
				}
			}// if end

			String netInfo = networkInfo.toString().toUpperCase(Locale.CHINA);
			if (netInfo.contains("HSPA") || netInfo.contains("UMTS")
					|| netInfo.contains("EVDO") || netInfo.contains("HSDPA")) {
				is3G = true;
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * 判断是否是3G/WIFI网络
	 * 
	 * @return
	 */
	public static boolean is3GOrWifi() {
		return is3G || isWifi;
	}

	public static boolean isWifi() {
		return isWifi;
	}

	/**
	 * 网络是否可用
	 * 
	 * @return true false
	 */
	public static boolean isNet() {
		int netStatus = checknet();
		if (NetState.STATE_AVAILABLE_YES == netStatus) {
			return true;
		}
		return false;
	}

}
