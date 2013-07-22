package org.coffee.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.coffee.App;
import org.coffee.map.LocationManager.LocationResult;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * 
 * @author coffee <br>
 *          2013-7-22上午9:39:21
 */
public final class LocationImpl {
	private static LocationImpl mLocationImpl;

	/**
	 * 定位管理类
	 */
	private LocationManager mLocationMgr;

	/**
	 * 获取位置监听最短间隔时间
	 */
	private final long MIN_TIME = 500;

	/**
	 * 获取位置监听最短间隔距离
	 */
	private static final float MIN_INSTANCE = 20;

	private static final String TAG = "LocationImpl";

	private LocationImpl() {
		mLocationMgr = (LocationManager) App.getContext().getSystemService(
				Service.LOCATION_SERVICE);
	}

	/**
	 * 获取位置服务实例
	 * 
	 * @param context
	 *            上下文
	 * @return 位置服务实例
	 */
	public static LocationImpl getInstance() {
		synchronized (LocationImpl.class) {
			if (mLocationImpl == null) {
				mLocationImpl = new LocationImpl();
			}
		}
		return mLocationImpl;
	}

	/**
	 * 
	 * 获取用户位置 根据GPS or WiFi Or 基站定位用户位置
	 * 
	 * @return Location 经纬度Location
	 */
	public LocationResult getLocation() {
		Log.d(TAG, "------getLocation---start---");

		LocationResult locationInfo = new LocationResult();

		if (mLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Log.d(TAG, "startListenLocation NETWORK_PROVIDER");
			mLocationMgr.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_INSTANCE,
					mLocationListener, Looper.getMainLooper());
		}

		if (mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.d(TAG, "startListenLocation GPS_PROVIDER");
			mLocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					MIN_TIME, MIN_INSTANCE, mLocationListener,
					Looper.getMainLooper());
		}

		if (locationInfo.getLocation() == null) {
			locationInfo = getLocationInfo();
		}

		// 移除监听
		mLocationMgr.removeUpdates(mLocationListener);

		if (locationInfo.getLocation() != null) {
			Log.d(TAG, "end    =  " + locationInfo.getLocation().getLatitude()
					+ "*****" + locationInfo.getLocation().getLongitude());
		}
		return locationInfo;
	}

	/**
	 * 位置变化监听
	 */
	private LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			Log.d(TAG, "====location changed====");

			Log.d(TAG, "changed    =  " + location.getLatitude() + "*****"
					+ location.getLongitude());
		}

		@Override
		public void onStatusChanged(String s, int i, Bundle bundle) {
		}

		@Override
		public void onProviderEnabled(String s) {
		}

		@Override
		public void onProviderDisabled(String s) {
		}
	};

	/**
	 * 
	 * 根据基站或者Wifi获取位置详细信息
	 * 
	 * @param jsonArray
	 * @param isByCell
	 * @return
	 */
	private LocationResult getLocationInfo() {
		LocationResult info = new LocationResult();

		CellInfoManager cellManager = new CellInfoManager();
		WifiInfoManager mWiFiManager = new WifiInfoManager();
		JSONArray cellArray = cellManager.cellTowers();
		JSONArray wifiArray = mWiFiManager.wifiTowers();
		Log.d(TAG, "getLocationInfo----cellArray=" + cellArray + ", wifiArray="
				+ wifiArray);
		if (cellArray.length() < 1 && wifiArray.length() < 1) {
			// 如果本地的定位信息缺失，不去服务器查询
			return info;
		}
		JSONObject holder = new JSONObject();
		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("address_language", "zh_CN");
			if (cellArray.length() > 0) {
				if (cellManager.isGsm()) {
					holder.put("radio_type", "gsm");

				} else if (cellManager.isCdma()) {

					holder.put("radio_type", "cdma");
				}
			}
			holder.put("request_address", true);
			holder.put("cell_towers", cellArray);
			holder.put("wifi_towers", wifiArray);

			JSONObject retLocationJson = new JSONObject(
					queryLocDataFromGoogle(holder.toString()));

			JSONObject locationData = retLocationJson.getJSONObject("location");
			if (locationData != null) {
				Location loc = new Location(LocationManager.NETWORK_PROVIDER);
				loc.setLatitude(locationData.getDouble("latitude"));
				loc.setLongitude(locationData.getDouble("longitude"));
				loc.setAccuracy((float) locationData.getDouble("accuracy"));
				loc.setTime(System.currentTimeMillis());
				info.setLocation(loc);

				JSONObject addressData = locationData.getJSONObject("address");
				if (addressData != null) {
					StringBuffer strBuffer = new StringBuffer();
					if (addressData.getString("country") != null) {
						strBuffer.append(addressData.getString("country"));
					}
					if (addressData.getString("region") != null) {
						strBuffer.append(addressData.getString("region"));
					}
					if (addressData.getString("city") != null) {
						strBuffer.append(addressData.getString("city"));
					}
					if (addressData.getString("street") != null) {
						strBuffer.append(addressData.getString("street"));
					}
					if (addressData.getString("street_number") != null) {
						strBuffer
								.append(addressData.getString("street_number"));
					}
					info.setAddressInfo(strBuffer.toString());
				}
			}

			return info;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "getLocationInfo Exception =" + e.toString());
		}
		return info;
	}

	/**
	 * 
	 * 根据经纬度获取到位置信息<BR>
	 * [功能详细描述]
	 * 
	 * @param location
	 *            location
	 * @return String 位置信息
	 */
	public String getAddress(double lat, double lon) {
		// 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址
		// 密钥可以随便写一个key=abc
		// output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析
		String url = String.format(
				"http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",
				lat, lon);
		Log.d(TAG, "getAddress: Address send*****" + url);
		String result = "";
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse hr = client.execute(new HttpGet(url));
			HttpEntity entity = hr.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			if ((result = br.readLine()) != null) {
				Log.d(TAG, "getAddress: Address receive*****" + result);
				String[] retList = result.split(",");
				if (retList.length > 2 && ("200".equals(retList[0]))) {
					result = retList[2];
					result = result.replace("\"", "");
				} else {
					result = "";
				}
			}
			br.close();
			Log.d(TAG, "getAddress: Address =" + result);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "getAddress: Exception =" + e.toString());
			return null;
		}
		return result;
	}

	/**
	 * 
	 * [一句话功能简述]判断/system/framework/中是否有com.google.android.maps.jar [功能详细描述]
	 * 
	 * @return
	 */
	public boolean isJarExisits() {
		return new File("/system/framework/com.google.android.maps.jar")
				.exists();
	}

	/**
	 * 从google服务器查询地理位置
	 * 
	 * @param strJsonArgs
	 * @return
	 */
	private String queryLocDataFromGoogle(String strJsonArgs) {
		Log.d(TAG, "getLocationInfo: Location Send*****" + strJsonArgs);

		StringBuffer sb = new StringBuffer();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost localHttpPost = new HttpPost(
					"http://www.google.com/loc/json");
			StringEntity objJsonEntity = new StringEntity(strJsonArgs);

			localHttpPost.setEntity(objJsonEntity);
			HttpResponse objResponse = client.execute(localHttpPost);
			HttpEntity httpEntity = objResponse.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpEntity.getContent()));

			String result = null;
			while ((result = br.readLine()) != null) {
				Log.d(TAG, "getLocationInfo: Locaiton receive*****" + result);
				sb.append(result);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
