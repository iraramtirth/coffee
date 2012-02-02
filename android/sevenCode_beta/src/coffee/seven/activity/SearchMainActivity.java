package coffee.seven.activity;

import android.os.Bundle;
import coffee.seven.R;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.IActivity;

/**
 * 搜索 
 * @author wangtao
 */
public class SearchMainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Bundle localBundle = new Bundle();
		localBundle.putInt(IActivity.KEY_LAYOUT_RES, R.layout.search_main);
		super.onCreate(localBundle);
	}
	
}
