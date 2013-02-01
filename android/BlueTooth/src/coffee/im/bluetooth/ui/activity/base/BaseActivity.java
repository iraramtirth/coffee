package coffee.im.bluetooth.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fetion.apad.FetionApp;
import com.fetion.apad.util.ActivityMgr;
import com.fetion.util.log.Log;

public abstract class BaseActivity extends Activity implements Handler.Callback {

	protected Handler mHandler;

	/**
	 * 是否需要将activity放到ActivityMgr管理 注意:ActivityGroup中管理的activity不需要加入
	 */
	protected boolean activityToMgr = true;

	protected FetionApp getApp() {
		return (FetionApp) getApplication();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	

	/*TODO**************** BaseActivity定义的抽象方法 **********************/
	/**
	 * 初始化控件
	 */
	public abstract void doInitView();

	/*TODO**************** 父类的实现 **********************/
	@Override
	public boolean handleMessage(Message msg) {
		System.out.println(msg);
		return true;
	}

	/*TODO**************** 以下重写Activity的方法 **********************/

	public void startActivity(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		super.startActivity(intent);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		super.startActivityForResult(intent, requestCode);
	}

	/*TODO**************** 以下是 setter getter **********************/

	public Handler getHandler() {
		return mHandler;
	}
}
