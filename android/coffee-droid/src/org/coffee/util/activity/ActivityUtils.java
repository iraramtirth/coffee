package org.coffee.util.activity;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {

	/**
	 * @return : 屏幕宽度
	 */
	public static int getScreenWidth(Activity context) {
		return context.getWindowManager().getDefaultDisplay().getWidth();
	}

	/**
	 * @return ：屏幕高度 : 分辨率
	 */
	public static int getScreenHeight(Activity context) {
//		DisplayMetrics dm = new DisplayMetrics();
//		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		return dm.widthPixels;
		return context.getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 获取状态栏的高度
	 */
	public static int getStatusBarHeight(Activity context){
		/*//代码不能放在oncreate中获取,即：只能等到界面创建完成才能获取到
		 * 可以在oncreate执行完以后放到一个非UI线程中获取
			Rect frame = new Rect();  
			context.getWindow().getDecorView()
			.getWindowVisibleDisplayFrame(frame);
			return frame.top;
		*/
		String screen = getScreenWidth(context)+"x"+getScreenHeight(context);
		if(screen.equals("240x320")){
			return 19;//25 * 0.75;
		}
		if(screen.equals("320x480")){
			return 25;//25*1.00
		}
		if(screen.equals("480x800")){
			return 38;//25*1.50
		}
		return 25;
	}
	
	
	/**
	 * 设置全屏
	 * 
	 * @param context
	 */
	public static void setFullScreem(Activity context) {
		int val = context.getWindow().getAttributes().flags;
		// 全屏 66816 - 非全屏 65792
		if(val != 66816){//非全屏
			context.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			context.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
	}
	/**
	 * 取消全屏
	 * @param context
	 */
	public static void setNotFullScreen(Activity context) {
		context.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	/**
	 * 设置-无标题栏
	 */
	public static void setNoTitleBar(Activity context){
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public static enum ScreenShowType {
		Vertical, // 竖屏
		Horizontal // 水平方向
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
	
	/**
	 * 获取手机号 
	 */
	public static String getTelphoneNumber(Activity context){
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = telManager.getLine1Number();
		return phoneNumber;
	}
	
}
