package coffee.im.bluetooth.activity;

import coffee.im.bluetooth.R;

import android.os.Bundle;
import coffee.im.bluetooth.activity.base.BaseActivity;

/**
 * 
 * @author coffee<br>
 *         2013上午11:57:05
 */
public class ConversationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		super.layoutResource = R.layout.main_tab1;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void doInitView() {
	}

}
