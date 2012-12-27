package com.xml.puller.bean;

import java.util.ArrayList;
import java.util.List;

import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;
import com.xml.puller.bean.BlogListMb.BlogComment;

@XmlRootElement(name = "mb")
public class BlogCommentListMb {

	private String retn;
	private String desc;
	
	@XmlElement(type = BlogComment.class, name = "comment")
	private List<BlogComment> commentList = new ArrayList<BlogComment>();
	

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

	public List<BlogComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<BlogComment> commentList) {
		this.commentList = commentList;
	}
}
