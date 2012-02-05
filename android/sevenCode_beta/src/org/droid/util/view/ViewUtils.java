package org.droid.util.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ViewUtils {
	/**
	 * 获取组件的文本值
	 */
	public static String getText(Activity thiz, int resId){
		View view = thiz.findViewById(resId);
		if(view instanceof TextView){
			return ((TextView)view).getText().toString(); 
		}
		if(view instanceof EditText){
			return ((EditText)view).getText().toString(); 
		}
		return "";
	}
	/**
	 * 重载 
	 * 查找View的子节点
	 */
	public static String getText(View layout, int resId){
		View view = layout.findViewById(resId);
		if(view instanceof TextView){
			return ((TextView)view).getText().toString(); 
		}
		if(view instanceof EditText){
			return ((EditText)view).getText().toString(); 
		}
		return "";
	}
	
	/**
	 * 获取图像
	 * @param layout
	 * @param resId
	 * @return
	 */
	public static Bitmap getBitmap(RelativeLayout layout, int resId){
		ImageView imageView = ((ImageView)layout.findViewById(resId));
//		Bitmap	productImg = imageView.getDrawingCache();//??? 返回null
		BitmapDrawable mDrawable =  (BitmapDrawable) imageView.getDrawable();
		Bitmap bitmap = mDrawable.getBitmap();
		return bitmap;
	}
	
	/**
	 * 为某一个组件赋值
	 * @param thiz	： 当前活动的Activity
	 * @param resId : 组件ID
	 * @param value : 组件文本值
	 */
	public static void setText(Activity thiz, int resId,String text){
		View view = thiz.findViewById(resId);
		if(view instanceof TextView){
			((TextView)view).setText(text);
		}
	}
	
	public static void setText(View thiz, int resId,String text){
		View view = thiz.findViewById(resId);
		if(view instanceof TextView){
			((TextView)view).setText(text);
		}
	}
	/**
	 * 设置背景图 
	 */
	public static void setBackground(View thiz, int resId){
		View view = thiz.findViewById(resId);
		view.setBackgroundColor(resId);
	}
	
	
	/**
	 * 	获取View的中心坐标
	 */
	public static int[] getPositionOfCenter(View view){
		int left = view.getLeft();
		int top = view.getTop();
		int bottom = view.getBottom();
		int right = view.getRight();
		int x = right - left;
		int y = bottom - top;
		return new int[]{x/2,y/2};
	}
}
