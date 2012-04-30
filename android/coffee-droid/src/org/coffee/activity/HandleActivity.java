package org.coffee.activity;

import org.coffee.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;

public class HandleActivity extends Activity {

	private String TAG = "HandleActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ImageView iv = new ImageView(this);
		
		HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
		mHandlerThread.start();
		
		final Handler mHandler = new Handler(mHandlerThread.getLooper()) {
			public void handleMessage(android.os.Message msg) {
				Log.i(TAG, "Msg --- " + msg + " " + Thread.currentThread().getName());
				try {
					Thread.sleep(1000 * 5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				iv.setImageResource(R.drawable.avatar_0);
			};
		};

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 3; i++) {
					Log.i(TAG, "XXXX " + i + " " + Thread.currentThread().getName());
					mHandler.sendEmptyMessage(i);
				}
			}
		});

		try {
			Thread.sleep(1000 * 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "onCreate end...." + Thread.currentThread().getName());
		this.setContentView(iv);
	}

}
