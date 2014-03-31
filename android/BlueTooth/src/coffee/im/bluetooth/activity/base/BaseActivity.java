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
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.MainActivity;
import coffee.im.bluetooth.utils.ActivityMgr;

/**
 * 基类：<br>
 * 注意: 基类要处理字段两个字段 <br>
 * 1)activityToMgr：标志位,如果为true 则该类会被加入到{@link ActivityMgr}管理<br>
 * 2)layoutResource：子类需要在子类的onCreate的首行代码中设置该字段
 * 
 * @author coffee<br>
 *         2013-2-5上午7:39:05
 */
public abstract class BaseActivity extends Activity implements Handler.Callback {

	protected Handler mHandler;

	/**
	 * 是否需要将activity放到ActivityMgr管理 注意:ActivityGroup中管理的activity不需要加入<br>
	 * 此类activity需要在onCreate的第一行代码中设置super.activityToMgr = false;
	 */
	protected boolean activityToMgr = true;

	protected int layoutResource = -1;

	/**
	 * 标题栏左右两侧按钮,中间TextView
	 */
	protected View mTitleViewLeft, mTitleViewCenter, mTitleViewRight;

	protected App getApp() {
		return (App) getApplication();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (layoutResource != -1) {
			setContentView(layoutResource);
		}
		// 初始化标题栏,标题栏在父类中初始化，如果无特殊要求，不需要覆盖
		doInitTitle();
		doInitView();

		// 初始化标题栏,标题栏在父类中初始化，如果无特殊要求，不需要覆盖
		doInitTitle();

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
	 * 初始化控件(出标题栏之外的)
	 */
	public abstract void doInitView();

	/* TODO**************** 父类的实现 ********************* */
	private void doInitTitle() {
		mTitleViewLeft = findViewById(R.id.title_left);
		mTitleViewCenter = findViewById(R.id.title_center);
		mTitleViewRight = findViewById(R.id.title_right);
	}

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
	public void setTitle(View.OnClickListener leftOnClick, View.OnClickListener rightOnClick, Object leftContent, Object centerContent, Object rightContent) {
		// 左侧按钮单机事件
		if (this.mTitleViewLeft != null) {
			this.mTitleViewLeft.setOnClickListener(leftOnClick);
		}
		// 右侧按钮单机事件
		if (this.mTitleViewRight != null) {
			this.mTitleViewRight.setOnClickListener(rightOnClick);
		}
		// 设置内容
		this.setTitleViewContent(this.mTitleViewLeft, leftContent);
		this.setTitleViewContent(this.mTitleViewCenter, centerContent);
		this.setTitleViewContent(this.mTitleViewRight, rightContent);
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
		// 设置可见
		titleView.setVisibility(View.VISIBLE);
		// 一种是：View为Button; Content为String
		if (titleContent instanceof String && titleView instanceof Button) {
			((Button) titleView).setText(String.valueOf(titleContent));
		}
		// 一种是 View为ImageButton;content为Integer(即Drawable资源文件)
		else if (titleContent instanceof Integer && titleView instanceof ImageButton) {
			((ImageButton) titleView).setImageResource(Integer.valueOf(String.valueOf(titleContent)));
		}
	}

	/* TODO**************** 父类的实现 ********************* */

	@Override
	public boolean handleMessage(Message msg) {
		return true;
	}

	/* TODO**************** 以下重写Activity的方法 ********************* */

	public void startActivity(Class<?> activityClass, Object... params) {
		Intent intent = new Intent();
		// intent.setAction(action);
		intent.setClass(this, activityClass);
		for (int i = 0; i < params.length; i += 2) {
			if (i + 1 < params.length) {
				intent.putExtra(String.valueOf(params[i]), String.valueOf(params[i + 1]));
			}
		}
		if (ActivityMgr.peek() == null) {
			MainActivity.getContext().startActivity(intent);
		} else {
			ActivityMgr.peek().startActivity(intent);
		}
	}

	public String getExtra(String paramName) {
		if (getIntent() != null && getIntent().getExtras() != null) {
			return String.valueOf(getIntent().getExtras().get(paramName));
		}
		return null;
	}

	/* TODO**************** 以下是 setter getter ********************* */

	public Handler getHandler() {
		return mHandler;
	}
}
