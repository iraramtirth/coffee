package org.coffee.process;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CoffeeService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
