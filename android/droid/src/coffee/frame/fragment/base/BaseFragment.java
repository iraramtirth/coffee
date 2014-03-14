package coffee.frame.fragment.base;

import java.io.Serializable;

import org.coffee.util.view.OnGestureEvent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import coffee.frame.fragment.MainFragment;
import coffee.frame.utils.FragmentMgr;
import coffee.utils.log.Log;

/**
 * 生命周期 FragmentTransaction.add(resId, Fragment) 的时候依次执行
 * onAttach-->onCreate-->onCreateView -->onActivityCreated <br>
 * <br>
 * [可以理解为：当activity对象创建的时候会执行onAttch <br>
 * 当"开始"执行onCreate的时候会执行onCreateView, onCreate执行"完成"会执行onActivityCreated] <br>
 * <br>
 * 销毁的过程依次执行 <br>
 * onPause-->onDestroyView-->onDestroy-->onDetach
 * 
 * @author coffee
 * 
 */
public abstract class BaseFragment extends Fragment implements android.os.Handler.Callback {

	private final String TAG = "BaseFragment";

	/**
	 * Fragment 所在的 FragmentActivity
	 */
	protected Activity mContext;
	/**
	 * 每个Fragment管理一个Handler,主要用来与Logic层进行通信<br>
	 * 该变量需要在子类根据需要实例化
	 */
	protected Handler mHandler = null;

	/**
	 * 是否可编辑，即frame所在的activity是否获有焦点<br>
	 * 只有执行完onResume才为true<br>
	 * onCreate onStart 状态都是false <br>
	 */
	protected boolean isResume = false;

	/**
	 * 用于传参
	 */
	protected Bundle mArgs = new Bundle();
	/**
	 * 设计思想来自 {@link Message#obj}<br>
	 * 主要用于传参
	 */
	public Object obj;

	public BaseFragment() {

	}

	/**
	 * 处理Handler的Message
	 */
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	// TODO 参数设置相关
	/*****************************************************************/
	public void putArgument(String key, Object value) {
		if (value instanceof String) {
			this.mArgs.putString(key, String.valueOf(value));
		} else {
			// 这里面根据情况添加其他类型的数据
			// if (value instanceof Integer) {
			if (value instanceof Serializable) {
				this.mArgs.putSerializable(key, (Serializable) value);
			}
		}
		this.setArguments(mArgs);
	}

	public Object getArgument(String key) {
		Bundle args = getArguments();
		if (args != null) {
			return args.get(key);
		}
		return null;
	}

	/*****************************************************************/
	/**
	 * gragment的手势操作 如果需要的话，子类需要去实现
	 */
	protected OnGestureEvent mOnGestureEvent;

	// = new OnGestureEvent() {
	// /**
	// * 滑动后up时触发
	// */
	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// // 向左
	// if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 20) {
	// }
	// // 向右
	// if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 20) {
	// remove(BaseFragment.this);
	// }
	// return false;
	// }
	// };

	/**
	 * 接收所有事件，包括其他fragment内部的事件
	 * 
	 * @param event
	 */
	public void dispatchTouchEvent(MotionEvent event) {
		if (mOnGestureEvent != null) {
			mOnGestureEvent.onTouch(getView(), event);
		}
	}

	/*
	 * 获取过滤后的事件，即每个fragment只能接收到自己区域内的事件。
	 */
	public void onTouchEvent(MotionEvent event) {

	}

	/**
	 * @return
	 */
	protected MainFragment getMainFragment() {
		return MainFragment.getMainFragment();
	}

	@SuppressWarnings("unchecked")
	public <T> T getFragment(Class<T> fragmentClass) {
		return (T) FragmentMgr.getBaseFragments().get(fragmentClass);
	}

	public <T> View getFragmentLayoutView(Class<T> fragmentClass) {
		return FragmentMgr.getBaseFragmentViewsContainer().get(fragmentClass);
	}

	/******************************* 以下是 Fragment 生命周期 *********************************/
	/**
	 * 当fragment被加入到activity时调用（在这个方法中可以获得所在的activity
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		Log.d(TAG, "onAttach");
	}

	/**
	 * 注意： 子类重载该类的时候需要，在第一行代码设置变量layoutResId <br>
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 该项目中onCreate主要用来初始化mHandler等非layout相关的操作
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart() {
		super.onStart();
		this.isResume = false;
	}

	/**
	 * Fragment可见。 Fragment在一个运行中的activity中并且可见。
	 */
	@Override
	public void onResume() {
		super.onResume();
		this.isResume = true;
		Log.d(TAG, "onResume");
	}

	/**
	 * Fragment可见,但是失去焦点。
	 * 另一个activity处于最顶层，但是fragment所在的activity并没有被完全覆盖（顶层的activity是半透明的或不占据整个屏幕）。
	 */
	@Override
	public void onPause() {
		super.onPause();
		this.isResume = false;
		Log.d(TAG, "onPause");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/******************************* 以上是生命周期 End *********************************/
}
