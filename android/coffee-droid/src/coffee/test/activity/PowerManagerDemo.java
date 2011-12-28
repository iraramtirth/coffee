package coffee.test.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;

public class PowerManagerDemo extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "xxx");
		wakeLock.acquire();
	}
}
