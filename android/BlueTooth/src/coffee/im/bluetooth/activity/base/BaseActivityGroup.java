/**
 * 
 */
package coffee.im.bluetooth.activity.base;

import coffee.im.bluetooth.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

public abstract class BaseActivityGroup extends BaseActivity {
	protected View mFocusView = null;
	/**
	 * ActivityGroup当前选中的activity页面内容
	 */
	private ViewGroup mViewGroup;
	private LayoutParams mmViewGroupParams;
	private ActivityGroup mActivityGroup;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mmViewGroupParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mActivityGroup = new ActivityGroup();
		//
		mActivityGroup.getLocalActivityManager().dispatchCreate(
				savedInstanceState);
		mActivityGroup.getLocalActivityManager().dispatchResume();
		
		mViewGroup = (ViewGroup) findViewById(R.id.main_content);
	}

	/**
	 * ViewGroup跳转
	 * @param cla
	 */
	protected synchronized void showViewGroup(Class<?> cla) {
		// 每次都去调用LocalActivityManager的startActivity方法，
		// 该方法会对launchMode及intent的flag进行检查，在创建过该activity的情况下是否需要再创建，
		// 目前的设置是不会再创建一个的，即不管调用多少次永远只创建了一个对应的View，并且改方法
		// 会去调用Activity的生命周期方法，这样在切换tab的时候onResume也会被调用了
		try {
			/**
			 * java.lang.IllegalStateException: Activities can't be added until
			 * the containing group has been created. 解决办法： <br/>
			 * 1) mActivityGroup.getLocalActivityManager().dispatchCreate(
			 * savedInstanceState); <br/>
			 * 该代码会执行mActivityGroup#onCreate否则会报上述异常 <br/>
			 * 2) 通过将该类继承ActivityGroup解决 <br/>
			 */
			mFocusView = mActivityGroup.getLocalActivityManager()
					.startActivity(cla.getSimpleName(), new Intent(this, cla))
					.getDecorView();
			// 不能缓存mFocusView，否则弹出各个子Activity的菜单会出现问题
			mViewGroup.removeAllViews();
			mViewGroup.addView(mFocusView, mmViewGroupParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}