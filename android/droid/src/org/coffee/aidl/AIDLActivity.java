package org.coffee.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * (仅start)生命周期 onCreate-->onStart<br>
 * --------(activity结束)service仍然生存<br>
 * -----------------------------------------
 * (仅bind) 生命周期 onCreate-->onBind<br>
 *  --------activity结束--onUnbind-->onDestroy<br>
 * ----------------------------------------
 * 先start再bind onCreate--onBind--onStart<br>
 *  --------activity结束--onUnbind  只是解绑了 start仍然运行
 * 先bind再start 同上<br>
 * 
 * @author coffee <br>
 *         2013-7-31下午5:27:47
 */
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
		}, Context.BIND_AUTO_CREATE);
		
		this.startService(intent);
	}
}
