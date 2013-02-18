package coffee.frame.adapter;

import java.util.List;

import org.coffee.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import coffee.frame.adapter.base.BaseAdapter;
import coffee.frame.bean.MessageBean;

/**
 * 聊天内容
 * 
 * @author Administrator
 */
public class ChatListAdapter extends BaseAdapter<MessageBean> {

	public ChatListAdapter(List<MessageBean> items, Activity mContext) {
		super(items, mContext);
	}

	public void addChatItem(MessageBean item) {
		super.items.add(item);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View
					.inflate(super.mContext, R.layout.chat_item, null);
		}
		TextView infoView = (TextView) convertView.findViewById(R.id.chat_info);

		infoView.setText(items.get(position).toString());
		return convertView;
	}

}
