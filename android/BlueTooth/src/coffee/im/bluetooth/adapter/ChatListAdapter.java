package coffee.im.bluetooth.adapter;

import java.util.Stack;

import coffee.im.bluetooth.R;

import coffee.im.bluetooth.bean.MessageBean;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 聊天内容
 * @author Administrator
 */
public class ChatListAdapter extends BaseAdapter {

	public Stack<MessageBean> chatStack = new Stack<MessageBean>();
	
	private Activity context;
	
	public ChatListAdapter(Activity context){
		this.context = context;
	}
	
	public void addChatItem(MessageBean item){
		chatStack.add(item);
	}
	
	public int getCount() {
		return chatStack.size();
	}
	public Object getItem(int position) {
		
		return null;
	}
	public long getItemId(int position) {
		
		return 0;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(context, R.layout.chat_item, null);
		}
		TextView infoView = (TextView) convertView.findViewById(R.id.chat_info);
		
		infoView.setText(chatStack.get(position).toString());
		context.registerForContextMenu(convertView);
		return convertView;
	}

}