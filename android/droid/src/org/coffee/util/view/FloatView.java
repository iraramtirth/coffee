package org.coffee.util.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


/**
 * 窗口浮动
 * @author coffee <br>
 *          2013-7-25下午12:11:18
 */
public class FloatView {
	private final static String TAG = "FloatView";

	private static WindowManager mWindowManager;
	private static WindowManager.LayoutParams wmParams;
	private static View mFloatView;

	/**
	 * 默认头像
	 * 
	 * @return
	 */
	private static View getDefaultView(Activity context) {
		 return null;
	}


	/**
	 * 显示浮动组件
	 * 
	 * @param context
	 * @param floatView
	 */
	public static void show(Activity context, final View floatView) {
		if (floatView == null) {
			mFloatView = getDefaultView(context);
		} else {
			mFloatView = floatView;
		}
		// 获取LayoutParams对象
		wmParams = new WindowManager.LayoutParams();
		// 获取的是LocalWindowManager对象
		// >android.view.Window$LocalWindowManager@40d91f90
		mWindowManager = context.getWindowManager();
		Log.i(TAG, "mWindowManager1--->" + context.getWindowManager());
		mWindowManager = context.getWindow().getWindowManager();
		Log.i(TAG, "mWindowManager2--->" + context.getWindow().getWindowManager());

		// 获取的是CompatModeWrapper对象
		// android.view.WindowManagerImpl$CompatModeWrapper@40d9df40
		mWindowManager = (WindowManager) context.getApplication().getSystemService(Context.WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager3--->" + mWindowManager);
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		;
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		mWindowManager.addView(mFloatView, wmParams);
		// 绑定触摸移动监听
		mFloatView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				wmParams.x = (int) event.getRawX() - mFloatView.getWidth() / 2;
				// 25为状态栏高度
				wmParams.y = (int) event.getRawY() - mFloatView.getHeight() / 2 - 40;
				mWindowManager.updateViewLayout(mFloatView, wmParams);
				return false;
			}
		});
	}

	/**
	 * 移除浮动组件
	 * 
	 */
	public static void close(Context context) {
		if (mFloatView != null && mFloatView.isShown()) {
			WindowManager wm = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
			wm.removeView(mFloatView);
		}
	}
}
