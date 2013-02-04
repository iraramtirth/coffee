package org.coffee.util.framework;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class TitleUtils {
	
	/**
	 * 注意：context.getWindow().findViewById
	 * 该方法需要设设置requestWindowFeature(Window.FEATURE_PROGRESS);
	 */
	public static void setTitleProgressShow(Activity context){
		// 显示进度条
		int titleId = getResId("com.android.internal.R$id", "title_container");
		// 显示标题栏
		ViewGroup mViewGroup = ((ViewGroup) context.getWindow().findViewById(titleId));
		if(mViewGroup != null){
			mViewGroup.setVisibility(View.VISIBLE);
		}
		context.setProgressBarIndeterminateVisibility(true);
	}
	
	public static void setTitleProgressHidden(Activity context){
		// 显示进度条
//		int titleId = getResId("com.android.internal.R$id", "title_container");
//		// 显示标题栏
//		((ViewGroup) context.getWindow().findViewById(titleId)).setVisibility(View.INVISIBLE);
		context.setProgressBarIndeterminateVisibility(false);
		context.setProgress(0);
	}
	
	public static void setTitleProgressGone(Activity context){
		// 显示进度条
		int titleId = getResId("com.android.internal.R$id", "title_container");
		// 显示标题栏
		ViewGroup mViewGroup = ((ViewGroup) context.getWindow().findViewById(titleId));
		if(mViewGroup != null){
			mViewGroup.setVisibility(View.GONE);
		}
		context.setProgressBarIndeterminateVisibility(true);
	}
	
	/**
	 * 获取资源ID
	 * 用法：getResId("com.android.internal.R$id", "title_container");
	 * @return
	 */
	public static int getResId(String rClassName, String resName){
		int resId = 0;
		try{
			Class<?> r = Class.forName(rClassName);
			resId = r.getField(resName).getInt(null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resId;
	}
}
