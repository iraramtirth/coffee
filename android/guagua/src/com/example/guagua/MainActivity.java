package com.example.guagua;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {

	private int screenWidth = 0;
	private int screenHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		setContentView(new GuaGuaKa(this));
	}

	class GuaGuaKa extends View {
		// 字体大小
		private int TEXT_SIZE = 30;
		// 中奖文本绘制的区域Rect
		private Rect textRect;
		private int STROKE_WIDTH = 20;// 画壁粗细
		private Canvas mCanvas = null;
		private Path mPath = null;
		private Paint mPaint = null;
		private Bitmap backgroudBitmap = null;
		private Bitmap foregroundBitmap = null; 
		public GuaGuaKa(Context context) {
			super(context);
			init(context);
		}

		private void init(Context context) {
			setBackground();
			//paint
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
			//path
			mPath = new Path();
			//back
			foregroundBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
			mCanvas = new Canvas(foregroundBitmap);
			mCanvas.drawColor(Color.GRAY);
		}

		@SuppressWarnings("deprecation")
		private void setBackground() {
			Paint paint = new Paint();
			backgroudBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
			paint.setTextSize(TEXT_SIZE);
			paint.setColor(Color.BLACK);
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);
			paint.setAntiAlias(true);
			//
			String drawText = "谢谢惠顾";
			int left = 100;
			int right = (int) (left + paint.getTextSize() * drawText.length());
			int bottom = 100;
			int top = (int) (bottom + paint.getTextSize());
			Canvas canvas = new Canvas(backgroudBitmap);
			canvas.drawColor(Color.WHITE);
			canvas.drawText(drawText, left, bottom, paint);
			setBackgroundDrawable(new BitmapDrawable(getResources(), backgroudBitmap));
			//
			textRect = new Rect(left, top, right, bottom);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			mCanvas.drawPath(mPath, mPaint);
			canvas.drawBitmap(foregroundBitmap, 0, 0, null);
			
			System.out.println("xxx " + backgroudBitmap.getPixel(110, 110));
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
				break;
			}
			return true;
		}
	}
}
