package coffee.im.bluetooth.ui.activity;

import android.os.Bundle;
import cn.com.fetion.R;

import com.fetion.apad.ui.activity.base.BaseActivity;

public class Tab0Activity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void doInitView() {
		setContentView(R.layout.main_tab0);
	}

}
