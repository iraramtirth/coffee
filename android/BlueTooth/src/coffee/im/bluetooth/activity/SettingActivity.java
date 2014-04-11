package coffee.im.bluetooth.activity;

import android.os.Bundle;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;

/**
 * 设置
 * 
 * @author coffee<br>
 *         2013上午11:59:45
 */
public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		super.onCreate(savedInstanceState);

	}

	@Override
	public void findViewById() {
		setContentView(R.layout.main_tab2);
	}

}
