package org.coffee.inputmethod;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public class CoffeeIME extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// CoffeeIMEService cs = getSystemService(INPUT_METHOD_SERVICE)
//		Settings.Secure.putString(getContentResolver(),
//				Settings.Secure.DEFAULT_INPUT_METHOD,
//				"org.coffee.inputmethod/.CoffeeIMEService");
		onCreateIMM();
	}

	private Object mInputMethodProperties;

	private void onCreateIMM() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		List<InputMethodInfo> imList = imm.getInputMethodList();
		for (InputMethodInfo imi : imList) {
			System.out.println(imi.getId());
			// InputMethodService.
		}
		imm.setInputMethod(findViewById(R.id.test).getWindowToken(),"org.coffee.inputmethod/.CoffeeIMEService");
		String mLastInputMethodId = Settings.Secure.getString(
				getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

	}

}