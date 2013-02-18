package coffee.frame.adapter.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	protected Activity mContext;

	protected List<T> items;

	/**
	 * items中的元素的个数的限制，0代表没有限制
	 */
	protected int MAX_SIZE = 0;

	/********************* 以下是自定义 ************************/
//	protected BaseAdapter(int size) {
//		this.MAX_SIZE = size;
//	}

	public BaseAdapter(List<T> items, Activity mContext) {
		this.items = items;
		this.mContext = mContext;
		if (this.items == null) {
			this.items = new ArrayList<T>();
		}
	}

	/********************* 以下是自定义 ************************/

	public void setMaxSize(int maxSize) {
		this.MAX_SIZE = maxSize;
	}

	/**
	 * 设置完数据以后是否直接刷新
	 * 
	 * @param items
	 * @param isNotify
	 */
	public void notifyData(List<T> items, boolean isNotify) {
		this.items = items;
		if (items == null) {
			return;
		}
		if (this.MAX_SIZE != 0) {
			// 只取前MAX_SIZE个元素
			if (this.items.size() > MAX_SIZE) {
				this.items = this.items.subList(0, MAX_SIZE);
			}
		}
		if (isNotify) {
			notifyDataSetChanged();
		}
	}

	/**
	 * 新增一个元素
	 * 
	 * @param obj
	 * @param isNotify
	 *            ： 是否需要刷新
	 */
	public void notifyAdd(T obj, boolean isNotify) {
		if (this.items == null) {
			this.items = new ArrayList<T>();
		}
		this.items.add(obj);
		if (this.MAX_SIZE != 0) {
			// 只取最后MAX_SIZE个元素
			if (this.items.size() > MAX_SIZE) {
				this.items = this.items.subList(this.items.size() - MAX_SIZE,
						this.items.size());
			}
		}
		if (isNotify) {
			notifyDataSetChanged();
		}
	}

	/********************* 以下是父类实现 ************************/
	@Override
	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	@Override
	public T getItem(int position) {
		if (items != null) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
