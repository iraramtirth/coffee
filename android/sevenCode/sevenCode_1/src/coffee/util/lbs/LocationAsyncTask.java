package coffee.util.lbs;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

public class LocationAsyncTask extends AsyncTask<Activity, Void, Location> {
	final String TAG;

	private Activity context;

	private LocationManager locationmanager;
	private Location mLocation;

	/**
	 * 经度
	 */
	private double latitude;
	/**
	 * 维度
	 */
	private double longitude;
	/**
	 * 城市
	 */
	private String city;

	public LocationAsyncTask() {
		String str = LocationAsyncTask.class.getSimpleName();
		this.TAG = str;
		this.city = "";
	}

	@Override
	protected Location doInBackground(Activity... params) {
		if ((params.length < 1) || (!(params[0] instanceof Context))) {
			throw new IllegalArgumentException();
		}
		this.context = params[0];

		// 开启定位服务。
		locationmanager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria(); // 查询出适合自己的provider条件。
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);// 设置精度。
		criteria.setAltitudeRequired(false);// 是否显示海拔数据。
		criteria.setBearingRequired(false);// 是否获得方向数据。
		criteria.setCostAllowed(true);// 是否容许运营商产生资费
		criteria.setPowerRequirement(Criteria.POWER_LOW);// 电量的消耗等级
		String provider = locationmanager.getBestProvider(criteria, true);// 得到最适合的provider。
		System.out.println(provider + "provider");
		try{
			this.mLocation = locationmanager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);// 获得gps的位置。
			System.out.println(this.mLocation + "location");
			if (this.mLocation == null) {
				this.mLocation = locationmanager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.mLocation;
	}

	protected void onPostExecute(Location location) {
		if (location != null) {
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();
			//java.io.IOException: Unable to parse response from server
			Geocoder mGeocoder = new Geocoder(context, Locale.CHINA);
			List<Address> addrList = null;
			try {
				addrList = mGeocoder.getFromLocation(this.latitude,
						this.longitude, 1);
				this.city = addrList.get(0).getAdminArea();
				if (city != null && city.endsWith("市")) {
					city = city.replaceAll("市$", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getCity() {
		return this.city;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

}