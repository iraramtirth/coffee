package com.xml.puller.bean;

import java.util.ArrayList;
import java.util.List;

import coffee.xml.annotation.XmlElement;
import coffee.xml.annotation.XmlRootElement;

import com.xml.puller.bean.BlogListMb.BlogStatus;

@XmlRootElement(name = "mb")
public class BlogNewMb {

	private String retn;
	private String desc;

	@XmlElement(type = BlogStatus.class, name = "status")
	public List<BlogStatus> statusList = new ArrayList<BlogStatus>();

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

	public List<BlogStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<BlogStatus> statusList) {
		this.statusList = statusList;
	}


}
