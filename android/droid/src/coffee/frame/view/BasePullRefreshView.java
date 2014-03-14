package coffee.frame.view;

import coffee.utils.log.Log;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

/**
 * 
 * @author coffee
 * 
 *         2014年3月14日上午11:05:59
 */
public abstract class BasePullRefreshView<T extends View> extends LinearLayout {

	protected LoadingLayout mHeaderLayout;
	protected LoadingLayout mFooterLayout;

	public BasePullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		mHeaderLayout = new LoadingLayout(context);
		mFooterLayout = new LoadingLayout(context);
		this.addView(mHeaderLayout, 0);
		this.addView(createRefreshableView(context, attrs));
		this.addView(mFooterLayout);

		scrollTo(0, mHeaderLayout.getWidth());

		setPadding(0, -100, 0, 0);
	}

	protected abstract T createRefreshableView(Context context, AttributeSet attrs);

	private State mState = State.RESET;

	public final boolean isRefreshing() {
		return mState == State.REFRESHING || mState == State.MANUAL_REFRESHING;
	}

	public final void onRefreshComplete() {
		if (isRefreshing()) {
			mState = State.RESET;
		}
	}

	int paddTop = -100;

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

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		Log.d("coffee_scroll", event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE: {
			// smoothScrollTo(getScrollY() -50, 100, 0, null);
			Log.d("coffee_scroll", paddTop -= 3);
			scrollTo(0, paddTop);
			return true;
		}

		case MotionEvent.ACTION_DOWN: {
			paddTop = 0;
			scrollTo(0, 0);

			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			smoothScrollTo(0, 300, 0, null);
			break;
		}
		}

		return true;
	}

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

	static interface OnSmoothScrollFinishedListener {
		void onSmoothScrollFinished();
	}
}
