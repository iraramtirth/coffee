package coffee.frame.activity;

import org.coffee.R;

import android.os.Bundle;
import coffee.frame.activity.base.BaseActivity;
import coffee.frame.constant.ConstMsg;
import coffee.frame.logic.TestLogic;

/**
 * 设置
 * 
 * @author coffee<br>
 *         2013上午11:59:45
 */
public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
		//
		TestLogic.getInstance().sendMessageDelayed(ConstMsg.MSG_APP_EXIT, null,
				3000);
	}

	@Override
	public void doInitView() {
		setContentView(R.layout.main_tab2);
	}

}
