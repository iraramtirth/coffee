package com.example.guagua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ShaveView extends View {
	private Bitmap foregroundBitmap;
	private int width, height;
	// 字体大小
	private int TEXT_SIZE;
	private String drawText;
	// 中奖文本绘制的区域Rect
	private Rect drawRect;
	private int STROKE_WIDTH = 20;// 画壁粗细
	private Canvas mCanvas = null;
	private Path mPath = null;
	private Paint mPaint = null;
	private Bitmap backgroudBitmap = null;

	private AreaThread areaThread;

	public ShaveView(Context context) {
		super(context);
		init();
	}

	@SuppressLint("NewApi")
	public ShaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 关闭硬件加速。否则会报OpenGLRenderer Message：Cannot generate texture from bitmap
		// setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		drawText = "我中奖了";
		TEXT_SIZE = 40;
		// measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getHeight() > 0 && mPaint == null) {
			init();
		}
	}

	// 像素点-存放像素值。
	private short[][] pixel;
	private int totalPoint;// 全部的点
	private int openPoint;// 已经刮开的点的总数
	private final int space = 1;// 间隔两个点

	private void init() {
		this.width = getMeasuredWidth();
		this.height = getMeasuredHeight();
		//
		setBackground();
		// paint
		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(STROKE_WIDTH);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeJoin(Join.ROUND);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		mPaint.setAlpha(0);
		// path
		mPath = new Path();
		// back
		foregroundBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		mCanvas = new Canvas(foregroundBitmap);
		mCanvas.drawColor(Color.GRAY);
	}

	@SuppressWarnings("deprecation")
	private void setBackground() {
		Paint paint = new Paint();
		backgroudBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		paint.setTextSize(TEXT_SIZE);
		paint.setColor(Color.BLACK);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		//
		Canvas canvas = new Canvas(backgroudBitmap);
		canvas.drawColor(Color.WHITE);
		//
		int centerX = (canvas.getWidth() / 2);
		int centerY = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
		// ((textPaint.descent() + textPaint.ascent()) / 2) is the distance from
		// the baseline to the center.
		canvas.drawText(drawText, centerX, centerY, paint);
		setBackgroundDrawable(new BitmapDrawable(getResources(), backgroudBitmap));
		// 计算Draw区域
		int drawTextWidth = (int) (paint.getTextSize() * drawText.length());
		int drawTextHeight = (int) paint.getTextSize();
		int left = centerX - drawTextWidth / 2;
		int right = centerX + drawTextWidth / 2;
		int bottom = centerY + drawTextHeight / 2;
		int top = centerY - drawTextHeight / 2;
		drawRect = new Rect(left, top, right, bottom);
		//
		int xLen = drawRect.right - drawRect.left;
		int yLen = drawRect.bottom - drawRect.top;
		pixel = new short[xLen][yLen];
		totalPoint = xLen * yLen;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mCanvas.drawPath(mPath, mPaint);
		canvas.drawBitmap(foregroundBitmap, 0, 0, null);

		openPoint = 0;// Rect(248, 308 - 408, 348)
		for (int x = 0; x < pixel.length; x += space) {
			for (int y = 0; y < pixel[x].length; y += space) {
				// 已经刮开
				if (foregroundBitmap.getPixel(drawRect.left + x, drawRect.top + y) == 0) {
					openPoint++;
				} else {
					// System.out.println((rect.left + x) + "," +
					// (rect.top + y));
				}
			}

		}// Rect(100, 70 - 220, 100)
		Log.d("open", openPoint / (float) totalPoint + "");
	}

	private int x = 0;
	private int y = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int currX = (int) event.getX();
		int currY = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mPath.reset();
			x = currX;
			y = currY;
			mPath.moveTo(x, y);
			// if (areaThread == null && isRunning) {
			// areaThread = new AreaThread();
			// areaThread.start();
			// } else {
			// areaThread.resumeCalc();
			// }
			break;
		case MotionEvent.ACTION_MOVE:
			mPath.quadTo(x, y, currX, currY);
			x = currX;
			y = currY;
			postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mPath.reset();
			if (areaThread != null) {
				areaThread.suspendCalc();
			}
			break;
		}
		return true;
	}

	/**
	 * 标志 . 是否开启Area线程
	 */
	private boolean isRunning = false;

	public void startAreaThread() {
		isRunning = true;
	}

	public void stopAreaThread() {
		if (areaThread != null) {
			areaThread.stopCalc();
			areaThread = null;
		}
	}

	/**
	 * 计算刮开的面积百分比
	 */
	private class AreaThread extends Thread {
		private final String TAG = "AreaThread";
		// 是否挂起线程
		private boolean isSuspend = false;
		// 像素点-存放像素值。
		private short[][] pixel;
		private int totalPoint;// 全部的点
		private int openPoint;// 已经刮开的点的总数
		private final int space = 1;// 间隔两个点

		public AreaThread() {
			int xLen = drawRect.right - drawRect.left;
			int yLen = drawRect.bottom - drawRect.top;
			pixel = new short[xLen][yLen];
			totalPoint = xLen * yLen;
			// pixelSet = new HashSet<short[][]>();
		}

		@Override
		public void run() {
			while (isRunning) {
				openPoint = 0;// Rect(248, 308 - 408, 348)
				for (int x = 0; x < pixel.length; x += space) {
					for (int y = 0; y < pixel[x].length; y += space) {
						// 已经刮开
						if (foregroundBitmap.getPixel(drawRect.left + x, drawRect.top + y) == 0) {
							openPoint++;
						} else {
							// System.out.println((rect.left + x) + "," +
							// (rect.top + y));
						}
					}
					if (isSuspend) {
						try {
							synchronized (this) {
								Log.d(TAG, "wait");
								wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}// Rect(100, 70 - 220, 100)
				Log.d("open", openPoint / (float) totalPoint + "");
			}
		}

		@Override
		public synchronized void start() {
			super.start();
			isRunning = true;
			Log.d(TAG, "start");
		}

		public void resumeCalc() {
			this.isSuspend = false;
			synchronized (this) {
				notify();
			}
			Log.d(TAG, "resumeCalc");
		}

		public void suspendCalc() {
			this.isSuspend = true;
			Log.d(TAG, "suspendCalc");
		}

		public void stopCalc() {
			isRunning = false;
			Log.d(TAG, "stopCalc");
		}
	}

}
