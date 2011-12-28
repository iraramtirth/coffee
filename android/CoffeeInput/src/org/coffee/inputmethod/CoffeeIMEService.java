package org.coffee.inputmethod;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

public class CoffeeIMEService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
	}

	
	@Override
	public View onCreateInputView() {
		Toast.makeText(this, "onCreateInputView", Toast.LENGTH_SHORT).show();
		//软键盘容器
		KeyboardView keyboardView = (KeyboardView) this.getLayoutInflater()
				.inflate(R.layout.keyboard, null);
		//容器中具体的[键]布局
		keyboardView.setKeyboard(new Keyboard(this, R.xml.symbols));
		//如果不设置KeyboardActionListener,则单击键盘的时候会报java.lang.NullPointerException异常
		keyboardView.setOnKeyboardActionListener(this);
		return keyboardView;
	}


	@Override
	public void onPress(int primaryCode) {
	}
	/**
	 * 释放键盘
	 */
	@Override
	public void onRelease(int primaryCode) {
		
	}
	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		InputConnection ic = getCurrentInputConnection();
		ic.beginBatchEdit();
		ic.commitText(String.valueOf((char) primaryCode), 1);
		ic.endBatchEdit();
	}
	@Override
	public void onText(CharSequence text) {
		
	}
	@Override
	public void swipeLeft() {
	}
	@Override
	public void swipeRight() {
	}
	@Override
	public void swipeDown() {
	}
	@Override
	public void swipeUp() {
	}
}
