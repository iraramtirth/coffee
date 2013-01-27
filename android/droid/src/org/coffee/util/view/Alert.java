package org.coffee.util.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;
/**
 * 
 * @author wangtao
 */
public class Alert {
	public enum AlertType{
		TOAST,
		DIALOG,
		ALERT,
		PROGRESS_DIALOG
	}
	/**
	 * 重载；默认调用AlertType.TOAST....
	 * @param context
	 * @param msg
	 */
	public static void show(Activity context,String msg){
		show(context, msg, AlertType.TOAST);
	}
	/**
	 *   
	 */
	public static void show(Activity context,String msg,int shortOrLong){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 
	 * @param context
	 * @param msg
	 * @param type
	 */
	@SuppressWarnings("static-access")
	public static void show(Activity context,String message, AlertType type){
		switch(type){
		case TOAST:
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			break;
		case PROGRESS_DIALOG:
			progressDialog = new ProgressDialog(context);
			progressDialog.show(context, null, message, true, true);
			break;
		case ALERT:
		
		}
	}
	
	public static void cancal(AlertType type){
		switch(type){
			case PROGRESS_DIALOG:
				progressDialog.cancel();	
		}
	}
	
	private static ProgressDialog  progressDialog; 
	
	/**
	 * 
	 * @param view
	 * @param msg:
	 * @param p: View 的 width与height 
	 */
	public static void show(Activity context,View view,String msg,int... p){
//		int[] xy = ViewUtils.getPositionOfCenter(view);
//		if(p.length < 2){
//			p = new int[2];
//			p[0] = 100;
//			p[1] = 60;
//		}
//		xy[0] -= p[0]/2;
//		xy[1] -= p[1]/2;
//		WindowManager wManager = context.getWindowManager();
//		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
//		wmParams.width = p[0];	//宽度
//		wmParams.height = p[1];	//高度
//		//wmParams.x = xy[0];		//x 位移
//		//wmParams.y = xy[1];		//y 位移
//		wmParams.alpha = 0.5f;
//		Button btn = new Button(context);
//		btn.setText(msg);
//		wManager.addView(btn, wmParams);
	}
	
}
