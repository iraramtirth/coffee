package coffee.im.bluetooth.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import coffee.im.bluetooth.IActivity;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseBluetoothListActivity;

/**
 * 扫描蓝牙设备
 * 
 * @author coffee<br>
 *         2013下午12:00:02
 */
public class ScanDeviceActivity extends BaseBluetoothListActivity implements
		IActivity {

	private BluetoothAdapter mBtAdapter;
	// 选中的设备address
	private String selectedDeviceAddress;

	private ProgressBar mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = true;
		super.layoutResource = R.layout.scan_device;
		//
		super.onCreate(savedInstanceState);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// 发现设备
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		// 扫描设备结束
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// 设备未开启，则直接退出
		if (mBtAdapter.getState() == BluetoothAdapter.STATE_OFF) {
			Toast.makeText(this, "蓝牙设备未开启", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		// 开始搜索设备， 异步
		mBtAdapter.startDiscovery();
	}

	@Override
	public void doInitView() {
		super.doInitView();
		mProgressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice remoteDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mListAdapter.notifyAdd(remoteDevice, true);
			}
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				mListAdapter.notifyDataSetChanged();
				mProgressBar.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public void onBackPressed() {
		this.mBtAdapter.cancelDiscovery();
		if (mProgressBar.getVisibility() == View.VISIBLE) {
			mProgressBar.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 配对请求--响应
		if (requestCode == REQUEST_CODE_PAIR) {
			// bug
			// mListAdapter.setPireState(true, selectedDeviceAddress);
			mListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			this.unregisterReceiver(mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}