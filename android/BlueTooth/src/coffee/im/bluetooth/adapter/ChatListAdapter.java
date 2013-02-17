package coffee.im.bluetooth.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.adapter.base.BaseAdapter;
import coffee.im.bluetooth.bean.MessageBean;

/**
 * 聊天内容
 * 
 * @author Administrator
 */
public class ChatListAdapter extends BaseAdapter<MessageBean> {

	public ChatListAdapter(List<MessageBean> items, Activity mContext) {
		super(items, mContext);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.chat_item, null);
		}
		TextView infoView = (TextView) convertView.findViewById(R.id.chat_info);

		infoView.setText(items.get(position).toString());
		return convertView;
	}

}
