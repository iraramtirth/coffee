package org.coffee.hibernate;

import java.util.List;
/**
 * 分页 
 * @author wangtao
 * @param <T>
 */
public class PagerModel<T>{
	private int total;       // 总记录数
	private int size = 10;	 // 单页条数
	private List<T> items;   // 单页记录列表
	/**
	 * 解决pager-taglib 在 struts2 中的如下异常信息
	 * ognl.OgnlException: target is null for 
	 * setProperty(null, "offset", [Ljava.lang.String;@1e09e6a)
	 */
	private int offset;			// 偏移量
	private int curpage = 1;	// 当前页 : 默认是第一页
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	} 
}
