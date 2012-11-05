package coffee.cms.admin.bean;

import java.util.List;
/**
 * 
 * @author coffee
 */
public class Pager {
	/**
	 * 总页数
	 */
	private int count;
	/**
	 * 单页记录
	 */
	public static int size = 50;
	/**
	 * 当前页
	 */
	private int curpage = 1;
	/**
	 * 结果列表
	 */
	private List<?> items;

	/**
	 * 扩展字段;主要用于Object[][]二维数组
	 */
	private Object arr;

	/**
	 * 总页数
	 */
	private int page;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		if (count % size == 0) {
			this.page = count / size;
		} else {
			this.page = count / size + 1;
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size_) {
		size = size_;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Object getArr() {
		return arr;
	}

	public void setArr(Object arr) {
		this.arr = arr;
	}
}
