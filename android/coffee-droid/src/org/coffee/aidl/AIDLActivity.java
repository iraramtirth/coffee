package org.coffee.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class AIDLActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent();
		intent.setClass(this, AIDLService.class);
		this.bindService(intent, new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				try {
					String value = IAIDLAction.Stub.asInterface(service).getName();
				    
					System.out.println(value);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		},  Context.BIND_AUTO_CREATE);
	}
}
