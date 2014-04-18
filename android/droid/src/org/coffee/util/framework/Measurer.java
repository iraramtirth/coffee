package org.coffee.util.framework;

import android.app.Activity;
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

	public static DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics;
	}

}
