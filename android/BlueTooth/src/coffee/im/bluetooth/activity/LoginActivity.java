package coffee.im.bluetooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import coffee.im.bluetooth.ClientService;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.server.Config;
import coffee.utils.framework.Alert;

/**
 * 用户登录界面
 * 
 * @author coffee <br>
 *         2014年4月11日上午10:28:35
 */
public class LoginActivity extends BaseActivity {
	private EditText mUsername;

	private EditText mHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ConstMsg.LOGIN_TCP:
					if (msg.obj == null) {
						Intent intent = new Intent();
						intent.setClass(context, MainActivity.class);
						context.startActivity(intent);
						finish();
					} else {
						Alert.toast("登录失败");
					}
					break;
				default:
					break;
				}
			}
		};
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mUsername.requestFocus();
	}

	@Override
	public void findViewById() {
		setContentView(R.layout.login);
		super.findViewById();
		setTitle(null, new TitleRes("登录"), null);
		mUsername = (EditText) findViewById(R.id.username);
		mHost = (EditText) findViewById(R.id.host);
		mHost.setText(Config.getServerHost());
		//
		findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = mUsername.getText().toString();
				if (username == null || username.trim().length() == 0) {
					Alert.toast("请输入用户名");
					return;
				}
				Config.setServerHost(mHost.getText().toString());
				Intent intent = new Intent();
				intent.setClass(context, ClientService.class);
				intent.putExtra("username", username);
				startService(intent);
			}
		});
	}

}
