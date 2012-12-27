package com.xml.puller.bean;

import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;
import com.xml.puller.bean.BlogListMb.BlogUser;

@XmlRootElement(name = "mb")
public class BlogUserInfoMb {

	private String retn;
	private String desc;
	
	@XmlElement(type = BlogUser.class, name="user")
	private BlogUser blogUser;
	

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

	public BlogUser getBlogUser() {
		return blogUser;
	}

	public void setBlogUser(BlogUser blogUser) {
		this.blogUser = blogUser;
	}
	
}
