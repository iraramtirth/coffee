package coffee.frame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Region;

/**
 * 阅读-书页
 * 
 * @author coffee <br>
 *         2014年4月17日上午10:58:19
 */
public class BookPageView extends BaseBookPage {

	private ColorMatrixColorFilter mColorMatrixFilter;
	private Matrix mMatrix;
	private float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	public BookPageView(Context context) {
		super(context);
		//
		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0, 0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (this.mCornerY == this.mHeight / 2) {
			drawCurrentBackArea2(canvas, mCurPageBitmap);
		} else {
			drawCurrentBackArea(canvas, mCurPageBitmap);
		}
	}

	protected void drawCurrentBackArea2(Canvas canvas, Bitmap bitmap) {
		mPath1.reset();
		mPath1.moveTo(mTouch.x, 0);
		mPath1.lineTo(mTouch.x, mHeight);
		float x = mTouch.x + (mWidth - mTouch.x) / 2;
		mPath1.lineTo(x, mHeight);
		mPath1.lineTo(x, 0);
		mPath1.close();
		canvas.save();
		// 献花卷起的部分
		// mPaint.setColor(Color.BLUE);
		// mPaint.setStrokeWidth(1);
		// mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(mPath1, mPaint);
		// 画背景
		canvas.clipPath(mPath1);
		mMatrix.reset();
		// mPaint.setColorFilter(mColorMatrixFilter);
		// 注意 setScale 如果选的坐标不对。可能会导致图片显示不出来
		mMatrix.setScale(-1.0f, 1f, x, 0);
		canvas.drawColor(0xFFAAAAAA);
		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		canvas.restore();
	}

	/** 绘制翻起页背面 */
	protected void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}

		mPath1.reset();
		mPath1.moveTo(mBezierVertex2.x, mBezierVertex2.y);
		mPath1.lineTo(mBezierVertex1.x, mBezierVertex1.y);
		mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath1.close();
		//
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		mPaint.setColorFilter(mColorMatrixFilter);

		float dis = (float) Math.hypot(mCornerX - mBezierControl1.x, mBezierControl2.y - mCornerY);
		float f8 = (mCornerX - mBezierControl1.x) / dis;
		float f9 = (mBezierControl2.y - mCornerY) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
		mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		canvas.rotate(45, mBezierStart1.x, mBezierStart1.y);
		canvas.restore();
	}

}
