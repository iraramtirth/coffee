/**
 * 
 */
package coffee.frame.activity.base;

import org.coffee.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

@SuppressWarnings("deprecation")
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
		mmViewGroupParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mActivityGroup = new ActivityGroup();
		//
		mActivityGroup.getLocalActivityManager().dispatchCreate(savedInstanceState);
		mActivityGroup.getLocalActivityManager().dispatchResume();
		mActivityGroup.getLocalActivityManager().dispatchDestroy(true);
		mViewGroup = (ViewGroup) findViewById(R.id.main_content);
	}

	/**
	 * ViewGroup跳转
	 * 
	 * @param cla
	 */
	protected synchronized void showViewGroup(Class<?> activityClass) {
		try {
			// 结束Activity
			//destroyCurrentActivity();
			//
			Intent intent = new Intent(this, activityClass);
			mFocusView = mActivityGroup.getLocalActivityManager().startActivity(activityClass.getSimpleName(), intent).getDecorView();
			// 不能缓存mFocusView，否则弹出各个子Activity的菜单会出现问题
			mViewGroup.removeAllViews();
			mViewGroup.addView(mFocusView, mmViewGroupParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void destroyCurrentActivity() {
		String id = mActivityGroup.getLocalActivityManager().getCurrentId();
		mActivityGroup.getLocalActivityManager().destroyActivity(id, true);
	}

	protected void onDestroy() {
		destroyCurrentActivity();
		super.onDestroy();
	}

}