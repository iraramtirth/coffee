package coffee.test.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

public class MorseCode extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vibrator vibrate = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = MorseCodeConverter.pattern("hello world");
		vibrate.vibrate(pattern,-1);
//		vibrate.vibrate(1000); //震动1秒钟
	}
}
