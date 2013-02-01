package coffee.im.bluetooth.activity;

import org.bluetooth.R;

import android.os.Bundle;
import coffee.im.bluetooth.activity.base.BaseActivity;

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
