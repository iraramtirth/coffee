package com.android.game.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Surface01View extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(new SurfaceView01(this));
		
	}

	class SurfaceView01 extends SurfaceView implements SurfaceHolder.Callback {

		final SurfaceHolder holder;

		public SurfaceView01(Context context) {
			super(context);
			holder = this.getHolder();
			// 该方法将调用surfaceCreated,执行创建Surface
			// SurfaceHolder.Callback在底层的Surface状态发生变化的时候通知View
			holder.addCallback(this);
		}

		@Override
		public void surfaceCreated(final SurfaceHolder holder) {
			Canvas c = holder.lockCanvas(null);
			c.drawColor(Color.RED);
			c.drawText("bule hello world_blue", 20, 20, new Paint());
			holder.unlockCanvasAndPost(c);
			
			c = holder.lockCanvas(null);
			c.drawColor(Color.CYAN);
			c.drawText("cyan hello world_cyan", 20, 40, new Paint());
			holder.unlockCanvasAndPost(c);
			new Thread(new MyThread()).start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}

		// 内部类的内部类
		class MyThread implements Runnable {
			int i = 0;
			@Override
			public void run() {
				while(true){
					
					i++;
//				
					Canvas canvas = holder.lockCanvas(new Rect(0, 0, 80, 80));// 获取画布
					//canvas.drawColor(Color.RED);
					//canvas.drawText("hhhhh", 20, 60 , new Paint());
//					Paint mPaint = new Paint();
//					mPaint.setColor(Color.BLUE);
//					//canvas.setBitmap(BitmapFactory.decodeResource(Surface01View.this.getResources(), R.drawable.icon));
//					if(i == 0){
//						canvas.drawRect(new RectF(40, 60, 180, 80), mPaint);					
//					}
				
					holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
					if(i == 1){
						break;
					}
				}
			}
		}
	}

}