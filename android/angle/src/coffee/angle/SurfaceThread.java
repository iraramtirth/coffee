package coffee.angle;

import java.util.List;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import coffee.angle.base.Engine;
import coffee.angle.base.Texture;

/**
 * 
 * @author coffee<br>
 *         2013-3-17下午12:12:15
 */
public class SurfaceThread extends Thread {

	private SurfaceHolder mHolder;
	/**
	 * 线程状态
	 */
	private boolean mThreadRunning = false;

	public SurfaceThread(SurfaceHolder holder) {
		this.mHolder = holder;
	}

	public void setThreadRunning(boolean threadRunning) {
		this.mThreadRunning = threadRunning;
	}

	@Override
	public void run() {
		while (this.mThreadRunning) {
			Canvas canvas = mHolder.lockCanvas();
			boolean isRunable = false;
			try {
				for (List<Texture> textures : Engine.getTextures().values()) {
					for (Texture texture : textures) {
						texture.draw(canvas);
						if (texture.isRnnnable()) {
							isRunable = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mHolder.unlockCanvasAndPost(canvas);
			}
			try {
				if (isRunable) {
					sleep(200);
				} else {
					synchronized (this) {
						this.wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
