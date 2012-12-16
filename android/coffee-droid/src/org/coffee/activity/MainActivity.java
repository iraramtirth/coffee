package org.coffee.activity;

import org.coffee.App;
import org.coffee.aidl.AIDLService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App.count = 11111;
		
		Log.d("APP", App.count + "");
		
		Intent intent = new Intent();
		
		intent.setClass(this, AIDLService.class);
		
		startService(intent);
	}
}
