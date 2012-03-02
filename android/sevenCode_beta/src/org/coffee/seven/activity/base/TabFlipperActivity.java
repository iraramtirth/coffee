package org.coffee.seven.activity.base;

import coffee.seven.activity.HelpActivity;
import coffee.seven.activity.ScanMainActivity;
import coffee.seven.activity.SearchMainActivity;

import android.os.Bundle;

public class TabFlipperActivity extends FlipperActivity {

	public static FlipperActivity saleNowActivity;
	public static FlipperActivity saleNextActivity;
	public static FlipperActivity saleOrderActivity;
	public static FlipperActivity saleHelpActivity;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		String className =  getIntent().getStringExtra(ACTIVITY_CLASS_NAME);
		if(ScanMainActivity.class.getName().equals(className)){
			saleNowActivity = this;
		}
		if(SearchMainActivity.class.getName().equals(className)){
			saleNextActivity = this;
		}
//		if(OrderHomeActivity.class.getName().equals(className)){
//			saleOrderActivity = this;
//		}
		if(HelpActivity.class.getName().equals(className)){
			saleHelpActivity = this;
		}
	}
}
