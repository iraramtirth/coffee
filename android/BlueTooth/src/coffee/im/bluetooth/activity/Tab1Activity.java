package coffee.im.bluetooth.activity;

import org.bluetooth.R;

import android.os.Bundle;
import coffee.im.bluetooth.activity.base.BaseActivity;

public class Tab1Activity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
//		String abc = null;
//		int a = abc.length();
//		System.out.println(a);
		
//		throw new NullPointerException();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void doInitView() {
		setContentView(R.layout.main_tab1);
	}

}
