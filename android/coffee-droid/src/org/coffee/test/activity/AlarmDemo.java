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
import android.widget.TimePicker;
/**
 * 发送广播 
 * @author wangtao
 */
public class AlarmDemo extends Activity{

	LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//布局
		LinearLayout lineLayout = new LinearLayout(this);
		lineLayout.setLayoutParams(layoutParams);
		lineLayout.setOrientation(LinearLayout.VERTICAL);
		TimePicker timer = new TimePicker(this);
		lineLayout.addView(timer);
		//组件
		Button button = new Button(this);
		button.setText("后台运行，绑定service");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//发送广播
		        //sendBroadcast(new Intent("com.receiver.action.TestAction"));
				
				Intent intent = new Intent();
				intent.setClassName(AlarmDemo.this.getPackageName(), AlarmServiceDemo.class.getName());
				AlarmDemo.this.startService(intent);
			}
		});
		lineLayout.addView(button);
		button = new Button(this);
		button.setText("退出");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlarmDemo.this.finish();
//				System.exit(0);
				 NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				 nm.cancelAll();
			}
		});
		lineLayout.addView(button);
		this.addContentView(lineLayout, layoutParams);
	}
	
	
}
