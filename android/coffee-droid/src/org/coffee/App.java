package org.coffee;

import org.coffee.os.CpuUtils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * ContentProvider的初始化位于Application之前
 * 
 * @author coffee
 */
public class App extends Application {
	private final String TAG = App.class.getCanonicalName();
	public static Context context;
	public static final String DB_NAME = "COFFEE";

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate application....");
		context = this;
		//
		//Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000 * 3);

						CpuUtils.info();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static Context getContext() {
		return context;
	}

}
