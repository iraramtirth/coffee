package coffee.frame.imagezoom;

import java.util.Observable;

/**
 * 
 * [用于设置图片的缩放的大小]<BR>
 * [功能详细描述]
 * 
 * @author wangtaoyfx<br>
 *         2013下午5:30:40
 */
public class ImageZoomState extends Observable {

	/**
	 * 缩放比例
	 */
	private float mZoom;
	private float mPanX;
	private float mPanY;

	public float getPanX() {
		return mPanX;
	}

	public float getPanY() {
		return mPanY;
	}

	public float getZoom() {
		return mZoom;
	}

	/**
	 * [用于设置画笔横坐标]<BR>
	 * 
	 * @param panX
	 *            画笔横坐标
	 */
	public void setPanX(float panX) {
		if (panX != mPanX) {
			mPanX = panX;
			setChanged();
		}
	}

	/**
	 * [用于设置画笔纵坐标]<BR>
	 * 
	 * @param panY
	 *            画笔纵坐标
	 */
	public void setPanY(float panY) {
		if (panY != mPanY) {
			mPanY = panY;
			setChanged();
		}
	}

	/**
	 * [用于设置缩放的比例]<BR>
	 * 
	 * @param zoom
	 *            缩放比例
	 */
	public void setZoom(float zoom) {
		if (zoom != mZoom) {
			mZoom = zoom;
			setChanged();
		}
	}

	/**
	 * [获取缩放比例]<BR>
	 * 
	 * @param aspectQuotient
	 *            缩放比例横坐标
	 * @return 缩放比例
	 */
	public float getZoomX(float aspectQuotient) {
		return Math.min(mZoom, mZoom * aspectQuotient);
	}

	/**
	 * [用于获取缩放的纵坐标]<BR>
	 * 
	 * @param aspectQuotient
	 *            纵坐标
	 * @return 纵坐标
	 */
	public float getZoomY(float aspectQuotient) {
		return Math.min(mZoom, mZoom / aspectQuotient);
	}

	/**
	 * 放大一次<br>
	 * 每次0.2倍累加, 最大为3
	 */
	public void setZoomInOnce() {
		float z = getZoom() + 0.2F;
		if (z <= 3.0f) {
			setZoom(z);
			notifyObservers();
		}
	}

	/**
	 * 缩放一次<br>
	 * 每次0.2倍递减, 最小为0.4
	 */
	public void setZoomOutOnce() {
		float z = getZoom() - 0.2F;
		if (z < 0.4) {
			z = 0.4F;
		}
		setZoom(z);
		notifyObservers();
	}
}
