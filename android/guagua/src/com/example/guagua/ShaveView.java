package com.example.guagua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
	private int width, height;
	// 字体大小
	private int TEXT_SIZE = 40;
	private String drawText;
	// 中奖文本绘制的区域Rect
	private Rect drawRect;
	private int STROKE_WIDTH = 20;// 画笔粗细
	private Path foregroundPath = null;
	private Paint backgroudPaint, foregroundPaint;
	private Canvas backgroundCanvas, foregroundCanvas;
	private Bitmap backgroundBitmap, foregroundBitmap;

	// 像素点-存放像素值。
	private short[][] pixel;
	private int totalPoint;// 全部的点
	private int openPoint;// 已经刮开的点的总数
	private final int space = 1;// 间隔两个点

	public ShaveView(Context context) {
		super(context);
		init();
	}

	@SuppressLint("NewApi")
	public ShaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		int[] attrsArray = new int[] { android.R.attr.id, // 0
				android.R.attr.background, // 1
				android.R.attr.layout_width, // 2
				android.R.attr.layout_height, // 3
				android.R.attr.text, // 4
				android.R.attr.textSize };
		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
		// 关闭硬件加速。否则会报OpenGLRenderer Message：Cannot generate texture from bitmap
		// setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		drawText = ta.getString(4);
		TEXT_SIZE = ta.getIndex(5);
		ta.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getHeight() > 0 && foregroundBitmap == null) {
			init();
		}
	}

	private void init() {
		this.width = getWidth();
		this.height = getHeight();
		// 背景图
		backgroundBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		backgroundCanvas = new Canvas(backgroundBitmap);
		// 背景Paint
		backgroudPaint = new Paint();
		backgroudPaint.setTextSize(TEXT_SIZE);
		backgroudPaint.setColor(Color.BLACK);
		backgroudPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		backgroudPaint.setTextAlign(Align.CENTER);
		backgroudPaint.setAntiAlias(true);
		// 前景图
		foregroundBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		foregroundCanvas = new Canvas(foregroundBitmap);
		// 前景Paint
		foregroundPaint = new Paint();
		foregroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		foregroundPaint.setAntiAlias(true);
		foregroundPaint.setDither(true);
		foregroundPaint.setStyle(Style.STROKE);
		foregroundPaint.setStrokeWidth(STROKE_WIDTH);
		foregroundPaint.setStrokeCap(Cap.ROUND);
		foregroundPaint.setStrokeJoin(Join.ROUND);
		foregroundPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		foregroundPaint.setAlpha(0);
		// path
		foregroundPath = new Path();
		onReset(drawText);
	}

	@SuppressWarnings("deprecation")
	private void drawBackground() {
		backgroundCanvas.drawColor(Color.WHITE);
		int centerX = (backgroundCanvas.getWidth() / 2);
		int centerY = (int) ((backgroundCanvas.getHeight() / 2) - ((backgroudPaint.descent() + backgroudPaint.ascent()) / 2));
		// ((textPaint.descent() + textPaint.ascent()) / 2) is the distance from
		// the baseline to the center.
		backgroundCanvas.drawText(drawText, centerX, centerY, backgroudPaint);
		setBackgroundDrawable(new BitmapDrawable(getResources(), backgroundBitmap));
		// 计算Draw区域
		drawRect = new Rect();
		// Rect(3, -33 - 226, 1)
		Rect bounds = new Rect();
		backgroudPaint.getTextBounds(drawText, 0, drawText.length(), bounds);
		int drawTextWidth =  bounds.width(); //(int) backgroudPaint.measureText(drawText);
		int drawTextHeight =  bounds.height();
		int left = centerX - drawTextWidth / 2;
		int right = centerX + drawTextWidth / 2;
		int bottom = centerY + drawTextHeight / 2;
		int top = centerY - drawTextHeight / 2;
		drawRect = new Rect(left, top, right, bottom); 
		//
		pixel = new short[drawRect.width()][drawRect.height()];
		totalPoint = drawRect.width() * drawRect.height();
	}

	private boolean isShaveOpen = false;

	/**
	 * 刮开-回调
	 */
	private void onShaveOpen() {
		isShaveOpen = true;
		if (shaveOpenCallback != null) {
			shaveOpenCallback.callback();
		}
	}

	public void onReset(String drawText) {
		this.drawText = drawText;
		drawBackground();
		foregroundCanvas.drawColor(Color.GRAY);
		isShaveOpen = false;
		// postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (foregroundCanvas == null) {
			super.onDraw(canvas);
			return;
		}
		foregroundCanvas.drawPath(foregroundPath, foregroundPaint);
		canvas.drawBitmap(foregroundBitmap, 0, 0, null);
		if (isShaveOpen == false) {
			openPoint = 0;// Rect(248, 308 - 408, 348)
			for (int x = 0; x < pixel.length; x += space) {
				for (int y = 0; y < pixel[x].length; y += space) {
					// 已经刮开
					if (foregroundBitmap.getPixel(drawRect.left + x, drawRect.top + y) == 0) {
						openPoint++;
					} else {
						// System.out.println((drawRect.left + x) + "," +
						// (drawRect.top + y));
					}
				}
			}
			float percent = openPoint / (float) totalPoint;
			Log.d("open", percent + "");
			if (percent > 0.8) {
				onShaveOpen();
			}
		}
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
			foregroundPath.reset();
			x = currX;
			y = currY;
			foregroundPath.moveTo(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			foregroundPath.quadTo(x, y, currX, currY);
			x = currX;
			y = currY;
			postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			foregroundPath.reset();
			break;
		}
		return true;
	}

	private SheveOpenCallback shaveOpenCallback;

	public static interface SheveOpenCallback {
		public void callback();
	}

	public void setShaveOpenCallback(SheveOpenCallback shaveOpenCallback) {
		this.shaveOpenCallback = shaveOpenCallback;
	}

}
