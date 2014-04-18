package coffee.frame.activity.base;

import org.coffee.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import coffee.frame.activity.MainActivity;
import coffee.frame.utils.ActivityMgr;

/**
 * 基类：<br>
 * 注意: 基类要处理字段两个字段 <br>
 * 1)activityToMgr：标志位,如果为true 则该类会被加入到{@link ActivityMgr}管理<br>
 * 2)layoutResource：子类需要在子类的onCreate的首行代码中设置该字段
 * 
 * @author coffee<br>
 *         2013-2-5上午7:39:05
 */
public abstract class FrameBaseActivity extends Activity implements Handler.Callback {

	protected Handler mHandler;
	protected FrameBaseActivity context;
	/**
	 * 是否需要将activity放到ActivityMgr管理 注意:ActivityGroup中管理的activity不需要加入<br>
	 * 此类activity需要在onCreate的第一行代码中设置super.activityToMgr = false;
	 */
	protected boolean activityToMgr = true;

	protected App getApp() {
		return (App) getApplication();
	}

	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (activityToMgr) {
			ActivityMgr.push(this);
		}
		if(mHandler != null){
			HandlerMgr.put(this.getClass().getSimpleName(), mHandler);
		}
		findViewById();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (activityToMgr) {
			ActivityMgr.pop();
		}
		if (mHandler != null) {
			HandlerMgr.remove(this.getClass().getSimpleName());
		}
	}

	/* TODO**************** BaseActivity定义的抽象方法 ********************* */
	/**
	 * 初始化控件(出标题栏之外的)
	 */
	protected abstract void findViewById();

	 

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
