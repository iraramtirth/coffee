/*
 * 修改内容：[修改内容]
 */
package org.coffee.map;

import org.coffee.map.LocationManager.LocationResult;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

/**
 * 
 * @author coffee <br>
 *         2013-7-22上午9:40:00
 */
public class LocationManager extends AsyncTask<Void, Void, LocationResult> {
	/**
	 * 获取定位信息监听器
	 */
	private LocationProgressListenr mLocationProgressListenr;

	/**
	 * 获取到位置信息的回调
	 */
	private LocationDataListenr mLocationDataListenr;

	/**
	 * 是否需要根据经纬度获取到具体的位置信息
	 */
	private boolean mNeedAddressInfo;

	/**
	 * 
	 * 获取当前位置信息<BR>
	 * [功能详细描述]
	 * 
	 * @param progressListener
	 *            获取定位信息监听器
	 * @param locationDataListenr
	 *            获取到位置信息的回调
	 * @param needAddressInfo
	 *            是否需要根据经纬度获取到具体的位置信息
	 * @param context
	 *            context
	 */
	public void getCurrentLocation(LocationProgressListenr progressListener, LocationDataListenr locationDataListenr,
			boolean needAddressInfo, Context context) {
		mLocationProgressListenr = progressListener;
		mLocationDataListenr = locationDataListenr;
		mNeedAddressInfo = needAddressInfo;
		execute();
	}

	@Override
	protected LocationResult doInBackground(Void... params) {
		LocationResult result = null;
		try {
			// 打开进度更新
			publishProgress();
			result = LocationImpl.getInstance().getLocation();

			Location location = result.getLocation();
			if (location != null && mNeedAddressInfo
					&& (result.getAddressInfo() == null || result.getAddressInfo().trim().length() == 0)) {
				result.setAddressInfo(LocationImpl.getInstance().getAddress(location.getLatitude(),
						location.getLongitude()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new LocationResult();
		}
		return result;

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		if (mLocationProgressListenr != null) {
			mLocationProgressListenr.onLocationProgress(true);
		}
	}

	@Override
	protected void onPostExecute(LocationResult result) {
		super.onPostExecute(result);

		if (mLocationProgressListenr != null) {
			mLocationProgressListenr.onLocationProgress(false);
		}
		mLocationDataListenr.onLocationResult(result);
	}

	public static class LocationResult {
		/**
		 * 位置经纬度
		 */
		private Location location;

		/**
		 * 具体的位置信息
		 */
		private String addressInfo;

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public String getAddressInfo() {
			return addressInfo;
		}

		public void setAddressInfo(String addressInfo) {
			this.addressInfo = addressInfo;
		}
	}

	/**
	 * 
	 * 获取到位置信息后的回调<BR>
	 * [功能详细描述]
	 * 
	 * @author tjzhang
	 */
	public static interface LocationDataListenr {
		void onLocationResult(LocationResult result);
	}

	/**
	 * 
	 * 获取定位信息的进度更新<BR>
	 * [功能详细描述]
	 * 
	 * @author tjzhang
	 */
	public static interface LocationProgressListenr {
		void onLocationProgress(boolean show);
	}

}
