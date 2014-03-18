package coffee.frame.view;

import org.coffee.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import coffee.utils.log.Log;

/**
 * 下拉刷新组件BasePullRefreshView继承自LinearLayout<br>
 * 该组件有三个子组件 header--abslistview--footer <br>
 * 初始状态 setPadding(0, -100, 0, 0)将header隐藏,其中-100是header的高度<br>
 * 
 * 通过监听手势, 借助该组件的scrollTo(0,负值) 即向Y负方向滚动,来打到header的显示<br>
 * smoothScrollTo平滑滚动到初始状态<br>
 * 
 * @author coffee
 * 
 *         2014年3月14日上午11:05:59
 */
public abstract class BasePullRefreshView<T extends View> extends LinearLayout {

	/**
	 * 
	 */
	protected LoadingLayout mHeaderLayout;
	protected LoadingLayout mFooterLayout;

	private XListener.Header pullRefreshListener;
	private XListener.Footer loadMoreListener;
	//
	private int mTouchSlop;
	private final int paddTop = -100;
	private final int maxScrollY = -110;
	private int scrollY = 0;
	// 通过这俩操作判断手势方向--向下还是向上
	private float mInitialMotionY;
	private float mLastMotionY;

	public BasePullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
		mHeaderLayout = new RotateLoadingLayout(context, a);
		mFooterLayout = new RotateLoadingLayout(context, a);
		a.recycle();
		// 依次添加
		this.addView(mHeaderLayout, 0);
		this.addView(createRefreshableView(context, attrs), 1);
		//
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		setPadding(0, paddTop, 0, 0);

		if (loadMoreListener != null) {
			mFooterLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					loadMoreListener.loadMore();
				}
			});
		}
	}

	protected abstract T createRefreshableView(Context context, AttributeSet attrs);

	protected abstract boolean isReadyForPull();

	public void setHeaderListener(XListener.Header pullRefreshListener) {
		this.pullRefreshListener = pullRefreshListener;
	}

	public void setFooterListener(XListener.Footer loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

	/**
	 * Helper method which just calls scrollTo() in the correct scrolling
	 * direction.
	 * 
	 * @param value
	 *            - New Scroll value
	 */
	protected final void setHeaderScroll(int value) {
		// Clamp value to with pull scroll range
		final int maximumPullScroll = Math.round(getWidth() / 2);
		value = Math.min(maximumPullScroll, Math.max(-maximumPullScroll, value));
		Log.d("coffee_scroll", value);
		scrollTo(0, value);
	}

	/**
	 * 是否构成刷新的必备条件<br>
	 * 刷新前提, 显示松开刷新。。然后才刷新
	 */
	private boolean toRefresh = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE: {
			// smoothScrollTo(getScrollY() -50, 100, 0, null);
			mLastMotionY = event.getY();
			// 向下滑
			Log.d("coffee_scroll", mLastMotionY + " " + mInitialMotionY);
			if (mLastMotionY - mInitialMotionY > mTouchSlop) {
				Log.d("coffee_scroll", scrollY -= 15);
				if (scrollY > maxScrollY) {
					setHeaderScroll(scrollY);
					if (Math.abs(scrollY - maxScrollY) < 10) {
						mHeaderLayout.onReleaseToRefresh();
						toRefresh = true;
					} else {
						mHeaderLayout.onPullToRefresh();
					}
				}
				mInitialMotionY = event.getY();
			}
			return true;
		}

		case MotionEvent.ACTION_DOWN: {
			scrollY = 0;
			setHeaderScroll(0);
			mInitialMotionY = event.getY();
			mLastMotionY = event.getY();
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (toRefresh) {
				if (pullRefreshListener != null) {
					pullRefreshListener.pullRefresh();
				}
				mHeaderLayout.onRefreshing();
				smoothScrollTo(0, 300, 500, onSmoothScrollFinishedListener);// 重置位置
			} else {
				smoothScrollTo(0, 300, 0, onSmoothScrollFinishedListener);// 重置位置
			}
			toRefresh = false;
			break;
		}

		return true;
	}

	/************************************************ 以下操作时平滑滚动到原始位置相关的代码 *******************************************************/
	/**
	 * 下拉刷新结束后需要重置
	 */
	private OnSmoothScrollFinishedListener onSmoothScrollFinishedListener = new OnSmoothScrollFinishedListener() {

		@Override
		public void onSmoothScrollFinished() {
			mHeaderLayout.reset();
		}
	};

	private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis, OnSmoothScrollFinishedListener listener) {
		if (null != mCurrentSmoothScrollRunnable) {
			mCurrentSmoothScrollRunnable.stop();
		}

		final int oldScrollValue = getScrollY();

		if (oldScrollValue != newScrollValue) {
			if (null == mScrollAnimationInterpolator) {
				// Default interpolator is a Decelerate Interpolator
				mScrollAnimationInterpolator = new DecelerateInterpolator();
			}
			mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

			if (delayMillis > 0) {
				postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
			} else {
				post(mCurrentSmoothScrollRunnable);
			}
		}
	}

	private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
	private Interpolator mScrollAnimationInterpolator;

	final class SmoothScrollRunnable implements Runnable {
		private final Interpolator mInterpolator;
		private final int mScrollToY;
		private final int mScrollFromY;
		private final long mDuration;
		private OnSmoothScrollFinishedListener mListener;

		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		private int mCurrentY = -1;

		public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
			mScrollFromY = fromY;
			mScrollToY = toY;
			mInterpolator = mScrollAnimationInterpolator;
			mDuration = duration;
			mListener = listener;
		}

		@Override
		public void run() {

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY) * mInterpolator.getInterpolation(normalizedTime / 1000f));
				mCurrentY = mScrollFromY - deltaY;
				setHeaderScroll(mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				ViewCompat.postOnAnimation(BasePullRefreshView.this, this);
			} else {
				if (null != mListener) {
					mListener.onSmoothScrollFinished();
				}
			}
		}

		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
	}

	interface OnSmoothScrollFinishedListener {
		void onSmoothScrollFinished();
	}
}
