package coffee.im.bluetooth.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.adapter.ChatListAdapter;
import coffee.im.bluetooth.bean.MessageBean;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.im.bluetooth.logic.ChatLogic;
import coffee.im.bluetooth.utils.BtUtils;

/**
 * 聊天界面
 * 
 * @author coffee<br>
 *         2013上午9:53:24
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {

	private ChatListAdapter mListAdapter;

	/**
	 * 聊天记录列表
	 */
	private ListView mListView;

	private ChatLogic mChatLogic = ChatLogic.getInstance();

	private String mRemoteAddress;
	/**
	 * 聊天内容框
	 */
	private EditText mChatWords = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = true;
		super.onCreate(savedInstanceState);
		mRemoteAddress = getExtra("device");
		super.mHandler = new Handler(this);
		mChatLogic.startServer();
	}


	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ConstMsg.MSG_IM_RECV_MESSAGE:
			mListAdapter.notifyAdd(new MessageBean(BtUtils.getLocalAddress(),
					String.valueOf(msg.obj)), true);
			mListView.setSelection(mListView.getBottom());
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void findViewById() {
		setContentView(R.layout.chat_main);
		mListView = (ListView) this.findViewById(R.id.chat_list);
		mListAdapter = new ChatListAdapter(null, this);
		mListView.setAdapter(mListAdapter);

		Button chatSend = (Button) this.findViewById(R.id.chat_send);
		chatSend.setOnClickListener(this);
		mChatWords = (EditText) this.findViewById(R.id.chat_words);

		//this.setTitle(this, this, "开启", "聊天", "连接");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_send:
			String content = mChatWords.getText().toString();
			if (content.length() > 0) {
				ChatLogic.getInstance().sendChatMessage(content + "\n");
				mChatWords.setText("");
				mListAdapter.notifyAdd(
						new MessageBean(BtUtils.getLocalAddress(), content),
						true);
				mListView.setSelection(mListView.getBottom());
			}
			break;
		case R.id.title_left_text:
			if (!mChatLogic.isConnected()) {
				mChatLogic.startServer();
			}
			break;
		case R.id.title_right_text:
			if (!mChatLogic.isConnected()) {
				mChatLogic.connectionServer(mRemoteAddress);
			}
			break;
		default:
			break;
		}

	}

}
