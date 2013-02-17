package org.coffee.util.view;

import org.coffee.App;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * 
 * @author coffee<br>
 *         2013上午10:41:24
 */
public class Alert {
	/**
	 * 
	 * @param msg
	 */
	public static void toast(String msg) {
		toast(msg, Toast.LENGTH_SHORT);
	}

	/**
	 * @param msg
	 * @param shortOrLong
	 *            {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void toast(String msg, int shortOrLong) {
		Toast.makeText(App.getContext(), msg, shortOrLong).show();
	}

	/**
	 * 
	 * @param context
	 * @param stringArray
	 *            string资源文件的 <string-array> 定义格式如下<br>
	 *            <string-array name="alert_bluetooth_open"> <br>
	 *            <item>@drawable/icon</item> <br>
	 *            <item>title</item> <br>
	 *            <item>message</item> <br>
	 *            <item>left button text</item> <br>
	 *            <item>center button text</item> <br>
	 *            <item>right button text</item> <br>
	 *            </string-array>
	 * @param listeners
	 */
	public static void dialog(Activity context, int stringArray,
			final OnClickListener[] listeners) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String[] arr = context.getResources().getStringArray(stringArray);
		System.out.println(arr);
//		builder.setIcon();
		builder.setTitle("蓝牙设备未打开");
//		builder.setMessage(messageId);
//		builder.setNegativeButton(text, listener)
	}

	 
	/**
	 * 
	 * @param view
	 * @param msg
	 *            :
	 * @param p
	 *            : View 的 width与height
	 */
	public static void show(Activity context, View view, String msg, int... p) {
		// int[] xy = ViewUtils.getPositionOfCenter(view);
		// if(p.length < 2){
		// p = new int[2];
		// p[0] = 100;
		// p[1] = 60;
		// }
		// xy[0] -= p[0]/2;
		// xy[1] -= p[1]/2;
		// WindowManager wManager = context.getWindowManager();
		// WindowManager.LayoutParams wmParams = new
		// WindowManager.LayoutParams();
		// wmParams.width = p[0]; //宽度
		// wmParams.height = p[1]; //高度
		// //wmParams.x = xy[0]; //x 位移
		// //wmParams.y = xy[1]; //y 位移
		// wmParams.alpha = 0.5f;
		// Button btn = new Button(context);
		// btn.setText(msg);
		// wManager.addView(btn, wmParams);
	}

}
