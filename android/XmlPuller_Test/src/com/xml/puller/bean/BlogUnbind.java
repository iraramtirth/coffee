package com.xml.puller.bean;

import coffee.xml.annotation.XmlRootElement;

/**
 * 微博解绑
 * @author wangtao
 */
@XmlRootElement(name="mb")
public class BlogUnbind {
	private String retn;
	private String desc;
	private String weibouserid;
	public String getRetn() {
		return retn;
	}
	public void setRetn(String retn) {
		this.retn = retn;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getWeibouserid() {
		return weibouserid;
	}
	public void setWeibouserid(String weibouserid) {
		this.weibouserid = weibouserid;
	}
	
}
