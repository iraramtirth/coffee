package org.coffee.util;

import java.util.List;
/**
 * 分页 
 * @author wangtao
 * @param <T>
 */
public class PagerModel<T>{
	private int total;      // 总记录数
	private int size = 10;	// 单页条数
	private List<T> items;  // 单页记录列表
	
	private int curpage;	// 当面页数
	private int offset;		// 偏移量
	
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
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	} 
}
