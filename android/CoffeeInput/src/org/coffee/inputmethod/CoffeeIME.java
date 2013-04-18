package org.coffee.inputmethod;

import android.app.Activity;
import android.os.Bundle;

public class CoffeeIME extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setContentView(R.layout.main);

		// CoffeeIMEService cs = getSystemService(INPUT_METHOD_SERVICE)
//		Settings.Secure.putString(getContentResolver(),
//		Settings.Secure.DEFAULT_INPUT_METHOD,
//				"org.coffee.inputmethod/.CoffeeIMEService");
	}
}