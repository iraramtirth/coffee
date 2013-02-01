package coffee.im.bluetooth.activity;

import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.adapter.DeviceInfoBean;
import coffee.im.bluetooth.utils.BtUtils;

/**
 * 联系人
 * 
 * @author coffee<br>
 *         2013上午11:59:03
 */
public class ContactActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
		// String abc = null;
		// int a = abc.length();
		// System.out.println(a);

		// throw new NullPointerException();
		Set<BluetoothDevice> devices = BtUtils.getBondedDevices();
		for (BluetoothDevice device : devices) {
			DeviceInfoBean info = new DeviceInfoBean();
			info.setDeviceName(device.getName());
		}
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
