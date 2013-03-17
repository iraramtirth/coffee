package coffee.angle;

import coffee.angle.base.Engine;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * 
 * @author coffee<br>
 *         2013上午7:21:29
 */
public class CoffeeSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	/**
	 * 线程
	 */
	private SurfaceThread mThread;

	public CoffeeSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	public CoffeeSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread = new SurfaceThread(holder);
		mThread.setThreadRunning(true);
		mThread.start();

		Engine.setSurfaceThread(mThread);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mThread.setThreadRunning(false);
	}

}
