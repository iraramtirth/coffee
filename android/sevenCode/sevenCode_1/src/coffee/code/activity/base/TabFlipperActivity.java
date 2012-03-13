package coffee.code.activity.base;

import android.os.Bundle;
import coffee.code.activity.HelpActivity;
import coffee.code.activity.ScanMainActivity;
import coffee.code.activity.SearchMainActivity;

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
