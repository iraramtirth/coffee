package coffee.im.bluetooth.activity;

import coffee.im.bluetooth.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import coffee.im.bluetooth.activity.base.BaseActivityGroup;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.im.bluetooth.logic.TestLogic;
/**
 * 程序的主类
 * 
 * @author wangtaoyfx
 * 		2013-1-21下午4:28:19
 */
public class MainActivity extends BaseActivityGroup implements OnClickListener {

	private static MainActivity mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		showViewGroup(Tab0Activity.class);
		//
		mHandler = new Handler(this);
		TestLogic.getInstance().addHandler(mHandler);
		
		//throw new NullPointerException();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//String abc = null;
		//System.out.println(abc.length());
	}

	@Override
	public void doInitView() {
		this.setContentView(R.layout.main);
		Button btn0 = (Button) this.findViewById(R.id.tab_0);
		Button btn1 = (Button) this.findViewById(R.id.tab_1);
		Button btn2 = (Button) this.findViewById(R.id.tab_2);

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
		case R.id.tab_0:
			showViewGroup(Tab0Activity.class);
			break;
		case R.id.tab_1:
			//showViewGroup(ChatActivity.class);
			break;
		case R.id.tab_2:
			showViewGroup(Tab2Activity.class);
			break;
		}
	}

	/********************* 以下是setter getter **************************/

	public static MainActivity getContext() {
		return mainActivity;
	}
}