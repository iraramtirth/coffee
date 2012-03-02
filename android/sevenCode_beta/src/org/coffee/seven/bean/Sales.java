package org.coffee.seven.bean;

import java.util.ArrayList;
import java.util.List;

import coffee.util.xml.parser.annotation.GenericType;
import coffee.util.xml.parser.annotation.XmlRootElement;

@XmlRootElement(name="sales")
public class Sales {
	private long nowTime;
	
	@GenericType(type=SaleBean.class)
	private List<SaleBean> saleList = new ArrayList<SaleBean>();
	
	public long getNowTime() {
		return nowTime;
	}
	public void setNowTime(long nowTime) {
		this.nowTime = nowTime;
	}
	public List<SaleBean> getSaleList() {
		return saleList;
	}
	public void setSaleList(List<SaleBean> saleList) {
		this.saleList = saleList;
	}
}
