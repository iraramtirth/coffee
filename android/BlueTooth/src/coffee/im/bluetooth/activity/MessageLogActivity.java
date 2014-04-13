package coffee.im.bluetooth.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.constant.ConstMsg;

/**
 * 消息日志
 * 
 * @author coffee<br>
 *         2013上午11:59:45
 */
public class MessageLogActivity extends BaseActivity {

	private TextView message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case ConstMsg.IM_MESSAGE_SEND:
					message.append("\n发:" + msg.obj);
					break;
				case ConstMsg.IM_MESSAGE_RECV:
					message.append("\n收:" + msg.obj);
					break;
				default:
					break;
				}
			}
		};
		super.onCreate(savedInstanceState);
	}

	@Override
	public void findViewById() {
		setContentView(R.layout.message_log);
		message = (TextView) findViewById(R.id.messages);

	}

}
