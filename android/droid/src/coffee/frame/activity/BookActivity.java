package coffee.frame.activity;

import org.coffee.util.framework.Alert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	private boolean isScroll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageView = new BookPageView(this);
		pages = new BookPage[3];
		pages[0] = createPage(1);
		pages[1] = createPage(1);// current page
		pages[2] = createPage(2); //
		//
		mPageView.setPage(pages[0], pages[1], pages[2]);
		mPageView.setPageCallback(new PageCallback() {
			@Override
			public void onStart() {
				Log.d("page-Callback", "true");
				isScroll = true;
			}

			@Override
			public void onStop() {
				Log.d("page-Callback", "false");
				isScroll = false;
			}

			@Override
			public void onComplete(int action) {
				currentPage += action;
				if (currentPage == 0) {
					currentPage = 1;
					return;
				}
				// 翻到下一页
				if (action > 0) {
					pages[0] = pages[1];
					pages[1] = pages[2];
					pages[2] = createPage(currentPage + 1);
				} else {
					pages[2] = pages[1];
					pages[1] = pages[0];
					pages[0] = createPage(currentPage - 1);
				}
				mPageView.setPage(pages[0], pages[1], pages[2]);
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
		Log.d("activity-dispatchTouch-", event.getX() + " - " + event.getY());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d("event-", "----------------------------------------开始划屏");
			mPageView.stopScroll();
			showTip = false;
			touchDownX = event.getX();
			touchDownY = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			touchMoveX = event.getX();
			if (touchMoveX - touchDownX > 0) {
				// Log.d("dispatch-pm", mPageView.getPath0Lenght());
				int cornerPosition = mPageView.getCornerPosition(touchDownX, touchDownY);
				Log.d("activity-cornerXY", "拖拽点 " + cornerPosition);
				Log.d("path_measure------", "isScroll " + isScroll + " , currentPage " + currentPage);
				// isScroll==false 滚动停止以后才判断. 防止翻页后取消之前的操作
				if (isScroll == false && currentPage == 1 && (cornerPosition == 1 || cornerPosition == 3)) {
					if (showTip == false) {
						showTip = true;
						Alert.toast("已经是第一页了");
					}
					Log.d("activity-move", "--拦截--");
					return false;
				} else {
					Log.d("activity-move", "传递move");
				}
			} else if (touchMoveX - touchDownX < 0) {
				if (isScroll ==false && currentPage == 7) {
					if (showTip == false) {
						showTip = true;
						Alert.toast("结尾了");
					}
					return false;
				}
			} else {
				Log.d("activity-move", "传递move--------");
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (touchMoveX - touchDownX > 0) {
				if (isScroll == false && currentPage == 1) {
					return false;
				}
			}
		}
		return super.dispatchTouchEvent(event);
	}

	private BookPage createPage(int pageIndex) {
		int res = getResources().getIdentifier("page_" + pageIndex, "drawable", this.getPackageName());
		Bitmap pageBitmap = BitmapFactory.decodeResource(context.getResources(), res);
		BookPage page = new BookPage(pageIndex, pageBitmap);
		return page;
	}
}
