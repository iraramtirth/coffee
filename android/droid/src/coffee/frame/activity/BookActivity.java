package coffee.frame.activity;

import org.coffee.util.framework.Alert;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import coffee.frame.activity.base.BaseActivity;
import coffee.frame.view.BaseBookPage.PageCallback;
import coffee.frame.view.BookPageView;
import coffee.utils.log.Log;

public class BookActivity extends BaseActivity {

	/**
	 * 下标从1开始
	 */
	private int currentPage = 1;

	private BookPage[] pages;
	private BookPageView mPageView;

	// private boolean isScroll = false;

	private BookPageFactory bookPageFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		dm = this.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		System.out.println("screenWidth = " + screenWidth + "; screenHeight = " + screenHeight);
		//
		BookConfig bookConfig = new BookConfig(this);
		bookPageFactory = new BookPageFactory("金庸.txt", bookConfig);
		//
		mPageView = new BookPageView(this);
		pages = new BookPage[3];
		pages[0] = init(screenWidth, screenHeight, 0);
		pages[1] = init(screenWidth, screenHeight, 1);
		pages[2] = init(screenWidth, screenHeight, 2);
		//
		// bookPageFactory.updatePage(pages[0].getPageBitmap(), true);
		bookPageFactory.updatePage(pages[1], 0, true);
		bookPageFactory.updatePage(pages[2], pages[1].getOffsetLast(), true);
		//
		mPageView.setPage(pages[0], pages[1], pages[2]);
		mPageView.setPageCallback(new PageCallback() {
			@Override
			public void onStart(int lenPath0, int lenPath1) {
				Log.d("activity-page-onStart", "true" + " " + lenPath0 + "," + lenPath1);
				if (lenPath0 > 0 && lenPath1 > 0) {
					// isScroll = true;
				}
			}

			@Override
			public void onStop() {
				Log.d("activity-page-onStop", "false");
				// isScroll = false;
			}

			@Override
			public void onComplete(int action) {
				if (action == 0) {
					return;
				}
				currentPage += action;
				if (currentPage == 0) {
					currentPage = 1;
				}
				if (action > 0) { // 下一页
					if (pages[2].getOffsetFirst() >= bookPageFactory.getMaxOffset()) {
						// 图书已结束
					} else {
						bookPageFactory.updatePage(pages[0], pages[2].getOffsetLast(), true);
						mPageView.setPage(pages[1], pages[2], pages[0]);
						BookPage tmp = pages[0];
						pages[0] = pages[1];
						pages[1] = pages[2];
						pages[2] = tmp;
					}
				} else {// 查看上一页
					if (pages[0].getOffsetLast() <= 0) {
						// 当前是首页
					} else {
						bookPageFactory.updatePage(pages[2], pages[0].getOffsetFirst(), false);
						mPageView.setPage(pages[2], pages[0], pages[1]);
						BookPage tmp = pages[0];
						pages[0] = pages[2];
						pages[2] = pages[1];
						pages[1] = tmp;
					}
				}
			}
		});
		setContentView(mPageView);
		/**
		 * if (isFlipToLeft) { if (mNextPageBitmap == null) {
		 * Alert.toast("到头了..."); return false; } else { this.postInvalidate();
		 * } } if (isFlipToRight) { if (mPrePageBitmap == null) {
		 * Alert.toast("没有上一页了"); return false; } else { this.postInvalidate();
		 * } }
		 */
	}

	private float touchDownX;
	private float touchDownY;
	private float touchMoveX;
	/**
	 * 是否提示当前页是第一页或者当前是最后一页。<br>
	 * 当该值为false的提示
	 */
	private boolean showTip = false;

	/**
	 * 
	 * @return false 拦截MotionEvent事件。 <br>
	 *         true 会把MotionEvent事件继续分发给BaseBookPage#onTouchEvent, 否则将收不到
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d("activity-event", "----------------------------------------开始划屏");
			mPageView.stopScroll();
			// isScroll = false;
			showTip = false;
			touchDownX = event.getX();
			touchDownY = event.getY();
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			touchMoveX = event.getX();
			if (touchMoveX - touchDownX > 0) {
				int cornerPosition = mPageView.getCornerPosition(touchDownX, touchDownY, true);
				Log.d("activity-measure------", touchDownX + "," + touchDownY + "," + (touchMoveX - touchDownX) + " , currentPage " + currentPage + " , " + cornerPosition);
				// Log.d("dispatch-pm", mPageView.getPath0Lenght());
				// int cornerPosition = mPageView.getCornerPosition(touchDownX,
				// touchDownY, true);
				// Log.d("activity-cornerXY", "拖拽点 " + cornerPosition);
				// isScroll==false 滚动停止以后才判断. 防止翻页后取消之前的操作
				if (isFirstPage() && cornerPosition == 1) {
					if (showTip == false) {
						showTip = true;
						touchDownX = 0;// 注意, 如果是首页往右滑动 以后再往左侧滑动。会出问题
						// mPageView.setTouchXY(0,0);
						Alert.toast("已经是第一页了");
					}
					Log.d("activity-move", "--拦截--");
					return false;
				} else {
					// isScroll = true;
					Log.d("activity-move--向右", "传递move");
				}
			} else if (touchMoveX - touchDownX < 0) {
				int cornerPosition = mPageView.getCornerPosition(touchDownX, touchDownY, false);
				Log.d("activity-measure------", touchDownX + "," + touchDownY + "," + (touchMoveX - touchDownX) + " , currentPage " + currentPage + " , " + cornerPosition);
				if (isLastPage() && cornerPosition != 1) {
					if (showTip == false) {
						showTip = true;
						touchDownX = mPageView.getPageWidth();
						Alert.toast("结尾了");
					}
					return false;
				} else {
					// isScroll = true;
					Log.d("activity-move---向左", "传递move");
				}
			} else {
				Log.d("activity-move", "传递move--------");
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			touchMoveX = 0;
			if (touchMoveX - touchDownX > 0) {
				if (isFirstPage()) {
					return false;
				}
			}
		}
		return super.dispatchTouchEvent(event);
	}

	// private BookPage createPage(int pageIndex) {
	// int res = getResources().getIdentifier("page_" + pageIndex, "drawable",
	// this.getPackageName());
	// Bitmap pageBitmap = BitmapFactory.decodeResource(context.getResources(),
	// res);
	// BookPage page = new BookPage(pageIndex, pageBitmap);
	// return page;
	// }

	private BookPage init(int screenWidth, int screenHeight, int pageIndex) {
		Bitmap pageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		BookPage page = new BookPage(pageIndex, pageBitmap);
		return page;
	}

	/**
	 * 当前页面是否是首页
	 * 
	 * @return
	 */
	private boolean isFirstPage() {
		// 此时pages数组的排列形式为
		// [0, 0] [0, 165] [165, 330]
		return pages[0].getOffsetFirst() == 0 && pages[0].getOffsetLast() == 0;
	}

	private boolean isLastPage() {
		// 此时pages数组的排列形式为
		// [330, 495] [496, 700] [700, 700]
		return (pages[2].getOffsetFirst() == pages[2].getOffsetLast()) && (pages[2].getOffsetFirst() == pages[1].getOffsetLast());
	}
}
