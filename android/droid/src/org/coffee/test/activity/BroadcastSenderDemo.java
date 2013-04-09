package org.coffee.test.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
/**
 * 发送广播 
 * @author coffee
 */
public class BroadcastSenderDemo extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 在AndroidManifest中需要配置
			<receiver android:name="com.example.demo.BroadcastReceiverDemo">
				<intent-filter>
					<action android:name="com.receiver.action.TestAction"/>
				</intent-filter>
			</receiver>
		 */
		//发送广播
		final Intent intent = new Intent("com.receiver.action.TestAction");
		//布局
		LinearLayout lineLayout = new LinearLayout(this);
		//组件
		Button button = new Button(this);
		button.setText("发送广播,通知接受响应");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//发送广播
		        sendBroadcast(intent);
			}
		});
		lineLayout.addView(button);
		button = new Button(this);
		button.setText("退出");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BroadcastSenderDemo.this.finish();
//				System.exit(0);
				NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				//消除通知
				nm.cancelAll();
			}
		});
		lineLayout.addView(button);
		this.addContentView(lineLayout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
	}
	
	
}
