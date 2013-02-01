package coffee.im.bluetooth.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import cn.com.fetion.R;

import com.fetion.apad.constant.ConstMsg;
import com.fetion.apad.logic.MsgLogic;
import com.fetion.apad.model.MessageModel;
import com.fetion.apad.ui.activity.base.BaseActivity;
import com.fetion.apad.ui.adapter.ChatAdapter;

/**
 * 聊天界面
 * 
 * @author wangtaoyfx 2013-1-17下午1:23:18
 */
public class ChatActivity extends BaseActivity implements OnClickListener {

	/**
	 * 发送按钮，表情按钮，消息内容
	 */
	private View mSendBtn, mEmotionBtn;
	private EditText mContentText;

	private ListView mListView;
	private ChatAdapter mChatAdapter;
	private List<MessageModel> mMsgItems = new ArrayList<MessageModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.mHandler = new Handler(this);
	}

	@Override
	public void doInitView() {
		this.setContentView(R.layout.act_chat);
		this.mSendBtn = this.findViewById(R.id.chat_send);
		this.mEmotionBtn = this.findViewById(R.id.chat_emotion);
		this.mContentText = (EditText) this.findViewById(R.id.chat_content);
		this.mListView = (ListView) this.findViewById(R.id.chat_list);
		//
		this.mSendBtn.setOnClickListener(this);
		this.mEmotionBtn.setOnClickListener(this);
		//
		this.mChatAdapter = new ChatAdapter(mMsgItems, this);
		this.mListView.setAdapter(mChatAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MsgLogic.getInstance().addHandler(super.mHandler);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MsgLogic.getInstance().removeHandler(super.mHandler);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ConstMsg.MSG_IM_RECV_MESSAGE:
			this.mChatAdapter.notifyAdd((MessageModel) msg.obj, true);
			break;
		default:
			super.handleMessage(msg);
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送消息
		case R.id.chat_send:
			String content = mContentText.getText().toString();
//			String toUri = "sip:968980727@fetion.com.cn;p=12208";
			// 发送msg
//			MsgLogic.getInstance().sendChatMessage(null,content);
			// 显示listview
			MessageModel msg = this.createMessageModel(content);
			mMsgItems.add(msg);
			mChatAdapter.notifyData(mMsgItems, true);
			mContentText.setText("");
			break;
		case R.id.chat_emotion:
		default:
			break;
		}
	}

	private MessageModel createMessageModel(String content) {
		MessageModel msg = new MessageModel();
		msg.setContent(content);
		msg.setFaild(0);//
		msg.setTime(System.currentTimeMillis());
		msg.setUid(0);
		return msg;
	}
}
