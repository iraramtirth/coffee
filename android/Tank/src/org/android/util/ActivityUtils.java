package org.android.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {

	/**
	 * @param context
	 * @return : 屏幕宽度
	 */
	public static int getScreenWidth(Activity context) {
		return context.getWindowManager().getDefaultDisplay().getWidth();
	}

	/**
	 * @param context
	 * @return ：屏幕高度 : 分辨率
	 */
	public static int getScreenHeight(Activity context) {
		// DisplayMetrics dm = new DisplayMetrics();
		// context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// return dm.widthPixels;
		return context.getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 全屏 | 非全屏之间的切换
	 * 
	 * @param context
	 */
	public static void setFullScreem(Activity context) {
		int val = context.getWindow().getAttributes().flags;
		// 全屏 66816 - 非全屏 65792
		if (val != 66816) {// 非全屏
			context.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			context.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

	}

	/**
	 * 取消全屏
	 * 
	 * @param context
	 */
	public static void setNotFullScreen(Activity context) {
		context.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 隐藏工具栏
	 */
	public static void setNoTitleBar(Activity context) {
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 隐藏状态栏
	 */
	public static void setNoStateBar(Activity context) {
		// context.requestWindowFeature(Window.FEATURE_PROGRESS);
		context.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static enum ScreenShowType {
		Vertical, // 竖屏
		Horizontal
		// 水平方向
	}

	/**
	 * @param context
	 * @return : 屏幕的显示方式{横屏 | 竖屏}
	 */
	public ScreenShowType screenShowType(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mWidth = dm.widthPixels;
		int mHeight = dm.widthPixels;
		// 竖屏
		if (mWidth > mHeight) {
			return ScreenShowType.Vertical;
		} else {
			return ScreenShowType.Horizontal;
		}
	}

}
