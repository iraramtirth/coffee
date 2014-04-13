package coffee.im.bluetooth.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.ChatActivity;
import coffee.im.bluetooth.adapter.base.BaseAdapter;
import coffee.im.bluetooth.bean.ContactBean;

public class ContactAdapter extends BaseAdapter<ContactBean> {

	public ContactAdapter(List<ContactBean> items, Activity mContext) {
		super(items, mContext);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.contact_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.contact_name);
			holder.address = (TextView) convertView.findViewById(R.id.contact_address);
			holder.action = convertView.findViewById(R.id.contact_action);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ContactBean item = getItem(position);
		// 设置值
		holder.name.setText(item.getName());
		holder.address.setText(item.getAddress() + ":" + item.getPort());

		((Button) holder.action).setText("聊天");
		// 聊天
		holder.action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, ChatActivity.class);
				intent.putExtra("address", item.getName());
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}

	private class ViewHolder {
		TextView name;
		TextView address;
		View action; // 配对\聊天 button 或者ImageButton
	}

}
