package coffee.im.bluetooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import coffee.im.bluetooth.ClientService;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivityGroup;
import coffee.im.bluetooth.constant.ConstMsg;

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
		showViewGroup(MessageLogActivity.class);
		Log.d("thread-main-activity", Thread.currentThread().getId() + "");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void findViewById() {
		this.setContentView(R.layout.main);
		// 会话界面
		Button btn0 = (Button) this.findViewById(R.id.tab_1);
		// 联系人界面
		Button btn1 = (Button) this.findViewById(R.id.tab_2);
		// 设置界面
		Button btn2 = (Button) this.findViewById(R.id.tab_3);
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
		case R.id.tab_1:
			showViewGroup(MessageLogActivity.class);
			break;
		case R.id.tab_2:
//			showViewGroup(ContactActivity.class);
			break;
		case R.id.tab_3:
			showViewGroup(ContactActivity.class);
			break;
		}
	}

	/********************* 以下是setter getter **************************/

	public static MainActivity getContext() {
		return context;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(this, ClientService.class);
		stopService(intent);
	}
}