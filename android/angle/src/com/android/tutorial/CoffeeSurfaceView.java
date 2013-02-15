package com.android.tutorial;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

		public CoffeeThread()
		{
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
			while (this.mThreadRunning) {
				this.mThreadRunning = false;
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

}
