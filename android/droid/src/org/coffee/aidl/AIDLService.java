package org.coffee.aidl;

import org.coffee.App;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AIDLService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		Log.d("APP", App.count + "");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	private IAIDLAction.Stub stub = new IAIDLAction.Stub() {

		@Override
		public String getName() throws RemoteException {
			return "hello world";
		}
	};

	@Override
	public IBinder onBind(Intent intent) {

		return stub;
	}

}
