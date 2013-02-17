package coffee.angle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.tutorial.R;

/**
 * 
 * 
 * @author coffee<br>
 *         2013上午7:21:29
 */
public class CoffeeSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;

	/**
	 * 线程
	 */
	private CoffeeThread mThread;

	public CoffeeSurfaceView(Context context) {
		super(context);
		this.doInit();
	}

	private void doInit() {
		this.mHolder = getHolder();
		this.mHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread = new CoffeeThread();
		mThread.setThreadRunning(true);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	class CoffeeThread extends Thread {

		/** The drawable to use as the far background of the animation canvas */
		private Bitmap mBackgroundImageFar;

		/** The drawable to use as the close background of the animation canvas */
		private Bitmap mBackgroundImageNear;

		// right to left scroll tracker for near and far BG
		private int mBGFarMoveX = 0;
		private int mBGNearMoveX = 0;

		/**
		 * 线程状态
		 */
		private boolean mThreadRunning = false;

		public CoffeeThread() {
			Resources mRes = getContext().getResources();
			mBackgroundImageFar = BitmapFactory.decodeResource(mRes,
					R.drawable.background_a);
			mBackgroundImageNear = BitmapFactory.decodeResource(mRes,
					R.drawable.background_b);
		}

		public void setThreadRunning(boolean threadRunning) {
			this.mThreadRunning = threadRunning;
		}

		@Override
		public void run() {
			draw();
			Sprite s = new Sprite(R.drawable.icon);
			while (this.mThreadRunning) {
				for (int i = 0; i < 2; i++) {
					Rect dirty = null;
					dirty = new Rect(s.getX() - 4, s.getY() - 4, s.getX()
							+ s.getWidth(), s.getY() + s.getHeight());
					Canvas canvas = getHolder().lockCanvas(dirty);
					try {

						Bitmap buffer = Bitmap.createBitmap(s.getWidth(),
								s.getHeight(), Bitmap.Config.ARGB_4444);
						buffer.eraseColor(Color.TRANSPARENT);
						canvas.drawBitmap(buffer, s.getX(), s.getY(), new Paint());
						// canvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(),
						// new Paint());
						canvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(),
								new Paint());
						Thread.sleep(2000);
//						this.mThreadRunning = false;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						mHolder.unlockCanvasAndPost(canvas);
					}
				}
				s.setX(s.getX() + 1);
				s.setY(s.getY() + 1);
			}
		}

		public void draw() {
			for (int i = 0; i < 2; i++) {
				Canvas canvas = mHolder.lockCanvas();
				canvas.drawColor(Color.BLACK);
				Resources mRes = getContext().getResources();
				Bitmap mBackgroundImageFar = BitmapFactory.decodeResource(mRes,
						R.drawable.background_a);
				canvas.drawBitmap(mBackgroundImageFar, 0, 0, null);
				mHolder.unlockCanvasAndPost(canvas);
			}
			if (System.currentTimeMillis() != -1) {
				return;
			}

			Canvas canvas = mHolder.lockCanvas();
			try {
				// decrement the far background
				mBGFarMoveX = mBGFarMoveX - 1;

				// decrement the near background
				mBGNearMoveX = mBGNearMoveX - 4;

				// calculate the wrap factor for matching image draw
				int newFarX = mBackgroundImageFar.getWidth() - (-mBGFarMoveX);

				// if we have scrolled all the way, reset to start
				if (newFarX <= 0) {
					mBGFarMoveX = 0;
					// only need one draw
					canvas.drawBitmap(mBackgroundImageFar, mBGFarMoveX, 0, null);

				} else {
					// need to draw original and wrap
					canvas.drawBitmap(mBackgroundImageFar, mBGFarMoveX, 0, null);
					canvas.drawBitmap(mBackgroundImageFar, newFarX, 0, null);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

}
