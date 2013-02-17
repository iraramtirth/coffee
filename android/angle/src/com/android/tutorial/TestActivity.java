package com.android.tutorial;

import coffee.angle.CoffeeSurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TestActivity extends Activity {
	private CoffeeSurfaceView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		sv = new CoffeeSurfaceView(this);
		sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		layout.addView(sv);
		setContentView(layout);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
