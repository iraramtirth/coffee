package com.android.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import coffee.angle.CoffeeSurfaceView;
import coffee.angle.base.Engine;
import coffee.angle.base.Sprite;

public class TestActivity extends Activity implements View.OnClickListener{
	private CoffeeSurfaceView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.surface_view);
		
//		LinearLayout layout = new LinearLayout(this);
//		sv = new CoffeeSurfaceView(this);
//		sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
//		layout.addView(sv);
//		setContentView(layout);
		View click = findViewById(R.id.click);
		click.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Engine.createSprite(R.drawable.asteroid01);
//		Engine.notifyThread();
	}
}
