package com.example.guagua;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		setContentView(R.layout.activity_main);
		final ShaveView shave = (ShaveView) findViewById(R.id.shave);
		shave.setShaveOpenCallback(new ShaveView.SheveOpenCallback() {
			@Override
			public void callback() {
				Toast.makeText(MainActivity.this, "刮开了", Toast.LENGTH_LONG).show();
			}
		});
		
		findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shave.onReset("哈哈 我爱你");
			}
		});
	}

}
