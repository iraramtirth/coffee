package org.coffee.aidl;

import org.coffee.App;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author coffee <br>
 *         2013-7-31下午5:15:48
 */
public class AIDLService extends Service {
	private String TAG = "AIDLService";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		Log.d(TAG, App.count + "");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(TAG, "onStart");
	}

	private IAIDLAction.Stub stub = new IAIDLAction.Stub() {

		@Override
		public String getName() throws RemoteException {
			return "hello world";
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return stub;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
}

