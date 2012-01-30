package coffee.seven.activity.base;

import coffee.seven.activity.HelpActivity;
import coffee.seven.activity.OrderHomeActivity;
import coffee.seven.activity.SaleNextActivity;
import coffee.seven.activity.SaleNowActivity;

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
		if(SaleNowActivity.class.getName().equals(className)){
			saleNowActivity = this;
		}
		if(SaleNextActivity.class.getName().equals(className)){
			saleNextActivity = this;
		}
		if(OrderHomeActivity.class.getName().equals(className)){
			saleOrderActivity = this;
		}
		if(HelpActivity.class.getName().equals(className)){
			saleHelpActivity = this;
		}
	}
}
