package org.coffee.map;

import org.coffee.R;
import org.coffee.map.LocationManager.LocationResult;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationActivity extends Activity {

	private final String TAG = "LocationActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.location);
		TextView tv = (TextView) this.findViewById(R.id.loc_info);

		LocationResult locResult = LocationImpl.getInstance().getLocation();

		String info = LocationImpl.getInstance().getAddress(
				locResult.getLocation().getLatitude(),
				locResult.getLocation().getLongitude());

		tv.setText(info);
		Log.i(TAG, info + "");
	}

}
