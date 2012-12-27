package com.xml.puller.bean;

import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;
import com.xml.puller.bean.BlogListMb.BlogComment;

/**
 * 
 * @author xxxx
 */
@XmlRootElement(name = "mb")
public class BlogCommentMb {

	private String retn;
	private String desc;
	
	@XmlElement(type = BlogComment.class, name = "comment")
	private BlogComment comment;
	

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

	public BlogComment getComment() {
		return comment;
	}

	public void setComment(BlogComment comment) {
		this.comment = comment;
	}
	
	
}
