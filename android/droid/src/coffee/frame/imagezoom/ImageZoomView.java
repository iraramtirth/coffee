package coffee.frame.imagezoom;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * [图片的缩放管理类]<BR>
 * [功能详细描述]
 * 
 * @author wangtaoyfx<br>
 *         2013下午5:30:40
 */
public class ImageZoomView extends View implements Observer {

	/**
	 * 画笔
	 */
	private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	private final Rect mRectSrc = new Rect();
	private final Rect mRectDst = new Rect();
	private float mAspectQuotient;

	private Bitmap mBitmap;
	private ImageZoomState mState;
	private boolean mHasInited;

	/**
	 * 
	 * [构造图片的缩放管理工作类]
	 * 
	 * @param context
	 *            当前操作的上下文对象
	 * @param attrs
	 *            AttributeSet
	 */
	public ImageZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHasInited = false;
	}

	/**
	 * 
	 * [设置图片的ZoomState]<BR>
	 * [功能详细描述]
	 * 
	 * @param state
	 *            ZoomState
	 */
	public void setZoomState(ImageZoomState state) {
		if (mState != null) {
			mState.deleteObserver(this);
		}
		mState = state;
		mState.addObserver(this);
		invalidate();
	}

	/**
	 * 
	 * [画制新的图片]<BR>
	 * [功能详细描述]
	 * 
	 * @param canvas
	 *            画布
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null && mState != null) {
			final int viewWidth = getWidth();
			final int viewHeight = getHeight();
			final int bitmapWidth = mBitmap.getWidth();
			final int bitmapHeight = mBitmap.getHeight();

			final float panX = mState.getPanX();
			final float panY = mState.getPanY();
			final float zoomX = mState.getZoomX(mAspectQuotient) * viewWidth
					/ bitmapWidth;
			final float zoomY = mState.getZoomY(mAspectQuotient) * viewHeight
					/ bitmapHeight;

			// Setup source and destination rectangles
			mRectSrc.left = (int) (panX * bitmapWidth - viewWidth / (zoomX * 2));
			mRectSrc.top = (int) (panY * bitmapHeight - viewHeight
					/ (zoomY * 2));
			mRectSrc.right = (int) (mRectSrc.left + viewWidth / zoomX);
			mRectSrc.bottom = (int) (mRectSrc.top + viewHeight / zoomY);
			mRectDst.left = getLeft();
			mRectDst.top = getTop();
			mRectDst.right = getRight();
			mRectDst.bottom = getBottom();

			// Adjust source rectangle so that it fits within the source image.
			if (mRectSrc.left < 0) {
				mRectDst.left += -mRectSrc.left * zoomX;
				mRectSrc.left = 0;
			}
			if (mRectSrc.right > bitmapWidth) {
				mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
				mRectSrc.right = bitmapWidth;
			}
			if (mRectSrc.top < 0) {
				mRectDst.top += -mRectSrc.top * zoomY;
				mRectSrc.top = 0;
			}
			if (mRectSrc.bottom > bitmapHeight) {
				mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
				mRectSrc.bottom = bitmapHeight;
			}

			canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
		}
	}

	/**
	 * 
	 * [更新]<BR>
	 * [功能详细描述]
	 * 
	 * @param observable
	 *            Observable
	 * @param data
	 *            Object
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object data) {
		invalidate();
	}

	private void calculateAspectQuotient() {
		if (mBitmap == null) {
			return;
		}

		mAspectQuotient = (((float) mBitmap.getWidth()) / mBitmap.getHeight())
				/ (((float) getWidth()) / getHeight());

		if (mState != null && !mHasInited) {
			if ((mBitmap.getWidth() < this.getWidth())
					&& (mBitmap.getHeight() < this.getHeight())) {
				float zoom = Math.min(
						(float) this.getWidth() / mBitmap.getWidth(),
						(float) this.getHeight() / mBitmap.getHeight());

				// 如果长宽都小于屏幕，则初始值使用图片的大小
				mState.setZoom(1 / zoom);
			}

			mHasInited = true;
		}
	}

	/**
	 * 
	 * [用于设置图片源]<BR>
	 * [功能详细描述]
	 * 
	 * @param bitmap
	 *            图片源
	 */
	public void setImage(Bitmap bitmap) {
		mBitmap = bitmap;
		calculateAspectQuotient();
		invalidate();
	}

	/**
	 * 
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param changed
	 *            是否改变
	 * @param left
	 *            左侧距离
	 * @param top
	 *            顶部距离
	 * @param right
	 *            右侧距离
	 * @param bottom
	 *            底部距离
	 * @see android.view.View#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		calculateAspectQuotient();
	}

}
