package coffee.frame.imagezoom;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ZoomControls;

/**
 * 
 * [图片缩放的比例监听]<BR>
 * [功能详细描述]
 * 
 * @author wangtaoyfx<br>
 *         2013下午5:30:40
 */
public class ImageZoomListener implements View.OnTouchListener {

	private ImageZoomState mState;

	private float mX;
	private float mY;
	private float mGap;
	private ZoomControls zoomCrl;
	private float zoomOriginal = -1;
	private float move;

	public void setZoomState(ImageZoomState state) {
		mState = state;
	}

	/**
	 * 
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param v
	 *            View
	 * @param event
	 *            MotionEvent
	 * @return boolean
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 *      android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (zoomOriginal == -1) {
			zoomOriginal = mState.getZoom();
		}
		final int action = event.getAction();
		int pointCount = event.getPointerCount();
		if (pointCount == 1) {
			final float x = event.getX();
			final float y = event.getY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				mX = x;
				mY = y;
				break;
			case MotionEvent.ACTION_MOVE: {
				final float dx = (x - mX) / v.getWidth();
				final float dy = (y - mY) / v.getHeight();
				float moveX = mState.getPanX() - dx;
				float moveY = mState.getPanY() - dy;

				moveX = (moveX > 1.0f) ? 1.0f : (moveX < 0f) ? 0f : moveX;
				moveY = (moveY > 1.1f) ? 1.1f : (moveY < 0f) ? 0f : moveY;
				mState.setPanX(moveX);
				mState.setPanY(moveY);
				mState.notifyObservers();
				mX = x;
				mY = y;
				break;
			}
			case MotionEvent.ACTION_UP:
				if (zoomCrl != null) {
				}
				break;
			}
		} else if (pointCount == 2) {
			final float x0 = event.getX(event.getPointerId(0));
			final float y0 = event.getY(event.getPointerId(0));

			final float x1;
			final float y1;
			try {
				x1 = event.getX(event.getPointerId(1));
				y1 = event.getY(event.getPointerId(1));
			} catch (IllegalArgumentException e) {
				Log.e("", "catch out of range!");
				return true;
			}

			final float gap = getGap(x0, x1, y0, y1);
			switch (action) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
			case MotionEvent.ACTION_POINTER_1_DOWN:
				mGap = gap;
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
				mX = x1;
				mY = y1;
				if (move < zoomOriginal || move > 2.0f) {
					return2Original();
				}
				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				mX = x0;
				mY = y0;
				if (move < zoomOriginal || move > 2.0f) {
					return2Original();
				}
				break;
			case MotionEvent.ACTION_MOVE: {
				final float dgap = (gap - mGap) / mGap;
				move = mState.getZoom() * (float) Math.pow(5, dgap);
				move = move > 0.1f ? move : 0.1f;
				move = move > 3.5f ? 3.5f : move;
				mState.setZoom(move);
				mState.notifyObservers();
				mGap = gap;
				break;
			}
			}
		}
		return true;
	}

	private float getGap(float x0, float x1, float y0, float y1) {
		return (float) Math.pow(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2),
				0.5);
	}

	public void setHideOrShow(ZoomControls controls) {
		this.zoomCrl = controls;
	}

	private void return2Original() {
		while (move < zoomOriginal) {
			mState.setZoom(move);
			mState.notifyObservers();
			move += 0.01f;
		}
		while (move > 2.0f) {
			mState.setZoom(move);
			mState.notifyObservers();
			move -= 0.01f;
		}
	}

}
