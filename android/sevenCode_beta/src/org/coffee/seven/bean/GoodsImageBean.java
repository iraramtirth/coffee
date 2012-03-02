package org.coffee.seven.bean;

import coffee.util.sqlite.annotation.Bean;
import coffee.util.sqlite.annotation.Id;
import coffee.util.xml.parser.annotation.XmlRootElement;

@XmlRootElement(name="image")
@Bean(name="mmb_goods_image")
public class GoodsImageBean {
	@Id(isAuto=true)
	private int id;
	private int saleId;
	private String url;		//商品小图
	private String urlBig;	//图片大图
	private String alt;		//图片描述

	private String urlLocal;
	private String urlBigLocal;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getUrlBig() {
		return urlBig;
	}
	public void setUrlBig(String urlBig) {
		this.urlBig = urlBig;
	}
	public String getUrlLocal() {
		return urlLocal;
	}
	public void setUrlLocal(String urlLocal) {
		this.urlLocal = urlLocal;
	}
	public String getUrlBigLocal() {
		return urlBigLocal;
	}
	public void setUrlBigLocal(String urlBigLocal) {
		this.urlBigLocal = urlBigLocal;
	}
}
