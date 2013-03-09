package coffee.frame.utils;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import coffee.frame.fragment.MainFragment;
import coffee.frame.fragment.base.BaseFragment;

/**
 * 主要用来管理Fragment<br>
 * 全局的：方法都是static类型、主要用在{@link MainFragment}、{@link BaseFragment} 中
 * 
 * @author coffee
 */
public class FragmentMgr {

	/**
	 * key：Fragment的类型：如ChatFragment.class value： Fragment对象：new ChatFragment()
	 */
	private static Map<Class<? extends BaseFragment>, BaseFragment> fragments = new HashMap<Class<? extends BaseFragment>, BaseFragment>();
	/**
	 * key：Fragment的类型：如ChatFragment.class value： 盛放Fragment的view容器
	 */
	private static Map<Class<? extends BaseFragment>, View> viewsContainer = new HashMap<Class<? extends BaseFragment>, View>();

	private static FragmentTransaction trac;

	private static MainFragment getActivity() {
		return MainFragment.getMainFragment();
	}

	@SuppressLint("Recycle")
	public static FragmentTransaction beginTransaction() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		trac = manager.beginTransaction();
		return trac;
	}

	public static void commit() {
		if (trac != null) {
			trac.commit();
		}
		trac = null;
	}

	/**
	 * 
	 * @param trac
	 * @param containerViewId
	 * @param fragmentClass
	 * @param args
	 */
	public static void add(int containerViewId, BaseFragment fragment) {
		Class<? extends BaseFragment> fragmentClass = fragment.getClass();
		View view = getActivity().findViewById(containerViewId);
		if (trac == null) {// 每次都重新
			trac = beginTransaction();
			trac.add(containerViewId, fragment, fragmentClass.getSimpleName());
			fragments.put(fragmentClass, fragment);
			viewsContainer.put(fragmentClass, view);
			trac.commit();
			trac = null;
		} else { // 批量
			trac.add(containerViewId, fragment, fragmentClass.getSimpleName());
			fragments.put(fragmentClass, fragment);
			viewsContainer.put(fragmentClass, view);
		}

	}

	/************************************ fragment管理 *******************************************/
	/**
	 * Fragment移除 一般只需要传入this对象
	 */
	public static void remove(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction trac = manager.beginTransaction();
		trac.remove(fragment);
		trac.commit();
		//
		fragments.remove(fragment.getClass());
	}

	/**
	 * 删除要移除的Fragment类
	 * 
	 * @param fragmentClass
	 */
	public static <T> void remove(Class<T> fragmentClass) {
		BaseFragment baseFragment = getBaseFragments().get(fragmentClass);
		remove(baseFragment);
	}

	/**
	 * 加载view
	 * 
	 * @param containerViewId
	 *            传入一个R.id.main_fragment_开头的变量
	 * @param fragment
	 */
	public static void replace(int containerViewId, BaseFragment fragment) {
		if (getActivity() == null) {
			return;
		}
		View viewGroup = getActivity().findViewById(containerViewId);
		if (viewGroup == null) {
			return;
		} else {
			viewGroup.setVisibility(View.VISIBLE);
			FragmentManager manager = getActivity().getSupportFragmentManager();
			FragmentTransaction trac = manager.beginTransaction();
			trac.replace(containerViewId, fragment);
			trac.commit();
			//
			fragments.put(fragment.getClass(), fragment);
		}
	}

	/**
	 * 获取MainFragment管理的所有Fragment
	 * 
	 * @return
	 */
	public static Map<Class<? extends BaseFragment>, BaseFragment> getBaseFragments() {
		return fragments;
	}

	/**
	 * 获取MainFragment管理的所有layout视图
	 * 
	 * @return
	 */
	public static Map<Class<? extends BaseFragment>, View> getBaseFragmentViewsContainer() {
		return viewsContainer;
	}

	public static BaseFragment getFragment(
			Class<? extends BaseFragment> fragmentClass) {
		BaseFragment fragment = null;
		if (fragmentClass != null) {
			if (fragments.containsKey(fragmentClass)) {
				fragment = fragments.get(fragmentClass);
			} else {
				try {
					fragment = fragmentClass.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			return null;
		}

		return fragment;
	}
}
