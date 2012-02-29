package org.coffee.activity;

import org.coffee.R;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class TabhostActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		TabHost th = (TabHost) findViewById(android.R.id.tabhost);
		//th.setup(this.getLocalActivityManager());
		th.addTab(th
				.newTabSpec("一")
				.setContent(R.id.tv)
				.setIndicator("TAB一",
						this.getResources().getDrawable(R.drawable.icon)));
		th.addTab(th
				.newTabSpec("二")
				.setContent(R.id.tv)
				.setIndicator("TAB二",
						this.getResources().getDrawable(R.drawable.icon)));
	}
}