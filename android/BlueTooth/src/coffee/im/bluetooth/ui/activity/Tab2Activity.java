package coffee.im.bluetooth.ui.activity;

import android.os.Bundle;
import cn.com.fetion.R;

import com.fetion.apad.constant.ConstMsg;
import com.fetion.apad.logic.TestLogic;
import com.fetion.apad.ui.activity.base.BaseActivity;

public class Tab2Activity extends BaseActivity {

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
