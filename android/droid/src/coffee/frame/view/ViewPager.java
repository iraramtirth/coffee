package coffee.frame.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager工具类
 * 
 * @author wangtaoyfx 2013-1-23下午5:45:53
 */
public class ViewPager extends android.support.v4.view.ViewPager {

	/**
	 * ViewPager管理的的页面
	 */
	private List<View> mItems = new ArrayList<View>();

	/**
	 * this
	 */
	private int mScrollState = -1;

	public ViewPager(Context context) {
		super(context);
	}

	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void invalidateDrawable(Drawable paramDrawable) {
		super.invalidateDrawable(paramDrawable);
		invalidate();
	}

	@Override
	protected boolean verifyDrawable(Drawable paramDrawable) {
		return true;
	}

	private boolean isCircle;

	/**
	 * C 'ABC' A <br>
	 * 列表的首尾各增加一个元素 添加要显示页面到ViewPager
	 */
	public void setItems(List<View> pages, boolean isCircle) {
		this.isCircle = isCircle;
		mItems.clear();
		//
		mItems.addAll(pages);
		//
		super.setAdapter(new ViewPagerAdatper());
	}

	/**
	 * 获取page的数量<br>
	 * 如果page是循环展示的。则会去除头尾<br>
	 * 
	 * @return
	 */
	public int getPageCount() {
		int count = mItems.size();
		if (isCircle) {
			count = mItems.size() - 2;
		}
		if (count < 0) {
			count = 0;
		}
		return count;
	}

	public int getPageScrollState() {
		return this.mScrollState;
	}

	public class ViewPageChangeListener extends android.support.v4.view.ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// Log.i("on_PageScrolled", position + "");
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			mScrollState = state;
			if (isCircle && state == ViewPager.SCROLL_STATE_IDLE) {
				int curr = getCurrentItem();
				// 倒数第二个元素
				int lastReal = getAdapter().getCount() - 2;
				if (curr == 0) {
					setCurrentItem(lastReal, false);
				}
				// 如果-最后一个元素
				else if (curr == lastReal + 1) {
					setCurrentItem(1, false);
				}
			}
		}

		@Override
		public void onPageSelected(int position) {
			Log.i("on_PageSelected", position + "");

		}
	}

	/**
	 * 显示指定位置的page
	 * 
	 * @param position
	 */
	@Override
	public void setCurrentItem(int position) {
		// if (position == this.mItems.size() - 1) {
		// return;
		// }
		super.setCurrentItem(position);

	}

	/**
	 * ViewPager适配器
	 * 
	 * @author wangtaoyfx 2013-1-24上午9:33:55
	 */
	private class ViewPagerAdatper extends PagerAdapter {

		@Override
		public int getCount() {
			return mItems.size();
			// return Integer.MAX_VALUE;
		}

		/**
		 * 该方法必须被覆盖
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.i("on__destroyItem", position + "");
			removeView(mItems.get(position % mItems.size()));
		}

		/**
		 * 比较view与object是否为同一个对象，如果是则显示出胡来 如果不是则不显示 <br/>
		 * object即instantiateItem(ViewGroup,position)返回的对象
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		/**
		 * java.lang.UnsupportedOperationException: Required method
		 * instantiateItem was not overridden
		 * 
		 * 显示当前ViewPager指定位置的视图 该方法必须被重载
		 * 
		 * @param container
		 * @param position
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Log.i("on__instantiateItem", position + "");
			int tmp = position % mItems.size();
			//
			// removeView(mItems.get(tmp));
			try {
				addView(mItems.get(tmp), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mItems.get(tmp);
		}
	}

}
