package coffee.frame.view;

import org.coffee.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 该类的刷新操作不带动画,需要子类实现
 * 
 * @author coffee
 * 
 */
public abstract class LoadingLayout extends FrameLayout {

	protected final ImageView mHeaderImage;

	private final TextView mHeaderText;
	private final TextView mSubHeaderText;

	private CharSequence mPullLabel = "下拉刷新";
	private CharSequence mReleaseLabel = "松开加载";
	private CharSequence mRefreshingLabel = "加载中";

	public LoadingLayout(Context context, TypedArray attrs) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);

		mHeaderText = (TextView) findViewById(R.id.pull_to_refresh_text);
		mSubHeaderText = (TextView) findViewById(R.id.pull_to_refresh_sub_text);
		mHeaderImage = (ImageView) findViewById(R.id.pull_to_refresh_image);

		Drawable imageDrawable = context.getResources().getDrawable(R.drawable.default_ptr_rotate);
		// Set Drawable, and save width/height
		setLoadingDrawable(imageDrawable);
	}

	public final void reset() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
		mHeaderImage.setVisibility(View.VISIBLE);

		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(mSubHeaderText.getText())) {
				mSubHeaderText.setVisibility(View.GONE);
			} else {
				mSubHeaderText.setVisibility(View.VISIBLE);
			}
		}
		stopAnimation();
	}

	/**
	 * 下拉
	 */
	public final void onPullToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
		startAnimation();
	}

	/**
	 * 松开
	 */
	public final void onReleaseToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mReleaseLabel);
		}
	}

	/**
	 * 刷新中
	 */
	public final void onRefreshing() {
		if (null != mHeaderText) {
			mHeaderText.setText(mRefreshingLabel);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.GONE);
		}
	}

	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Set Drawable
		mHeaderImage.setImageDrawable(imageDrawable);
		// Now call the callback
		onLoadingDrawableSet(imageDrawable);
	}

	//
	protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

	protected abstract void startAnimation();

	protected abstract void stopAnimation();

	/***************** 以下是set刷新标签 ********************/
	public void setPullLabel(CharSequence pullLabel) {
		mPullLabel = pullLabel;
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	public void setLastUpdatedLabel(CharSequence label) {
		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(label)) {
				mSubHeaderText.setVisibility(View.GONE);
			} else {
				mSubHeaderText.setText(label);
				// Only set it to Visible if we're GONE, otherwise VISIBLE will
				// be set soon
				if (View.GONE == mSubHeaderText.getVisibility()) {
					mSubHeaderText.setVisibility(View.VISIBLE);
				}
			}
		}
	}

}
