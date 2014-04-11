package coffee.im.bluetooth.activity.base;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import coffee.utils.framework.ImmUtils;

/**
 * 添加了一些适应具体界面|业务需求的功能
 * 
 * @author coffee
 * 
 *         2013年12月19日下午7:08:30
 */
public abstract class BaseActivity extends FrameBaseActivity {
	private ViewSwitcher mTitleLeft, mTitleRight;
	private View mTitleMiddle;
	/**
	 * 返回到上一页||通用Title
	 */
	protected TitleRes mBackTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private ProgressBar mProgressBar;
	private TextView mEmptyView;

	@Override
	protected void findViewById() {
		initTitle();
		int loadingId = getResources().getIdentifier("loading_progressBar", "id", getPackageName());
		mProgressBar = (ProgressBar) findViewById(loadingId);
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
		int emptyId = getResources().getIdentifier("empty_text", "id", getPackageName());
		mEmptyView = (TextView) findViewById(emptyId);
		if (mEmptyView != null) {
			((ViewGroup) mEmptyView.getParent()).setVisibility(View.GONE);
		}
	}

	private void initTitle() {
		int resBack = getResources().getIdentifier("title_back", "drawable", getPackageName());
		int resLeft = getResources().getIdentifier("title_left_switcher", "id", getPackageName());
		int resMiddle = getResources().getIdentifier("title_middle", "id", getPackageName());
		int resRight = getResources().getIdentifier("title_right_switcher", "id", getPackageName());
		//
		mTitleLeft = (ViewSwitcher) findViewById(resLeft);
		mTitleMiddle = findViewById(resMiddle);
		mTitleRight = (ViewSwitcher) findViewById(resRight);
		//
		mBackTitle = new TitleRes(1, resBack, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public class TitleRes {
		/**
		 * 0 文本, 1图片
		 */
		private int type;
		/**
		 * 字符串或者int资源(其中资源包括文本或图片)
		 */
		private Object res;

		private View.OnClickListener clickListener;

		public int getType() {
			return type;
		}

		//
		public TitleRes(int type, Object res, OnClickListener clickListener) {
			super();
			this.type = type;
			this.res = res;
			this.clickListener = clickListener;
		}

		/**
		 * 适用于标题是文字的。没单击事件
		 * 
		 * @param title
		 */
		public TitleRes(String title) {
			this.type = 0;
			this.res = title;
			this.clickListener = null;
		}

		/**
		 * 适用于title右侧文本单击事件
		 * 
		 * @param title
		 * @param clickListener
		 */
		public TitleRes(String title, OnClickListener clickListener) {
			this.type = 0;
			this.res = title;
			this.clickListener = clickListener;
		}

		public TitleRes(int imageIcon, OnClickListener clickListener) {
			this.type = 1;
			this.res = imageIcon;
			this.clickListener = clickListener;
		}

		public void setType(int type) {
			this.type = type;
		}

		public Object getRes() {
			return res;
		}

		public void setRes(Object res) {
			this.res = res;
		}

		public View.OnClickListener getClickListener() {
			return clickListener;
		}

		public void setClickListener(View.OnClickListener clickListener) {
			this.clickListener = clickListener;
		}
	}

	/**
	 * 显示通用Title<br>
	 * 调用该方法时需要确保子类成功调用了 {@link #findViewById()}
	 */
	public void setCommonTitle(String titleText) {
		setTitle(new TitleRes[] { mBackTitle, new TitleRes(0, titleText, null), null });
	}

	protected void setTitle(TitleRes... reses) {
		for (int i = 0; i < reses.length; i++) {
			TitleRes res = reses[i];
			if (i == 0) {
				handleTitle(mTitleLeft, res, 0);
			} else if (i == 1) {
				handleTitle(mTitleMiddle, res, 1);
			} else if (i == 2) {
				handleTitle(mTitleRight, res, 2);
			}
		}
	}

	/**
	 * 设置右侧按钮
	 * 
	 * @param res
	 */
	protected void setTitleRight(TitleRes res) {
		handleTitle(mTitleRight, res, 2);
	}

	/**
	 * 设置组件可见性
	 * 
	 * @param position
	 *            0-左 1-中 2-右
	 * @param visibility
	 *            {@link View#VISIBLE View#GONE}
	 */
	protected void setTitleVisibility(int position, int visibility) {
		if (position == 0) {
			mTitleLeft.setVisibility(visibility);
		} else if (position == 1) {
			mTitleMiddle.setVisibility(visibility);
		} else if (position == 2) {
			mTitleRight.setVisibility(visibility);
		}
	}

	private void handleTitle(View view, TitleRes res, int position) {
		if (res == null) {
			view.setVisibility(View.INVISIBLE);
			// 让该view占指定资源的空间
			// view.setBackgroundResource(R.drawable.title_back);
			return;
		}
		view.setVisibility(View.VISIBLE);
		// 文本
		if (res.getType() == 0) {
			TextView textView = null;
			if (position == 0 || position == 2) {
				ViewSwitcher viewSwitcher = (ViewSwitcher) view;
				viewSwitcher.setDisplayedChild(0);
				textView = (TextView) viewSwitcher.getChildAt(0);
			} else {
				textView = (TextView) view;
			}
			if (res.getRes() instanceof Integer) {
				textView.setText(Integer.valueOf(res.getRes().toString()));
			} else {
				textView.setText(String.valueOf(res.getRes()));
			}
			//
			if (res.getClickListener() != null) {
				textView.setOnClickListener(res.getClickListener());
			}
		}
		// 图片
		else {
			ImageView imageView = null;
			if (position == 0 || position == 2) {
				ViewSwitcher viewSwitcher = (ViewSwitcher) view;
				viewSwitcher.setDisplayedChild(1);
				imageView = (ImageView) viewSwitcher.getChildAt(1);
			} else {
				imageView = (ImageView) view;
			}
			imageView.setImageResource(Integer.valueOf(res.getRes().toString()));

			if (res.getClickListener() != null) {
				imageView.setOnClickListener(res.getClickListener());
			}
		}
	}

	// 隐藏键盘输入法
	public final static int MSG_IMM_HIDE = 1000;
	public final static int MSG_IMM_SHOW = 1000 + 1;

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_IMM_HIDE:
			ImmUtils.hide(context);
		}
		return false;
	}

	// ******************************************************************************************

	public boolean isEmpty(Object str) {
		if (str == null || str.toString().trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断Http请求是否返回1000
	 * 
	 * @param ret
	 * @return
	 */
	public boolean isRetOK(Object ret) {
		try {
			if (isEmpty(ret)) {
				return false;
			}
			JSONObject json = new JSONObject(ret + "");
			//
			if (json.has("ret")) {
				if ("1000".equals(json.getString("ret"))) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void showLoadingBar() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	public void cancelLoadingBar() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	/**
	 * 该方法主要考虑到一个界面里多个EmptyView
	 * 
	 * @param emptyText
	 */
	protected void setEmptyTextView(TextView emptyText) {
		this.mEmptyView = emptyText;
	}

	public void setEmptyView(ViewGroup viewGroup, String emptyText) {
		if (mEmptyView == null) {
			return;
		}
		//
		if (viewGroup instanceof ListView || viewGroup instanceof GridView) {
			AbsListView mAbsListView = (AbsListView) viewGroup;
			if (mAbsListView == null || mAbsListView.getAdapter() == null || mAbsListView.getAdapter().getCount() == 0) {
				((View) mEmptyView.getParent()).setVisibility(View.VISIBLE);
				if (isEmpty(emptyText) == false) {
					mEmptyView.setText(emptyText);
				}
			} else {
				((View) mEmptyView.getParent()).setVisibility(View.GONE);
			}
		}
//		else if (viewGroup instanceof XListView) {
//			XListView xListView = (XListView) viewGroup;
//			// XListView含有Footer
//			if (xListView == null || xListView.getListView().getAdapter() == null || xListView.getListView().getAdapter().getCount() <= 1) {
//				((View) mEmptyView.getParent()).setVisibility(View.VISIBLE);
//				if (isEmpty(emptyText) == false) {
//					mEmptyView.setText(emptyText);
//				}
//			} else {
//				((View) mEmptyView.getParent()).setVisibility(View.GONE);
//			}
//		}
	}
}
