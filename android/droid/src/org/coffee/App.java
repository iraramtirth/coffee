package org.coffee;

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
	private static Context context;
	public static final String DB_NAME = "COFFEE";

	public static int count = 100;

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate application....");
		context = this;
	}

	public static Context getContext() {
		return context;
	}

}
