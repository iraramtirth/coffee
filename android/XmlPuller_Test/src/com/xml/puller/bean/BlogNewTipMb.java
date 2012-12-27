package com.xml.puller.bean;

import java.util.ArrayList;
import java.util.List;

import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;

@XmlRootElement(name = "mb")
public class BlogNewTipMb {

	private String retn;
	private String desc;
	
	@XmlElement(type = Count.class,name = "count")
	public List<Count> countList = new ArrayList<Count>();
	
	
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


	public List<Count> getCountList() {
		return countList;
	}


	public void setCountList(List<Count> countList) {
		this.countList = countList;
	}

	@XmlRootElement(name = "count")
	public static class Count{
		private String new_status;
		private String followers;
		private String status;
		private String dm;
		private String mentions;
		private String comments;
		private String weiboid;
		public String getNew_status() {
			return new_status;
		}
		public void setNew_status(String new_status) {
			this.new_status = new_status;
		}
		public String getFollowers() {
			return followers;
		}
		public void setFollowers(String followers) {
			this.followers = followers;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getDm() {
			return dm;
		}
		public void setDm(String dm) {
			this.dm = dm;
		}
		public String getMentions() {
			return mentions;
		}
		public void setMentions(String mentions) {
			this.mentions = mentions;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public String getWeiboid() {
			return weiboid;
		}
		public void setWeiboid(String weiboid) {
			this.weiboid = weiboid;
		}
	}
}
