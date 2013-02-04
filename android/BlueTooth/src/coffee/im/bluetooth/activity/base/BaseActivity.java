package coffee.im.bluetooth.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import coffee.im.bluetooth.App;
import coffee.im.bluetooth.utils.ActivityMgr;
import coffee.utils.log.Log;

/**
 * 
 * @author coffee
 * 
 */
public abstract class BaseActivity extends Activity implements Handler.Callback {

	protected Handler mHandler;

	/**
	 * 是否需要将activity放到ActivityMgr管理 注意:ActivityGroup中管理的activity不需要加入
	 */
	protected boolean activityToMgr = true;

	/**
	 * 标题栏左右两侧按钮,中间TextView
	 */
	protected View mTitleViewLeft,mTitleViewCenter, mTitleViewRight;
	/**
	 * 标题栏内容(左、中、右)
	 */
	protected Object mTitleContentleft, mTitleContentCenter,
			mTitleContentRight;

	protected App getApp() {
		return (App) getApplication();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i("Base_" + this, "onCreate");
		doInitView();
		if (activityToMgr) {
			ActivityMgr.push(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (activityToMgr) {
			ActivityMgr.pop();
		}
	}

	/* TODO**************** BaseActivity定义的抽象方法 ********************* */
	/**
	 * 初始化控件
	 */
	public abstract void doInitView();

	/**
	 * 设置标题的相关属性
	 * 
	 * @param leftOnClick
	 *            左侧view的单击事件
	 * @param rightOnClick
	 *            右侧view的单机事件
	 * @param leftContent
	 *            左侧view的内容，一般为String（文字）或者Integer(R.drawable)图片资源
	 * @param centerContent
	 *            中间view的内容，一般为String类型
	 * @param rightContent
	 *            右侧view的内容，同leftContent类似
	 */
	public void setTitle(View.OnClickListener leftOnClick,
			View.OnClickListener rightOnClick, Object leftContent,
			Object centerContent, Object rightContent) {
		// 左侧按钮单机事件
		if (this.mTitleViewLeft != null) {
			this.mTitleViewLeft.setOnClickListener(leftOnClick);
		}
		// 右侧按钮单机事件
		if (this.mTitleViewRight != null) {
			this.mTitleViewRight.setOnClickListener(rightOnClick);
		}
		//设置内容
		this.setTitleViewContent(this.mTitleViewLeft, this.mTitleContentleft);
		this.setTitleViewContent(this.mTitleViewCenter, this.mTitleContentCenter);
		this.setTitleViewContent(this.mTitleViewRight, this.mTitleContentRight);
	}

	/**
	 * 设置标题View内容,分两种情况 :<br>
	 * 一种是：View为Button; Content为String <br>
	 * 一种是 View为ImageButton;content为Integer(即Drawable资源文件)<br>
	 * 
	 * @param titleView
	 * @param titleContent
	 */
	private void setTitleViewContent(View titleView, Object titleContent) {
		if (titleView == null || titleContent == null) {
			return;
		}
		// 一种是：View为Button; Content为String
		if (titleContent instanceof String && titleView instanceof Button) {
			((Button) titleView).setText(String.valueOf(titleContent));
		}
		// 一种是 View为ImageButton;content为Integer(即Drawable资源文件)
		else if (titleContent instanceof Integer
				&& titleView instanceof ImageButton) {
			((ImageButton) titleView).setImageResource(Integer.valueOf(String
					.valueOf(titleContent)));
		}
	}

	/* TODO**************** 父类的实现 ********************* */
	@Override
	public boolean handleMessage(Message msg) {
		System.out.println(msg);
		return true;
	}

	/* TODO**************** 以下重写Activity的方法 ********************* */

	public void startActivity(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		super.startActivity(intent);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		super.startActivityForResult(intent, requestCode);
	}

	/* TODO**************** 以下是 setter getter ********************* */

	public Handler getHandler() {
		return mHandler;
	}
}
