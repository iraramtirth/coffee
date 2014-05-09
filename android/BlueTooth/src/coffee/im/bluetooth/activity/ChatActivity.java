package coffee.im.bluetooth.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import coffee.im.bluetooth.ClientService;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.adapter.ChatListAdapter;
import coffee.im.bluetooth.bean.MessageBean;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.im.bluetooth.logic.BluetoothLogic;
import coffee.im.bluetooth.utils.BtUtils;
import coffee.server.tcp.base.MessageParser;

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

	private BluetoothLogic bluetoothLogic;
	/**
	 * 蓝牙地址\或者TCP用户名
	 */
	private String remoteAddress;
	private boolean isBluetooth;
	/**
	 * 聊天内容框
	 */
	private EditText mChatWords = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = true;
		remoteAddress = getExtra("address");
		super.mHandler = new Handler(this);
		bluetoothLogic = BluetoothLogic.getInstance();
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ConstMsg.IM_MESSAGE_RECV:
			String message = String.valueOf(msg.obj);
			String messageBody = MessageParser.getMessageBody(message);
			String userFrom = MessageParser.getUserFrom(message);
			mListAdapter.notifyAdd(new MessageBean(userFrom, messageBody), true);
			mListView.setSelection(mListView.getBottom());
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void findViewById() {
		setContentView(R.layout.chat_main);
		super.findViewById();
		mListView = (ListView) this.findViewById(R.id.chat_list);
		mListAdapter = new ChatListAdapter(null, this);
		mListView.setAdapter(mListAdapter);

		Button chatSend = (Button) this.findViewById(R.id.chat_send);
		chatSend.setOnClickListener(this);
		mChatWords = (EditText) this.findViewById(R.id.chat_words);
		// 是蓝牙，还是TCP通信
		String type = getIntent().getStringExtra("type");
		if ("bluetooth".equals(type)) {
			isBluetooth = true;
			setTitle(new TitleRes("开启服务", this), new TitleRes("聊天"), new TitleRes("连接", this));
		} else {
			setCommonTitle("与" + remoteAddress + "聊天中");
		}
		// this.setTitle(this, this, "开启", "聊天", "连接");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_send:
			String content = mChatWords.getText().toString();
			if (content.length() > 0) {
				if (isBluetooth) {
					bluetoothLogic.sendChatMessage(content + "\n");
					mListAdapter.notifyAdd(new MessageBean(BtUtils.getLocalAddress(), content), true);
				} else {
					String message = MessageParser.getMessageSend(remoteAddress, content);
					ClientService.getInstance().sendMessage(message);
					mListAdapter.notifyAdd(new MessageBean("me", content), true);
				}
				mChatWords.setText("");
				mListView.setSelection(mListView.getBottom());
			}
			break;
		case R.id.title_left_text:
			if (!bluetoothLogic.isConnected()) {
				bluetoothLogic.startServer();
			}
			break;
		case R.id.title_right_text:
			if (!bluetoothLogic.isConnected()) {
				bluetoothLogic.connectionServer(remoteAddress);
			}
			break;
		default:
			break;
		}

	}

}
