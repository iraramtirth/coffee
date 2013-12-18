package org.coffee.test.activity;

import org.coffee.util.framework.ImmUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

public class SoftKeyTest extends Activity {
	private SoftKeyTest context = this;
	public final static int SHOWDIALOG = 1;

	private EditText editText = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showDialog(SHOWDIALOG);

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000 * 5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ImmUtils.show(context, editText);
				System.out.println("xx");
			}
		});
		System.out.println("xxxxxx");

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		switch (id) {
		case SHOWDIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("test softkey");
			editText = new EditText(this);
			builder.setView(editText);
			builder.setPositiveButton("的地方", null);
			builder.setNegativeButton("对对方答复", null);
			dialog = builder.create();
			break;
		default:
			break;
		}

		return dialog;
	}
}