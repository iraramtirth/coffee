package coffee.im.bluetooth.activity;

import coffee.im.bluetooth.R;

import android.os.Bundle;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.im.bluetooth.logic.TestLogic;

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
		super.layoutResource = R.layout.main_tab2;
		super.onCreate(savedInstanceState);
		//
		TestLogic.getInstance().sendMessageDelayed(ConstMsg.MSG_APP_EXIT, null,
				3000);
	}

	@Override
	public void doInitView() {
	}

}
