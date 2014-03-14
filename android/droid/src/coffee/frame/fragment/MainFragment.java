package coffee.frame.fragment;

import java.util.Map;

import org.coffee.R;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import coffee.frame.constant.ConstMsg;
import coffee.frame.fragment.base.BaseFragment;
import coffee.frame.utils.FragmentMgr;
import coffee.utils.log.Log;

/**
 * 主界面
 * 
 * 注意主界面布局文件ViewContainer的命名规范暂定义为 <br>
 * ID:fragment _ 功能模块名称 {@link R.layout.fragment_main}
 * 
 * @author coffee
 */
public class MainFragment extends FragmentActivity implements android.os.Handler.Callback, OnClickListener {

	private static MainFragment mContext;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.main);
		// 会话界面
		Button btn0 = (Button) this.findViewById(R.id.tab_conversation);
		// 联系人界面
		Button btn1 = (Button) this.findViewById(R.id.tab_contact);
		// 设置界面
		Button btn2 = (Button) this.findViewById(R.id.tab_setting);
		//
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);

		FragmentMgr.beginTransaction();
		FragmentMgr.add(R.id.main_content, new ConversationFragment());
		FragmentMgr.commit();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ConstMsg.MSG_APP_EXIT:
			System.exit(0);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_conversation:

			break;
		case R.id.tab_contact:

			break;
		case R.id.tab_setting:

			break;
		}
	}

	@Override
	protected void onDestroy() {
		mContext = null;
		super.onDestroy();
	}

	/**
	 * java.lang.IllegalStateException: <br>
	 * Can not perform this action after onSaveInstanceState
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.i("fragment", "dispatchTouchEvent");
//		Map<Class<? extends BaseFragment>, View> viewsContainer = FragmentMgr.getBaseFragmentViewsContainer();
//		for (Class<?> fragmentClass : viewsContainer.keySet()) {
//			View view = viewsContainer.get(fragmentClass);
//			if (ev.getX() > view.getLeft() && ev.getX() < view.getRight() && ev.getY() > view.getTop() && ev.getY() < view.getBottom()) {
//				// view.dispatchTouchEvent(ev);
//				view.onTouchEvent(ev);
//			}
//			FragmentMgr.getBaseFragments().get(fragmentClass).dispatchTouchEvent(ev);
//		}
//		return super.dispatchTouchEvent(ev);
//	}

	/************************************* Getter Setter -Start *****************************************/
	public static MainFragment getMainFragment() {
		return mContext;
	}

	/**
	 * 考虑到后期的扩展,R.id.viewContainer可能会改变，现在增加定义一下方法
	 */
	public int getViewContainerChat() {
		return R.id.main_fragment_center;
	}
	/************************************* Getter Setter -End *******************************************/
}
