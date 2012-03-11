package org.coffee.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AIDLService extends Service {

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
