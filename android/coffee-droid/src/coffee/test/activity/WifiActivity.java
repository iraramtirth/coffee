package coffee.test.activity;

import coffee.util.framework.WifiUtils;

import android.app.Activity;
import android.os.Bundle;

public class WifiActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WifiUtils.open(this);
		String ip = WifiUtils.getIp();
		System.out.println(ip);
	}
}
