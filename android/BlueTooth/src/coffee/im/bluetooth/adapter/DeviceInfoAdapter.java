package coffee.im.bluetooth.adapter;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.ChatActivity;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.adapter.base.BaseAdapter;
import coffee.im.bluetooth.constant.ConstIntent;

/**
 * 蓝牙设备信息
 * 
 * @author coffee<br>
 *         2013下午3:22:59
 */
public class DeviceInfoAdapter extends BaseAdapter<BluetoothDevice> {

	public DeviceInfoAdapter(List<BluetoothDevice> items, Activity mContext) {
		super(items, mContext);
	}

	// 设置配对状态
	// public void setPireState(boolean isPair, String adderss) {
	// for (ContactBean info : infoList) {
	// if (info.getDeviceAddress().equals(adderss)) {
	// info.setPair(isPair);
	// break;
	// }
	// }
	// }

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.device_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.device_name);
			holder.address = (TextView) convertView
					.findViewById(R.id.device_address);
			holder.action = convertView.findViewById(R.id.device_action);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final BluetoothDevice device = getItem(position);
		// 设置值
		holder.name.setText(device.getName());
		holder.address.setText(device.getAddress());
		// 如果还未配对
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			((Button) holder.action).setText("配对");
			// 配对
			holder.action.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//
				}
			});
		} else {
			((Button) holder.action).setText("聊天");
			// 聊天
			holder.action.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((BaseActivity) mContext).startActivity(ChatActivity.class,
							ConstIntent.EXTRA_BLUETOOTH_DEVICE,
							device.getAddress());
				}
			});
		}

		return convertView;
	}

	private class ViewHolder {
		TextView name;
		TextView address;
		View action; // 配对\聊天 button 或者ImageButton
	}

}
