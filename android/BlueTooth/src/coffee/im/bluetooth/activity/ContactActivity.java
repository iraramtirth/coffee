package coffee.im.bluetooth.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.ListView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.adapter.ContactAdapter;
import coffee.im.bluetooth.adapter.DeviceInfoAdapter;
import coffee.im.bluetooth.bean.ContactBean;
import coffee.im.bluetooth.utils.BtUtils;
import coffee.server.Online;

/**
 * 联系人界面
 * 
 * @author coffee<br>
 *         2013上午11:59:03
 */
public class ContactActivity extends BaseActivity {

	private ListView mListView;
	private List<ContactBean> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		showContact();
	}

	@Override
	public void findViewById() {
		setContentView(R.layout.contact);
		super.findViewById();
		mListView = (ListView) this.findViewById(R.id.contact_list);
		setTitle(null, new TitleRes("联系人"), null);
	}

	/**
	 * 显示联系人
	 */
	void showContact() {
		if (items == null) {
			items = new ArrayList<ContactBean>();
		} else {
			items.clear();
		}
		for (String name : Online.getItems(1).keySet()) {
			Online.Reg reg = Online.getItems(1).get(name);
			ContactBean item = new ContactBean();
			item.setName(name);
			item.setAddress(reg.getHost());
			item.setPort(reg.getPort());
			items.add(item);
		}
		ContactAdapter adapter = new ContactAdapter(items, MainActivity.getContext());
		mListView.setAdapter(adapter);
	}

	/**
	 * 显示蓝牙设备列表
	 */
	void showBluetoothList() {
		// 获取所有配对设备列表
		Set<BluetoothDevice> devices = BtUtils.getBondedDevices();
		List<BluetoothDevice> devicesList = new ArrayList<BluetoothDevice>(devices);
		DeviceInfoAdapter adapter = new DeviceInfoAdapter(devicesList, this);
		mListView.setAdapter(adapter);
	}

}
