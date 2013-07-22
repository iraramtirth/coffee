package org.coffee.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.coffee.App;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
/**
 * 
 * @author coffee <br>
 *          2013-7-22上午9:39:50
 */
public class WifiInfoManager {
	private static final String TAG = "WifiInfoManager";
	private WifiManager wifiManager;

	public WifiInfoManager() {
		wifiManager = (WifiManager) App.getContext().getSystemService(
				Context.WIFI_SERVICE);
	}

	public List<WifiInfo> dump() {
		if (!this.wifiManager.isWifiEnabled()) {
			return new ArrayList<WifiInfo>();
		}
		android.net.wifi.WifiInfo wifiConnection = this.wifiManager
				.getConnectionInfo();
		WifiInfo currentWIFI = null;
		if (wifiConnection != null) {
			String s = wifiConnection.getBSSID();
			int i = wifiConnection.getRssi();
			String s1 = wifiConnection.getSSID();
			currentWIFI = new WifiInfo(s, i, s1);

		}
		ArrayList<WifiInfo> lsAllWIFI = new ArrayList<WifiInfo>();
		if (currentWIFI != null) {
			lsAllWIFI.add(currentWIFI);
		}
		List<ScanResult> lsScanResult = this.wifiManager.getScanResults();
		for (ScanResult result : lsScanResult) {
			WifiInfo scanWIFI = new WifiInfo(result);
			if (!scanWIFI.equals(currentWIFI))
				lsAllWIFI.add(scanWIFI);
		}
		return lsAllWIFI;
	}

	public boolean isWifiEnabled() {
		return this.wifiManager.isWifiEnabled();
	}

	public JSONArray wifiInfo() {
		JSONArray jsonArray = new JSONArray();

		for (WifiInfo wifi : dump()) {
			JSONObject localJSONObject = wifi.info();
			jsonArray.put(localJSONObject);
		}
		return jsonArray;
	}

	public WifiManager wifiManager() {
		return this.wifiManager;
	}

	/**
	 * 将WIFI信息组合成JSON包，去服务器查询位置时做为数据上传至Google的位置服务器
	 * 
	 * @return JSONArray
	 */
	public JSONArray wifiTowers() {
		JSONArray jsonArray = new JSONArray();
		if (isWifiEnabled()) {
			try {
				Iterator<WifiInfo> localObject = dump().iterator();
				while (true) {
					if (!(localObject).hasNext()) {
						return jsonArray;
					}

					jsonArray.put(localObject.next().wifi_tower());
				}
			} catch (Exception localException) {
				Log.e(TAG, "wifiTowers: " + localException.getMessage());
			}
		}

		return jsonArray;
	}

	public class WifiInfo implements Comparable<WifiInfo> {
		public int compareTo(WifiInfo wifiinfo) {
			int i = wifiinfo.dBm;
			int j = dBm;
			return i - j;
		}

		public boolean equals(Object obj) {
			boolean flag = false;
			if (obj == this) {
				flag = true;
				return flag;
			} else {
				if (obj instanceof WifiInfo) {
					WifiInfo wifiinfo = (WifiInfo) obj;
					// int i = wifiinfo.dBm;
					// int j = dBm;
					// if (i == j)
					// {
					String s = wifiinfo.bssid;
					String s1 = bssid;
					if (s.equals(s1)) {
						flag = true;
						return flag;
					}
					// }
					// flag = false;
				} else {
					flag = false;
				}
			}
			return flag;
		}

		public int hashCode() {
			int i = dBm;
			int j = bssid.hashCode();
			return i ^ j;
		}

		public JSONObject info() {
			JSONObject jsonobject = new JSONObject();
			try {
				String s = bssid;
				jsonobject.put("mac", s);
				String s1 = ssid;
				jsonobject.put("ssid", s1);
				int i = dBm;
				jsonobject.put("dbm", i);
			} catch (Exception ex) {
				Log.e(TAG, "info failed: " + ex.getMessage());
			}
			return jsonobject;
		}

		public JSONObject wifi_tower() {
			JSONObject jsonobject = new JSONObject();
			try {

				String s = bssid;
				jsonobject.put("mac_address", s);
				int i = dBm;
				jsonobject.put("signal_strength", i);
				String s1 = ssid;
				jsonobject.put("ssid", s1);
				jsonobject.put("age", 0);
			} catch (Exception ex) {

			}
			return jsonobject;
		}

		public final String bssid;
		public final int dBm;
		public final String ssid;

		public WifiInfo(ScanResult scanresult) {
			String s = scanresult.BSSID;
			bssid = s;
			int i = scanresult.level;
			dBm = i;
			String s1 = scanresult.SSID;
			ssid = s1;
		}

		public WifiInfo(String s, int i, String s1) {
			bssid = s;
			dBm = i;
			ssid = s1;
		}

	}
}
