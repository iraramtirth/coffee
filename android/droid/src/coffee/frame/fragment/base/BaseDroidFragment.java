package coffee.frame.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * 
 * 
 * @author coffee<br>
 *         2013上午11:29:24
 */
public abstract class BaseDroidFragment extends BaseFragment {
	/**
	 * 标题栏左右两侧按钮,中间TextView
	 */
	protected View mTitleViewLeft, mTitleViewCenter, mTitleViewRight;
	/**
	 * 标题栏内容(左、中、右)
	 */
	protected Object mTitleContentleft, mTitleContentCenter,
			mTitleContentRight;

	public void setTitle(View.OnClickListener leftOnClick,
			View.OnClickListener rightOnClick, Object leftContent,
			Object centerContent, Object rightContent) {
		// 左侧按钮单机事件
		if (this.mTitleViewLeft != null) {
			this.mTitleViewLeft.setOnClickListener(leftOnClick);
		}
		// 右侧按钮单机事件
		if (this.mTitleViewRight != null) {
			this.mTitleViewRight.setOnClickListener(rightOnClick);
		}
		// 设置内容
		this.setTitleViewContent(this.mTitleViewLeft, this.mTitleContentleft);
		this.setTitleViewContent(this.mTitleViewCenter,
				this.mTitleContentCenter);
		this.setTitleViewContent(this.mTitleViewRight, this.mTitleContentRight);
	}

	/**
	 * 设置标题View内容,分两种情况 :<br>
	 * 一种是：View为Button; Content为String <br>
	 * 一种是 View为ImageButton;content为Integer(即Drawable资源文件)<br>
	 * 
	 * @param titleView
	 * @param titleContent
	 */
	private void setTitleViewContent(View titleView, Object titleContent) {
		if (titleView == null || titleContent == null) {
			return;
		}
		// 设置可见
		titleView.setVisibility(View.VISIBLE);
		// 一种是：View为Button; Content为String
		if (titleContent instanceof String && titleView instanceof Button) {
			((Button) titleView).setText(String.valueOf(titleContent));
		}
		// 一种是 View为ImageButton;content为Integer(即Drawable资源文件)
		else if (titleContent instanceof Integer
				&& titleView instanceof ImageButton) {
			((ImageButton) titleView).setImageResource(Integer.valueOf(String
					.valueOf(titleContent)));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutResId != -1) {
			View layout = inflater.inflate(layoutResId, container, false);
			// ImageButton fragmentClose = (ImageButton) layout
			// .findViewById(R.id.fragment);
			// if (fragmentClose != null) {
			// fragmentClose.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// FragmentMgr.remove(BaseDroidFragment.this);
			// }
			// });
			// }
			return layout;
		} else {
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}

}
