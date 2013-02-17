package coffee.im.bluetooth;

import android.app.Application;
import android.content.Context;

public class App extends Application {
	private static Context mContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}

	public static Context getContext() {
		return mContext;
	}
}
