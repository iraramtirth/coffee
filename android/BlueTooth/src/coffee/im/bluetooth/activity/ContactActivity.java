package coffee.im.bluetooth.activity;

import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseBluetoothListActivity;
import coffee.im.bluetooth.adapter.DeviceInfoAdapter;
import coffee.im.bluetooth.utils.BtUtils;

/**
 * 联系人界面
 * 
 * @author coffee<br>
 *         2013上午11:59:03
 */
public class ContactActivity extends BaseBluetoothListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		super.layoutResource = R.layout.contact;
		// 获取所有配对设备列表
		Set<BluetoothDevice> devices = BtUtils.getBondedDevices();
		mDevices.addAll(devices); //
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void doInitView() {
		setContentView(R.layout.contact);
		ListView listView = (ListView) this.findViewById(R.id.device_list);
		DeviceInfoAdapter adapter = new DeviceInfoAdapter(mDevices, this);
		listView.setAdapter(adapter);

		setTitle(null, new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		}, null, "联系人", "扫描");

	}

}
