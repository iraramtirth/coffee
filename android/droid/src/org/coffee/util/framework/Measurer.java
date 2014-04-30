package org.coffee.util.framework;

import java.lang.reflect.Field;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * 测量器
 * 
 * @author coffee<br>
 *         2013下午5:56:14
 */
public class Measurer {
	public int getViewWidth(Activity activity, View view) {
		if (view.getHeight() == ViewGroup.LayoutParams.MATCH_PARENT) {
			// view.m
			return getDisplayMetrics(activity).widthPixels;
		}
		return view.getWidth();
	}

	public int getViewHeight(Activity activity, View view) {
		if (view.getHeight() == ViewGroup.LayoutParams.MATCH_PARENT) {
			// view.m
			return getDisplayMetrics(activity).heightPixels;
		}
		return view.getHeight();
	}

	public DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public int getStatusBarHeight(Activity context) {
		// 4.0
		if (android.os.Build.VERSION.SDK_INT <= 14) {
			Rect frame = new Rect();
			context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			return statusBarHeight;
		}
		// 4.0.3以后的继续执行
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}
}
