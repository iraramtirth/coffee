package com.example.log;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author coffee 2012-11-7下午2:58:37
 */
public class MainActivity extends Activity {

	private Button startBtn;
	private Button stopBtn;

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.startBtn = (Button) this.findViewById(R.id.btn_service_start);
		this.stopBtn = (Button) this.findViewById(R.id.btn_service_stop);

		final Intent mIntent = new Intent(MainActivity.this, LogService.class);

		startBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startService(mIntent);
				// onCreate onStart 返回finish activity的时候 service仍然执行

				// [既执行startService又执行bindService] onCreate onStart onBind
				// 返回 finish 的时候 不会destroy。 当再次启动activity的时候 只执行onStart

				// onCreate onBind 返回finish activity的时候 service--onUnbind
				// onDestroy
				bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
			}
		});

		this.stopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (mServiceConnection != null) {
						unbindService(mServiceConnection);
					}
					stopService(mIntent);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					mServiceConnection = null;
				}

			}
		});

	}

}
