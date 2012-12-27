package com.xml.puller.bean;


import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;
import com.xml.puller.bean.BlogListMb.BlogStatus;

@XmlRootElement(name = "mb")
public class BlogRelayMb {

	private String retn;
	private String desc;
	
	@XmlElement(type = BlogStatus.class, name="status")
	private BlogStatus status;

	public BlogStatus getStatus() {
		return status;
	}

	public void setStatus(BlogStatus status) {
		this.status = status;
	}

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

}
