package coffee.frame.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import coffee.frame.activity.BookPage;
import coffee.utils.log.Log;

/**
 * 阅读-书页<br>
 * 实现了基本上的翻页效果<br>
 * 不带阴影<br>
 * 
 * @author coffee <br>
 *         2014年4月17日上午10:58:19
 */
public abstract class BaseBookPage extends View {
	// 屏幕宽高
	protected int mWidth;
	protected int mHeight;

	/**
	 * 右下角、右上角、左侧、右侧
	 */
	protected int mCornerX = 0; // 拖拽点对应的页脚()
	protected int mCornerY = 0;

	protected Path mPath0;
	protected Path mPath1;

	protected BookPage[] mPages = new BookPage[3];
	protected Bitmap mCurPageBitmap = null; // 当前页
	protected Bitmap mNextPageBitmap = null;// 下一页

	/**
	 * onTouchEvent事件中赋值
	 */
	protected PointF mTemp = new PointF();
	/**
	 * 用于画图 <br>
	 * 一般情况下该值是mTemp的拷贝。但是有些场景需要手动对mTouch赋值。<br>
	 * 为了onTouchEvent事件对mTouch手动赋值造成的影响,所以定义该变量<br>
	 */
	protected PointF mTouch = new PointF(); // 拖拽点
	// 左测 -贝塞尔线(以右下角为拖拽点对应的页脚为例)
	protected PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	protected PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	protected PointF mBezierVertex1 = new PointF(); // 贝塞尔曲线顶点
	protected PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点
	// 右测-贝塞尔线 (以右下角为拖拽点对应的页脚为例)
	protected PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	protected PointF mBezierControl2 = new PointF();
	protected PointF mBezierVertex2 = new PointF();
	protected PointF mBezierEnd2 = new PointF();
	protected Paint mPaint;

	private float mMiddleX;
	private float mMiddleY;
	// touch点与其靠近的翻起角的直线距离
	protected float mTouchToCornerDis;
	private Scroller mScroller;
	/**
	 * 左右划屏的次数。<br>
	 * 如果最终划屏操作--左滑: pageAction > 0; <br>
	 * 如果最终划屏操作--右滑: pageAction < 0; <br>
	 * 如果最终用户取消划屏操作：pageAction == 0;<br>
	 */
	protected int pageAction = 0;

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
	}

	public interface PageCallback {
		/**
		 * 当path0的区域面积大于0的时候调用该方法
		 */
		public void onStart();

		/**
		 * 翻页动作完成后的回调
		 * 
		 * @param action
		 *            1代表翻到下一页, -1代表翻到前一页
		 */
		public void onComplete(int action);
	}

	private PageCallback mPageCallback;

	public void setPage(BookPage prePage, BookPage currentPage, BookPage nextPage) {
		mPages[0] = prePage;
		mPages[1] = currentPage;
		mPages[2] = nextPage;
		Log.d("BookPage--设置Page", mPages[0] + "  ---  " + mPages[1] + "  ---  " + mPages[2]);
	}

	public void setPageCallback(PageCallback callback) {
		this.mPageCallback = callback;
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
		if (x <= mWidth * 1 / 2) {
			mCornerX = 0;
			mCornerY = mHeight / 2;
		} else {
			mCornerX = mWidth;
			if (y <= mHeight * 1 / 3) {
				mCornerY = 0;
			} else if (y <= mHeight * 2 / 3) {
				mCornerY = mHeight / 2;
			} else {
				mCornerY = mHeight;
			}
		}
		Log.d("calcCornerXY", "计算页脚" + mCornerX + " , " + mCornerY);
	}

	/**
	 * 参数是否递归
	 * 
	 * @param recursive
	 */
	private void calcPoints(boolean recursive) {
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
		mBezierStart1.y = mCornerY;

		// 当mBezierStart1.x < 0或者mBezierStart1.x > mWidth时
		// 如果继续翻页，会出现BUG故在此限制
		if (mTouch.x > 0 && mTouch.x < mWidth) {
			float minStart1X = -1.0F * mWidth / 3.6F;
			// minStart1X = 0;
			if (mBezierStart1.x < minStart1X || mBezierStart1.y < 0) {
				if (mBezierStart1.x < minStart1X) {
					mBezierStart1.x = mWidth - mBezierStart1.x - Math.abs(minStart1X);//
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
			// System.out.println("mBezierStart1.x " + mBezierStart1.x);
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
		mBezierVertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBezierVertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBezierVertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBezierVertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;

		// if (Math.abs(mBeziervertex1.x) <= 5 && mBeziervertex1.y == mHeight) {
		// mTemp.x = mTouch.x;
		// mTemp.y = mTouch.y;
		// } else if (mBeziervertex1.x < 0) {
		// mTouch.x = mTemp.x;
		// mTouch.y = mTemp.y;
		// if (recursive) {
		// calcPoints(false);
		// }
		// }

		// if (mBezierStart1.x == 0 && mBezierStart1.y == 1280 || true) {
		// Log.d("bezier", " ---- 1 -----");
		// Log.d("bezier", mBezierStart1);
		// Log.d("bezier", mBezierVertex1);
		// Log.d("bezier", mBezierControl1);
		// Log.d("bezier", mBezierEnd1);
		// Log.d("bezier", " ---- 2 -----");
		// Log.d("bezier", mBezierStart2);
		// Log.d("bezier", mBezierVertex2);
		// Log.d("bezier", mBezierControl2);
		// Log.d("bezier", mBezierEnd2);
		// Log.d("bezier", " ====== ");
		// Log.d("bezier", mTouch);
		// }
	}

	// MotionEvent#ACTION_DOWN 时候的x坐标
	private float mTouchDownX;
	// 上一个Touch点的x坐标
	private float mPreTouchX;
	//
	private boolean isFlipToRight = false;
	private boolean isFlipToLeft = false;

	public boolean onTouchEvent(MotionEvent event) {
		Log.d("view-onTouchEvent", event.getX() + " - " + event.getY());
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mPreTouchX = mTouch.x;
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			// 往右滑动
			if (mTouch.x - mPreTouchX > 0) {
				if (isFlipToRight == false) {
					pageAction--;
				}
				isFlipToRight = true;
				isFlipToLeft = false;
			}
			// 向左滑动
			else if (mPreTouchX - mTouch.x > 0) {
				if (isFlipToLeft == false) {
					pageAction++;
				}
				isFlipToLeft = true;
				isFlipToRight = false;
			}
			Log.d("page-action", isFlipToLeft + ", " + isFlipToLeft);
			if (isFlipToLeft || isFlipToRight) {
				this.postInvalidate();
			}
		}
		// 按下的时候需要初始化数据
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			isFlipToLeft = false;
			isFlipToRight = false;
			mTouchDownX = event.getX();
			calcCornerXY(mTouch.x, mTouch.y);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d("View-touch-up", mTouch.x + "," + mTouchDownX);
			if (Math.abs(mTouch.x - mTouchDownX) - mWidth / 10 > 0) {
				startScroll(1000);
			} else {
				reset();
			}
			// if (isFlipToLeft || isFlipToRight) {
			this.postInvalidate();
			// }
		}
		Log.d("pageAction", pageAction);
		return true;
	}

	/**
	 * 最后一下的滑动方向
	 * 
	 * @param duration
	 * @param lastDirection
	 */
	private void startScroll(int duration) {
		// dx 水平方向滑动的距离，负值会使滚动向左滚动 dy 垂直方向滑动的距离，负值会使滚动向上滚动
		int dx = 0, dy = 0;
		if (isFlipToLeft) {
			dx = -(int) (mWidth + mTouch.x);
			// 右下角翻页
			if (mCornerY != 0) {
				dy = (int) (mHeight - mTouch.y);
			} else {// 右上角翻页
				dy = (int) (1 - mTouch.y); // 防止mTouch.y最终变为0
			}
		} else if (isFlipToRight) {
			dx = (int) (mWidth - mTouch.x);
		}
		Log.d("animation", isFlipToLeft + "," + isFlipToRight + " " + dx + "," + dy);
		int startX = (int) mTouch.x;
		int startY = (int) mTouch.y;
		mScroller.startScroll(startX, startY, dx, dy, duration);
	}

	public void stopScroll() {
		if (mScroller != null) {
			Log.d("view-stopScroll", "停止");
			mScroller.abortAnimation();
			if (this.mPageCallback != null && pageAction != 0) {
				this.mPageCallback.onComplete(pageAction > 0 ? 1 : -1);
				reset();// 注意需要先reset然后再post
				postInvalidate();
			}
		}
	}

	private void reset() {
		Log.d("view", "------reset-------");
		mTouch.x = mWidth;
		mTouch.y = mHeight;
		mCornerX = mWidth;
		mCornerY = mHeight;
		//
		isFlipToLeft = false;
		isFlipToRight = false;
		pageAction = 0;
		//
		mPath0.reset();
		mPath0.moveTo(0, 0);
		mPath0.lineTo(0, 0);
		mPath0.close();
		mPath1.reset();
		mPath1.moveTo(0, 0);
		mPath1.lineTo(0, 0);
		mPath1.close();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			mTouch.x = mScroller.getCurrX();
			mTouch.y = mScroller.getCurrY();
			// 不能让
			if (mWidth - Math.abs(mTouch.x) < 5) {
				stopScroll();// 该方法中有postInvalidate操作
			} else {
				postInvalidate();
			}
		}
	}

	@Override
	public void postInvalidate() {
		super.postInvalidate();
		Log.d("postInvalidate", "准备刷屏。。");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("view-onDraw", "绘屏");
		// 右滑--查看前一页
		if (mCornerX == 0 && mCornerY == mHeight / 2 && (isFlipToLeft || isFlipToRight)) {
			mCurPageBitmap = mPages[0].getPageBitmap();
			mNextPageBitmap = mPages[1].getPageBitmap();
			Log.d("BookPage-right", mPages[0] + " , " + mPages[1] + " -- " + mPages[2]);
		} else {
			if (pageAction == 0 && isFlipToLeft == false && isFlipToRight == false) {
				mCornerX = mWidth;
				mCornerY = mHeight;
				mTouch.x = mWidth;
				mTouch.y = mHeight;
			}
			mCurPageBitmap = mPages[1].getPageBitmap();
			mNextPageBitmap = mPages[2].getPageBitmap();
			Log.d("BookPage-left", mPages[0] + " ---  " + mPages[1] + " , " + mPages[2]);
		}
		canvas.drawColor(0xFFAAAAAA);
		if (this.mPageCallback != null) {
			int len0 = getPathLenght(mPath0);
			int len1 = getPathLenght(mPath1);
			if (len0 > 0 && len1 > 0) {
				this.mPageCallback.onStart();
			}
			Log.d("path_measure", len0 + " , " + len1);
		}
		// 从屏幕的中间位置往左右划
		if (this.mCornerY == this.mHeight / 2) {
			calcPoints(true);
			drawCurrentPageArea2(canvas, mCurPageBitmap);
			drawNextPageAreaAndShadow2(canvas, mNextPageBitmap);
			drawCurrentBackArea2(canvas, mCurPageBitmap);// 画背景
		} else {
			calcPoints(true);
			drawCurrentPageArea(canvas, mCurPageBitmap);
			drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
			drawCurrentBackArea(canvas, mCurPageBitmap);// 画背景
		}
	}

	private void drawCurrentPageArea2(Canvas canvas, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		mPath0.reset();
		mPath0.moveTo(mTouch.x, 0);
		mPath0.lineTo(mTouch.x, mHeight);
		mPath0.lineTo(mWidth, mHeight);
		mPath0.lineTo(mWidth, 0);
		mPath0.close();
		//
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(1);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);//
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawPath(mPath0, mPaint);
		canvas.restore();
		//
		PathMeasure pm = new PathMeasure(mPath0, false);
		Log.d("PathMeasure-0", pm.getLength());
	}

	private void drawNextPageAreaAndShadow2(Canvas canvas, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		mPath1.reset();
		mPath1.moveTo(mTouch.x, 0);
		mPath1.lineTo(mTouch.x, mHeight);
		mPath1.lineTo(mWidth, mHeight);
		mPath1.lineTo(mWidth, 0);
		mPath1.close();
		//
		mPaint.setColor(Color.GRAY);
		canvas.save();
		canvas.clipPath(mPath1);
		canvas.drawPath(mPath1, mPaint);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.restore();
		//
		PathMeasure pm = new PathMeasure(mPath0, false);
		Log.d("PathMeasure-1", pm.getLength());
	}

	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		mPath0.reset();
		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y);
		mPath0.lineTo(mCornerX, mCornerY);
		mPath0.close();
		//
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(1);
		mPaint.setTextSize(40);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		// canvas.drawText("可以叫我疯子，不能叫我傻子。", 0, 100, mPaint);

		PathMeasure pm = new PathMeasure(mPath0, false);
		Log.d("PathMeasure-0", pm.getLength());
		canvas.restore();
	}

	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		mPath1.reset();
		mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.lineTo(mBezierVertex1.x, mBezierVertex1.y);
		mPath1.lineTo(mBezierVertex2.x, mBezierVertex2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.lineTo(mCornerX, mCornerY);
		mPath1.close();

		// mPaint.setColor(Color.GRAY);
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);// 相交的部分
		canvas.drawPath(mPath1, mPaint);
		if (mNextPageBitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		PathMeasure pm = new PathMeasure(mPath1, false);
		Log.d("PathMeasure-1", pm.getLength());
		canvas.restore();
	}

	/**
	 * 画出当前页翻起的部分的背景文字
	 */
	protected abstract void drawCurrentBackArea(Canvas canvas, Bitmap bitmap);

	/**
	 * 画出-上一页-翻起的部分的背景文字
	 */
	protected abstract void drawCurrentBackArea2(Canvas canvas, Bitmap bitmap);

	public int getPathLenght(Path path) {
		PathMeasure pm = new PathMeasure(path, false);
		try {
			int len = (int) pm.getLength();
			return len;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 拖拽点对应的页脚的位置<br>
	 * 有1、2、3、4四个值
	 * 
	 * @return 1,2,3,4 1:左侧中间。2右上角。3右侧中间。4右下角
	 */
	public int getCornerPosition(float x, float y) {
		calcCornerXY(x, y);
		if (mCornerX == 0 && mCornerY == mHeight / 2) {
			return 1;
		} else if (mCornerX == mWidth) {
			if (mCornerY == 0) {
				return 2;
			} else if (mCornerY == mHeight / 2) {
				return 3;
			} else if (mCornerY == mHeight) {
				return 4;
			}
		}
		return 4;
	}
}
