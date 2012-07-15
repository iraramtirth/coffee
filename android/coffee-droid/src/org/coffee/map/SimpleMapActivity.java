package org.coffee.map;

import java.util.ArrayList;
import java.util.List;

import org.coffee.R;
import org.coffee.map.LocationManager.LocationDataListenr;
import org.coffee.map.LocationManager.LocationResult;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.ItemizedOverlay.OnFocusChangeListener;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SimpleMapActivity extends MapActivity implements OnClickListener,
		LocationDataListenr {
	/**
	 * 进入地图页面的模式
	 */
	public static final String BUNDLE_MODE = "mode";
	/**
	 * 发送位置模式
	 */
	public static final int MODE_SEND = 0;

	/**
	 * 查看位置信息模式
	 */
	public static final int MODE_WATCH = 1;

	/**
	 * 查看地图时需要传进的经度
	 */
	public static final String BUNDLE_LATITUDE = "latitude";
	/**
	 * 查看地图时需要传进的纬度
	 */
	public static final String BUNDLE_LONGITUDE = "longitude";

	/**
	 * 查看地图时需要传进的地址值
	 */
	public static final String BUNDLE_ADDRESS_INFO = "address_info";

	private static final String TAG = "SimpleMapActivity";

	/**
	 * 搜索需要访问URL
	 */
	private static final String SEARCHURL = "http://ajax.googleapis.com/ajax/services/search/local?v=1.0&hl=zh_CN&q=%s&start=0&rsz=5&sll=%s,%s";

	/**
	 * 默认地图缩放大小
	 */
	private static final int DEFAULT_ZOOM = 16;

	/**
	 * 地图展示模式
	 */
	private int mode;

	/**
	 * 显示地址信息的popView
	 */
	private View popView;
	private MapView mapView;
	private MapController mMapCtrl;
	private EditText searchEditText;
	private TextView popViewDesc;
	private TextView pointViewDesc;
	private ImageView pointView;
	private LinearLayout searchBar;
	private ImageView backButton;
	private GeoPoint watchPoint;
	private ProgressDialog mProgress;

	/**
	 * 标识地址的图层
	 */
	private MyItemizedOverlay mItemizedOverlay;
	private WatchModeOverlay mWatchModeOverlay;

	private List<Overlay> mapOverlays;
	private OverlayItem overlayitem = null;

	public final int MSG_VIEW_LOCATIONLATLNG = 0;
	public final int MSG_VIEW_LOCATIONLATLNG_FAIL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_map);
		mode = MODE_SEND;
		initView();
		GeoPoint pointer = null;
		// 如果是查看模式，则必须有经纬度，生成GeoPoint，否则定位到当前用户所在位置
		if (mode == MODE_WATCH) {
			double latitude = getIntent().getExtras()
					.getDouble(BUNDLE_LATITUDE);
			double longitude = getIntent().getExtras().getDouble(
					BUNDLE_LONGITUDE);
			String addressInfo = getIntent().getExtras().getString(
					BUNDLE_ADDRESS_INFO);
			pointer = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
			popViewDesc.setText(addressInfo);
			searchBar.setVisibility(View.GONE);
			pointView.setVisibility(View.GONE);
			pointViewDesc.setVisibility(View.GONE);
			watchPoint = pointer;
			// 使地图跳转到坐标位置
			animateTo(pointer, null);
		} else {
			new LocationManager().getCurrentLocation(null, this, false,
					SimpleMapActivity.this);
		}

	}

	/**
	 * 
	 * 初始化view <BR>
	 * [功能详细描述]
	 */
	private void initView() {
		// searchEditText = (EditText) findViewById(R.id.search_edit);
		// titleTxt = (TextView) findViewById(R.id.title);
		// titleTxt.setText(R.string.location_info);
		// searchBar = (LinearLayout) findViewById(R.id.map_search_bar);
		// findViewById(R.id.location_button).setOnClickListener(this);
		// findViewById(R.id.search_button).setOnClickListener(this);
		// findViewById(R.id.left_button).setOnClickListener(this);
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		mMapCtrl = mapView.getController();
		mMapCtrl.setZoom(DEFAULT_ZOOM);
		pointView = (ImageView) findViewById(R.id.map_point_view);
		pointViewDesc = (TextView) findViewById(R.id.map_point_desc);
		if (mode == MODE_SEND) {
			pointViewDesc.setOnClickListener(this);
			mItemizedOverlay = new MyItemizedOverlay(getResources()
					.getDrawable(R.drawable.map_search_location));
		}
		if (mode == MODE_WATCH) {
			backButton = (ImageView) findViewById(R.id.reback_button);
			backButton.setVisibility(View.VISIBLE);
			backButton.setOnClickListener(this);
			popView = getLayoutInflater().inflate(R.layout.map_pop, null);
			popViewDesc = (TextView) popView.findViewById(R.id.map_pop_desc);
			mapView.addView(popView, new MapView.LayoutParams(
					MapView.LayoutParams.WRAP_CONTENT,
					MapView.LayoutParams.WRAP_CONTENT, null,
					MapView.LayoutParams.BOTTOM_CENTER));
			popView.setVisibility(View.GONE);
			mItemizedOverlay = new MyItemizedOverlay(getResources()
					.getDrawable(R.drawable.map_location));
			mWatchModeOverlay = new WatchModeOverlay(getResources()
					.getDrawable(R.drawable.map_pointer));
		}
		mapOverlays = mapView.getOverlays();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.location_button: {
			new LocationManager().getCurrentLocation(null, this, false,
					SimpleMapActivity.this);
		}
			break;
		case R.id.map_point_desc: {
			GeoPoint pointer = mapView.getMapCenter();
			new GetAddressTask().execute(pointer);
		}
			break;
		case R.id.reback_button:
			animateTo(watchPoint, null);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * [一句话功能简述]发送模式下地图浮标类 [功能详细描述]
	 * 
	 * @author Raul
	 */
	@SuppressWarnings("rawtypes")
	public class MyItemizedOverlay extends ItemizedOverlay implements
			OnFocusChangeListener {
		private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		// 用于设置popview 相对某个位置向x轴偏移
		private int layout_x = -1;
		// 用于设置popview 相对某个位置向y轴偏移
		private int layout_y = -40;

		public MyItemizedOverlay(Drawable defaultMarker) {
			super(boundCenter(defaultMarker));
			setOnFocusChangeListener(this);
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			return super.onTap(p, mapView);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		public void addOverlay(OverlayItem item) {
			mOverlays.add(item);
			populate();
		}

		public void removeOverlay(OverlayItem overlay) {
			mOverlays.remove(overlay);
			populate();
		}

		public void removeOverlay(int location) {
			mOverlays.remove(location);
			populate();
		}

		public void clear() {
			mOverlays.clear();
			populate();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected boolean onTap(int index) {
			OverlayItem itemClicked = mOverlays.get(index);
			setFocus(itemClicked);
			final GeoPoint pointer = itemClicked.getPoint();
			final String titleStr = itemClicked.getTitle();
			final String addressStr = itemClicked.getSnippet();

			AlertDialog.Builder dialog = new AlertDialog.Builder(
					SimpleMapActivity.this);
			dialog.setTitle(searchEditText.getText().toString());
			Log.d(TAG, "itemClicked.getTitle()  =" + titleStr);
			dialog.setMessage(addressStr);
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(searchEditText.getText().toString());
			strBuffer.append(": ");
			strBuffer.append(addressStr);
			final String sentAddress = strBuffer.toString();
			Log.d(TAG, "itemClicked.getSnippet()  =" + addressStr);
			dialog.setCancelable(true);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent();
							intent.putExtra(BUNDLE_LATITUDE,
									pointer.getLatitudeE6());
							intent.putExtra(BUNDLE_LONGITUDE,
									pointer.getLongitudeE6());
							intent.putExtra(BUNDLE_ADDRESS_INFO, sentAddress);
							setResult(RESULT_OK, intent);
							finish();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			dialog.show();
			return true;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
		}

		@Override
		public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
			if (null != newFocus && mode != MODE_SEND) {
				MapView.LayoutParams params = (MapView.LayoutParams) popView
						.getLayoutParams();
				// Y轴偏移
				params.x = layout_x;
				// Y轴偏移
				params.y = layout_y;
				GeoPoint point = newFocus.getPoint();
				params.point = point;
				mMapCtrl.animateTo(point);
				mapView.updateViewLayout(popView, params);
				popView.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 
	 * [一句话功能简述]查看模式下地图浮标类 [功能详细描述]
	 * 
	 * @author Raul
	 */
	@SuppressWarnings("rawtypes")
	public class WatchModeOverlay extends ItemizedOverlay {
		private List<OverlayItem> overlays = new ArrayList<OverlayItem>();

		public WatchModeOverlay(Drawable defaultMarker) {
			super(boundCenter(defaultMarker));
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			return true;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlays.get(i);
		}

		@Override
		public int size() {
			return overlays.size();
		}

		public void addOverlay(OverlayItem item) {
			overlays.add(item);
			populate();
		}

		public void removeOverlay(int location) {
			overlays.remove(location);
		}

		@Override
		protected boolean onTap(int index) {
			return super.onTap(index);
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
		}
	}

	/**
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param ev
	 * @return
	 * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
	 */

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mode == MODE_SEND && event.getAction() == MotionEvent.ACTION_MOVE) {
			if (pointView.getVisibility() == View.GONE) {
				pointView.setVisibility(View.VISIBLE);
				pointViewDesc.setVisibility(View.VISIBLE);
			}

		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 
	 * [一句话功能简述]进入发送模式的Intent [功能详细描述]
	 * 
	 * @param context
	 * @return
	 */
	public static Intent getSendIntent(Context context) {
		Intent intent = new Intent(context, SimpleMapActivity.class);
		intent.putExtra(BUNDLE_MODE, SimpleMapActivity.MODE_SEND);
		return intent;
	}

	/**
	 * 
	 * [一句话功能简述]进入查看模式的Intent [功能详细描述]
	 * 
	 * @param context
	 * @param latitude
	 * @param longitude
	 * @param address
	 * @return
	 */
	public static Intent getWatchIntent(Context context, double latitude,
			double longitude, String address) {
		Intent intent = new Intent(context, SimpleMapActivity.class);
		intent.putExtra(BUNDLE_MODE, SimpleMapActivity.MODE_WATCH);
		intent.putExtra(BUNDLE_LATITUDE, latitude);
		intent.putExtra(BUNDLE_LONGITUDE, longitude);
		intent.putExtra(BUNDLE_ADDRESS_INFO, address);
		return intent;
	}

	/**
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param result
	 */
	@Override
	public void onLocationResult(LocationResult result) {
		Location location = result.getLocation();
		if (location != null) {
			GeoPoint pointer = new GeoPoint(
					(int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			if (mode == MODE_SEND) {
				animateTo(pointer);
			} else if (mode == MODE_WATCH) {
				watchAddressAnimateTo(pointer);
			}
		} else {
			Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * [一句话功能简述]跳转到地图中心位置 [功能详细描述]
	 * 
	 * @param pointer
	 * @param addressInfo
	 */
	private void animateTo(GeoPoint pointer, String addressInfo) {
		overlayitem = new OverlayItem(pointer, null, null);

		if (mItemizedOverlay.size() > 0) {
			mItemizedOverlay.removeOverlay(0);
		}
		mItemizedOverlay.addOverlay(overlayitem);
		mItemizedOverlay.setFocus(overlayitem);
		mapOverlays.add(mItemizedOverlay);
		mMapCtrl.animateTo(pointer);
		mapView.invalidate();
	}

	private void watchAddressAnimateTo(GeoPoint pointer) {
		overlayitem = new OverlayItem(pointer, null, null);
		if (mWatchModeOverlay.size() > 0) {
			mWatchModeOverlay.removeOverlay(0);
		}
		mWatchModeOverlay.addOverlay(overlayitem);
		mWatchModeOverlay.setFocus(overlayitem);
		mapOverlays.add(mWatchModeOverlay);
		mMapCtrl.animateTo(pointer);
		mapView.invalidate();
	}

	private void animateTo(GeoPoint geoPoint) {
		mMapCtrl.animateTo(geoPoint);
		mapView.invalidate();
	}

	/**
	 * 
	 * [一句话功能简述]异步获取地址 [功能详细描述]
	 * 
	 * @author Raul
	 */
	private class GetAddressTask extends AsyncTask<GeoPoint, Void, String> {
		@Override
		protected void onPreExecute() {
			showProgress(SimpleMapActivity.this, "ccccc");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(GeoPoint... params) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String addressName = LocationImpl.getInstance().getAddress(
					params[0].getLatitudeE6() / 1E6,
					params[0].getLongitudeE6() / 1E6);
			Log.d(TAG, "地址 =   " + addressName);

			return addressName;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String address = "";
			closeProgress();
			if (null != result && !result.equals("")) {
				address = result;
			} else {
				address = "xxxxxxxxxxx";
			}
			GeoPoint pointer = mapView.getMapCenter();
			double geoLatitude = pointer.getLatitudeE6() / 1E6;
			double geoLongitude = pointer.getLongitudeE6() / 1E6;
			Intent intent = new Intent();
			intent.putExtra(BUNDLE_LATITUDE, geoLatitude);
			intent.putExtra(BUNDLE_LONGITUDE, geoLongitude);
			intent.putExtra(BUNDLE_ADDRESS_INFO, address);
			Log.d(TAG, "Location =   " + geoLatitude + " --------- "
					+ geoLongitude);
			Log.d(TAG, "address =   " + address);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	/**
	 * 
	 * [一句话功能简述]显示进度对话框 [功能详细描述]
	 * 
	 * @param context
	 * @param message
	 */
	private void showProgress(Context context, CharSequence message) {
		mProgress = new ProgressDialog(context);
		mProgress.setMessage(message);
		mProgress.setIndeterminate(false);
		mProgress.setCancelable(false);
		mProgress.show();
	}

	/**
	 * 
	 * [一句话功能简述]关闭进度对话框 [功能详细描述]
	 */
	private void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class SearchResult {
		/**
		 * 位置经纬度
		 */
		private GeoPoint geoPoint;

		/**
		 * title
		 */
		private String title;

		/**
		 * 具体的位置信息
		 */
		private String addressInfo;

		public GeoPoint getGeoPoint() {
			return geoPoint;
		}

		public void setGeoPoint(GeoPoint geoPoint) {
			this.geoPoint = geoPoint;
		}

		public String getAddressInfo() {
			return addressInfo;
		}

		public void setAddressInfo(String addressInfo) {
			this.addressInfo = addressInfo;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

}
