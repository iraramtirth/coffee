package coffee.im.bluetooth.activity.base;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ListView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.ScanDeviceActivity;
import coffee.im.bluetooth.adapter.DeviceInfoAdapter;
import coffee.im.bluetooth.utils.ActivityMgr;
import coffee.im.bluetooth.utils.BtUtils;
import coffee.utils.framework.Alert;

/**
 * 蓝牙相关的列表抽象类 <br>
 * 主要包括蓝牙的基本操作：打开、配对等
 * 
 * @author coffee<br>
 *         2013上午10:08:58
 */
public abstract class BaseBluetoothListActivity extends BaseActivity {

	/**
	 * 在子类的onCreate中实例化
	 */
	protected List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();

	protected ListView mListView;
	protected DeviceInfoAdapter mListAdapter;

	@Override
	public void findViewById() {
		mListView = (ListView) this.findViewById(R.id.device_list);
		mListAdapter = new DeviceInfoAdapter(mDevices, this);
		mListView.setAdapter(mListAdapter);
	}

	/**
	 * 检查蓝牙的打开状态
	 * 
	 * @return
	 */
	protected boolean checkBluetoothOpen() {
		if (!BtUtils.isOpen()) {
			if (ActivityMgr.peek() != null) {
				Alert.dialog(ActivityMgr.peek(), R.array.alert_bluetooth_open, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BtUtils.requestDeviceOpen(ActivityMgr.peek());
					}
				}, null);
			}
			return false;
		} else {
			// 蓝牙已打开
			startActivity(new Intent(context, ScanDeviceActivity.class));
		}
		return true;
	}
}
