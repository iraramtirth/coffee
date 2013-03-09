package coffee.frame.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.coffee.R;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import coffee.frame.adapter.DeviceInfoAdapter;
import coffee.frame.fragment.base.BaseDroidFragment;
import coffee.frame.utils.BtUtils;

/**
 * 联系人
 * 
 * @author coffee<br>
 *         2013上午11:59:03
 */
public class ContactFragment extends BaseDroidFragment {
	private List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Set<BluetoothDevice> devices = BtUtils.getBondedDevices();
		for (BluetoothDevice device : devices) {
			// DeviceInfoBean info = new DeviceInfoBean();
			// info.setDeviceName(device.getName());
			mDevices.add(device);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.layoutResId = R.layout.contact;
		View layout = super.onCreateView(inflater, container,
				savedInstanceState);

		ListView listView = (ListView) layout.findViewById(R.id.contact_list);
		DeviceInfoAdapter adapter = new DeviceInfoAdapter(mDevices,
				this.getActivity());
		listView.setAdapter(adapter);

		setTitle(null, new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		}, null, "联系人", "扫描");
		return layout;
	}
}
