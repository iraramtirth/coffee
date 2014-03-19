package coffee.frame.pull2refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * 
 * @author coffee
 * 
 *         2014年3月13日上午11:31:37
 */
public class XListView extends BasePullRefreshView<ListView> {

	protected ListView mListView;

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ListView createRefreshableView(Context context, AttributeSet attrs) {
		mListView = new InternalListView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		mListView.setId(android.R.id.list);
		if (super.mFooterLayout != null) {
			FrameLayout footerParent = new FrameLayout(context);
			footerParent.addView(mFooterLayout);
			mListView.addFooterView(footerParent);
			//setMoreNo();
			setMoreHas("xxx");
		}
		return mListView;
	}

	public void setMoreHas(String text) {
		mFooterLayout.setVisibility(View.VISIBLE);
		mFooterLayout.setPullLabel("单击加载更多");
	}

	public void setMoreNo() {
		mFooterLayout.setPullLabel("单击加载更多");
		mFooterLayout.setVisibility(View.GONE);
	}

	protected class InternalListView extends ListView {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				super.dispatchDraw(canvas);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				if (isReadyForPull() || XListView.super.getScrollY() != 0) {
					XListView.this.onTouchEvent(ev);
				}
				return super.dispatchTouchEvent(ev);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	@Override
	protected boolean isReadyForPull() {
		return mListView != null && mListView.getFirstVisiblePosition() == 0;
	}

	public void setAdapter(BaseAdapter adapter) {
		mListView.setAdapter(adapter);
	}

}
