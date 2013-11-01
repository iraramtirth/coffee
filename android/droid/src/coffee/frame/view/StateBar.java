package coffee.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 状态条 StateBar默认有五个子组件ImageView用来显示状态<br>
 * 使用的时候先调用 {@link #setConfig(int, int, int)}配置属性 <br>
 * 然后设置当前显示的 {@link #setSelected(int)} <br>
 * 
 * @author wangtaoyfx<br>
 *         2013下午3:56:49
 */
public class StateBar extends LinearLayout {

	/**
	 * 选中后显示的drawable
	 */
	private int drawableSelected = -1;
	/**
	 * 默认的drawable
	 */
	private int drawableDefault = -1;

	private View[] children;

	public StateBar(Context context) {
		super(context);
	}

	public StateBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 配置state相关属性
	 * 
	 * @param size
	 *            要显示的ImageView的个数,最多为5个
	 * @param drawableSelected
	 *            选中后显示的图
	 * @param drawableDefault
	 *            默认图
	 */
	public void setConfig(int size, int drawableSelected, int drawableDefault) {
		this.drawableSelected = drawableSelected;
		this.drawableDefault = drawableDefault;

		children = new View[this.getChildCount()];
		for (int i = 0; i < children.length; i++) {
			if (i < size) {
				children[i] = this.getChildAt(i);
				children[i].setVisibility(View.VISIBLE);
			} else {
				break;
			}
		}
		resetState();
		setSelected(0);
	}

	/**
	 * 当前的位置
	 * 
	 * @param position
	 *            下标从0开始
	 */
	public void setSelected(int position) {
		if (position > children.length || position < 0) {
			return;
		}
		//
		resetState();
		//
		if (drawableSelected != -1) {
			((ImageView) children[position]).setImageResource(drawableSelected);
		}
	}

	/**
	 * 重置状态
	 */
	private void resetState() {
		if (drawableDefault != -1) {
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null) {
					((ImageView) children[i])
							.setImageResource(this.drawableDefault);
				}
			}
		}
	}
}
