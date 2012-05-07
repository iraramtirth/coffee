package coffee.code.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import coffee.code.activity.base.BaseActivity;
import coffee.code.activity.base.IActivity;
import coffee.code.R;
import coffee.util.lbs.LocationAsyncTask;

import com.google.zxing.client.android.CaptureActivity;

public class ScanMainActivity extends BaseActivity {

	private Spinner mSpinner;

	private final String SPINNER_DEFAULT = "正在定位...";

	private List<String> cityList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(SPINNER_DEFAULT);
			add("北京");
			add("上海");
		}
	};

	protected void onCreate(Bundle bundle) {
		Bundle localBundle = new Bundle();
		localBundle.putInt(IActivity.KEY_LAYOUT_RES, R.layout.scan_main);
		super.onCreate(localBundle);

		TextView view = (TextView) this.findViewById(R.id.scan_main_action);
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ScanMainActivity.this, CaptureActivity.class);
				ScanMainActivity.this.startActivity(intent);
			}
		});
		
		this.startLocation();
	}

	/**
	 * 开启定位
	 */
	private void startLocation() {
		mSpinner = (Spinner) this.findViewById(R.id.scan_main_city);
		//第三个参数 盛放数据的list 。 
		final ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cityList);
		//设置下拉列表的样式
		arrAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//设置数据
		mSpinner.setAdapter(arrAdapter);
		LocationAsyncTask locationAsync = new LocationAsyncTask() {
			@Override
			protected void onPostExecute(Location location) {
				super.onPostExecute(location);
				// 删除默认项
				arrAdapter.remove(SPINNER_DEFAULT);
				int position = cityList.indexOf(getCity());
				if (position != -1) {
					mSpinner.setSelection(position);
				} else {
					// 定位失败
					arrAdapter.add("定位失败");
					mSpinner.setSelection(arrAdapter.getCount() - 1);
				}
			}
		};
		locationAsync.execute(this);
	}
}