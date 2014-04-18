package coffee.frame.activity;

import org.coffee.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import coffee.frame.activity.base.BaseActivityGroup;
import coffee.frame.constant.ConstMsg;

/**
 * 程序的主类
 * 
 * @author coffee<br>
 *         2013-1-21下午4:28:19
 */
public class MainActivity extends BaseActivityGroup implements OnClickListener {

	private static MainActivity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		//
		mHandler = new Handler(this);
//		showViewGroup(ConversationActivity.class);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void findViewById() {
		this.setContentView(R.layout.main);
		// 会话界面
		Button btn0 = (Button) this.findViewById(R.id.tab_conversation);
		// 联系人界面
		Button btn1 = (Button) this.findViewById(R.id.tab_contact);
		// 设置界面
		Button btn2 = (Button) this.findViewById(R.id.tab_setting);
		//
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ConstMsg.MSG_APP_EXIT:
			System.exit(0);
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_conversation:
			//showViewGroup(ConversationActivity.class);
			break;
		case R.id.tab_contact:
			//showViewGroup(ContactActivity.class);
			break;
		case R.id.tab_setting:
			//showViewGroup(ScanDeviceActivity.class);
			break;
		}
	}

	/********************* 以下是setter getter **************************/

	public static MainActivity getContext() {
		return context;
	}
}