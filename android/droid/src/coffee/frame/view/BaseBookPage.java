package coffee.frame.view;

import org.coffee.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PointF;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * 阅读-书页<br>
 * 实现了基本上的翻页效果<br>
 * 不带阴影<br>
 * 
 * @author coffee <br>
 *         2014年4月17日上午10:58:19
 */
public class BaseBookPage extends View {
	//屏幕宽高
	private int mWidth;
	private int mHeight;
	
	protected int mCornerX = 0; // 拖拽点对应的页脚
	protected int mCornerY = 0;

	protected Path mPath0;
	protected Path mPath1;
	protected Bitmap mCurPageBitmap = null; // 当前页
	protected Bitmap mNextPageBitmap = null;

	protected PointF mTouch = new PointF(); // 拖拽点
	// 左测 -贝塞尔线(以右下角为拖拽点对应的页脚为例)
	protected PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	protected PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	protected PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
	protected PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点
	// 右测-贝塞尔线 (以右下角为拖拽点对应的页脚为例)
	protected PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	protected PointF mBezierControl2 = new PointF();
	protected PointF mBeziervertex2 = new PointF();
	protected PointF mBezierEnd2 = new PointF();
	protected Paint mPaint;

	private float mMiddleX;
	private float mMiddleY;
	//touch点与其靠近的翻起角的直线距离
	protected float mTouchToCornerDis;
	private Scroller mScroller;

	public BaseBookPage(Context context) {
		super(context);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		this.mWidth = displayMetrics.widthPixels; // Pager 宽和高
		this.mHeight = displayMetrics.heightPixels;

		mPath0 = new Path();
		mPath1 = new Path();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		mTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
		mTouch.y = 0.01f;

		mScroller = new Scroller(context);
		mCurPageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_1);
		mNextPageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.book_2);
	}

	/** 求解直线P1P2和直线P3P4的交点坐标 */
	public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
		PointF CrossP = new PointF();
		// 二元函数通式： y=ax+b
		float a1 = (P2.y - P1.y) / (P2.x - P1.x);
		float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}

	public void calcCornerXY(float x, float y) {
		if (x <= mWidth / 2) {
			mCornerX = 0;
		} else {
			mCornerX = mWidth;
		}
		if (y <= mHeight / 2) {
			mCornerY = 0;
		} else {
			mCornerY = mHeight;
		}
		// mCornerX = (int) x;
		// mCornerY = (int) y;
		// if ((mCornerX == 0 && mCornerY == mHeight) || (mCornerX == mWidth &&
		// mCornerY == 0))
		// mIsRTandLB = true;
		// else
		// mIsRTandLB = false;
	}

	private void calcPoints() {
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
		mBezierStart1.y = mCornerY;

		// 当mBezierStart1.x < 0或者mBezierStart1.x > 480时
		// 如果继续翻页，会出现BUG故在此限制
		if (mTouch.x > 0 && mTouch.x < mWidth) {
			if (mBezierStart1.x < 0 || mBezierStart1.x > mWidth) {
				if (mBezierStart1.x < 0){
					mBezierStart1.x = mWidth - mBezierStart1.x;
				}

				float f1 = Math.abs(mCornerX - mTouch.x);
				float f2 = mWidth * f1 / mBezierStart1.x;
				mTouch.x = Math.abs(mCornerX - f2);

				float f3 = Math.abs(mCornerX - mTouch.x) * Math.abs(mCornerY - mTouch.y) / f1;
				mTouch.y = Math.abs(mCornerY - f3);

				mMiddleX = (mTouch.x + mCornerX) / 2;
				mMiddleY = (mTouch.y + mCornerY) / 2;

				mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				mBezierControl1.y = mCornerY;

				mBezierControl2.x = mCornerX;
				mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
				mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
			}
		}
		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y) / 2;

		mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX), (mTouch.y - mCornerY));

		mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1, mBezierStart2);
		mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1, mBezierStart2);

		/*
		 * mBeziervertex1.x 推导
		 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
		 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
		 */
		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			this.postInvalidate();

		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			calcCornerXY(mTouch.x, mTouch.y);
			// this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// if (canDragOver()) {
			startAnimation(1200);
			// } else {
			// mTouch.x = mCornerX - 0.09f;
			// mTouch.y = mCornerY - 0.09f;
			// }
			this.postInvalidate();
		}
		// return super.onTouchEvent(event);
		return true;
	}

	private void startAnimation(int duration) {
		int dx, dy; // dx 水平方向滑动的距离，负值会使滚动向左滚动 dy 垂直方向滑动的距离，负值会使滚动向上滚动
		if (mCornerX > 0) {
			dx = -(int) (mWidth + mTouch.x);
		} else {
			dx = (int) (mWidth - mTouch.x + mWidth);
		}
		if (mCornerY > 0) {
			dy = (int) (mHeight - mTouch.y);
		} else {
			dy = (int) (1 - mTouch.y); // 防止mTouch.y最终变为0
		}
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy, duration);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y;
			postInvalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFAAAAAA);
		calcPoints();
		drawCurrentPageArea(canvas);
		drawNextPageAreaAndShadow(canvas);
	}

	private void drawCurrentPageArea(Canvas canvas) {
		mPath0.reset();
		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y);
		mPath0.lineTo(mCornerX, mCornerY);
		mPath0.close();
		mPath0.setFillType(FillType.WINDING);
		//
		mPaint.setColor(Color.BLUE);
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
		canvas.drawPath(mPath0, mPaint);
		canvas.restore();
	}

	private void drawNextPageAreaAndShadow(Canvas canvas) {
		mPath1.reset();
		mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.lineTo(mCornerX, mCornerY);
		mPath1.close();
		// mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x -
		// mCornerX, mBezierControl2.y - mCornerY));
		// int leftx;
		// int rightx;
		// GradientDrawable mBackShadowDrawable;
		// if (mIsRTandLB) {
		// leftx = (int) (mBezierStart1.x);
		// rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
		// mBackShadowDrawable = mBackShadowDrawableLR;
		// } else {
		// leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
		// rightx = (int) mBezierStart1.x;
		// mBackShadowDrawable = mBackShadowDrawableRL;
		// }
		mPaint.setColor(Color.GRAY);
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawPath(mPath1, mPaint);
		canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
		// canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		// mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
		// (int) (mMaxLength + mBezierStart1.y));
		// mBackShadowDrawable.draw(canvas);
		canvas.restore();
	}

}
