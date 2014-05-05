package coffee.frame.book.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import coffee.utils.log.Log;

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
		//
		this.createDrawable();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	/**
	 * 
	 * 绘制翻起页背面
	 */
	@Override
	protected void drawCurrentBackArea2(Canvas canvas, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		// x2是左侧边线。 x1是右侧轴线。
		float x1, x2;//
		if (mCornerX == 0) {
			x1 = mTouch.x;
			x2 = 2 * x1 - mWidth;
			mPath1.reset();
			mPath1.moveTo(x1, 0);
			mPath1.lineTo(x1, mHeight);
			mPath1.lineTo(x2, mHeight);
			mPath1.lineTo(x2, 0);
			Log.d("draw-touch", mTouch.x + " , " + mTouchDownX + " " + mCornerX);
			Log.d("draw-back2", (int) x1 + " " + (int) x2);

		} else {

			x1 = mWidth - (mTouchDownX - mTouch.x) / 2;
			if (x1 > mWidth) {
				x1 = mWidth;
			}
			mPath1.reset();
			mPath1.moveTo(x1, 0);
			mPath1.lineTo(x1, mHeight);
			// 其中 x1是x2和mWidth的中点坐标.数学公式 2x1 = x2 + width
			x2 = 2 * x1 - mWidth;
			mPath1.lineTo(x2, mHeight);
			mPath1.lineTo(x2, 0);
			mPath1.close();
			canvas.save();

		}
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
		mMatrix.setScale(-1.0f, 1f, x1, 0);
		canvas.drawColor(0xFFAAAAAA);
		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		{// 画阴影
			mBackShadowDrawableRL.setBounds((int) x2, 0, (int) x1 + 10, mHeight);
			mBackShadowDrawableRL.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 * 绘制翻起页背面
	 **/
	@Override
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

	public int getPageWidth() {
		return super.mWidth;
	}

	int[] mBackShadowColors;
	int[] mFrontShadowColors;
	GradientDrawable mBackShadowDrawableLR;
	GradientDrawable mBackShadowDrawableRL;
	GradientDrawable mFolderShadowDrawableLR;
	GradientDrawable mFolderShadowDrawableRL;

	GradientDrawable mFrontShadowDrawableHBT;
	GradientDrawable mFrontShadowDrawableHTB;
	GradientDrawable mFrontShadowDrawableVLR;
	GradientDrawable mFrontShadowDrawableVRL;

	/** 创建阴影的GradientDrawable */
	private void createDrawable() {
		int[] color = { 0x333333, 0xb0333333 };
		mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0x66222222, 0x222222 };

//		mBackShadowColors = new int[] { 0xF0AAAAAA, 0x10AAAAAA };
		mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mFrontShadowDrawableVRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}
}
