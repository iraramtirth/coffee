package coffee.frame.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.coffee.R;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import coffee.frame.activity.base.BaseActivity;
import coffee.frame.adapter.DeviceInfoAdapter;
import coffee.frame.utils.BtUtils;

/**
 * 联系人
 * 
 * @author coffee<br>
 *         2013上午11:59:03
 */
public class ContactActivity extends BaseActivity {
	private List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
		 
		Set<BluetoothDevice> devices = BtUtils.getBondedDevices();
		for (BluetoothDevice device : devices) {
			// DeviceInfoBean info = new DeviceInfoBean();
			// info.setDeviceName(device.getName());
			mDevices.add(device);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void doInitView() {
		setContentView(R.layout.contact);
		ListView listView = (ListView) this.findViewById(R.id.contact_list);
		DeviceInfoAdapter adapter = new DeviceInfoAdapter(mDevices, this);
		listView.setAdapter(adapter);
		
		setTitle(null, new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		}, null, "联系人", "扫描");
	}

}
