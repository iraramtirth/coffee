package com.xml.puller.bean;

import java.util.ArrayList;
import java.util.List;

import coffee.xml.annotation.XmlElement;
import coffee.xml.annotation.XmlRootElement;

/**
 * 从服务器获取blog列表
 * 
 * @author wangtao
 */
@XmlRootElement(name = "mb")
public class BlogListMb {

	private String retn;
	private String desc;

	@XmlElement(type = BlogStatus.class, name = "status")
	public List<BlogStatus> statusList = new ArrayList<BlogStatus>();
	@XmlElement(type = BlogComment.class, name = "comment")
	private BlogComment comment;
	
	
	@XmlRootElement(name = "retweeted_status") 
	public static class RetweetedStatus{
		private String id;
		private String source;
		private String text;
		private String weiboid;
		private String in_reply_to_status_id;
		private String created_at;
		private String favorited;
		private String truncated;
		private String in_reply_to_user_id;
		private String in_reply_to_screen_name;
		private String thumbnail_pic;
		private String bmiddle_pic;
		private String original_pic;
		private String geo;
		private String mid;
		private String deleted;
		
		@XmlElement(type = BlogUser.class, name="user")
		private BlogUser blogUser;
		

		public String getGeo() {
			return geo;
		}

		public void setGeo(String geo) {
			this.geo = geo;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

		public String getDeleted() {
			return deleted;
		}

		public void setDeleted(String deleted) {
			this.deleted = deleted;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getWeiboid() {
			return weiboid;
		}

		public void setWeiboid(String weiboid) {
			this.weiboid = weiboid;
		}

		public String getIn_reply_to_status_id() {
			return in_reply_to_status_id;
		}

		public void setIn_reply_to_status_id(String in_reply_to_status_id) {
			this.in_reply_to_status_id = in_reply_to_status_id;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public String getFavorited() {
			return favorited;
		}

		public void setFavorited(String favorited) {
			this.favorited = favorited;
		}

		public String getTruncated() {
			return truncated;
		}

		public void setTruncated(String truncated) {
			this.truncated = truncated;
		}

		public String getIn_reply_to_user_id() {
			return in_reply_to_user_id;
		}

		public void setIn_reply_to_user_id(String in_reply_to_user_id) {
			this.in_reply_to_user_id = in_reply_to_user_id;
		}

		public String getIn_reply_to_screen_name() {
			return in_reply_to_screen_name;
		}

		public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
			this.in_reply_to_screen_name = in_reply_to_screen_name;
		}

		public String getThumbnail_pic() {
			return thumbnail_pic;
		}

		public void setThumbnail_pic(String thumbnail_pic) {
			this.thumbnail_pic = thumbnail_pic;
		}

		public String getBmiddle_pic() {
			return bmiddle_pic;
		}

		public void setBmiddle_pic(String bmiddle_pic) {
			this.bmiddle_pic = bmiddle_pic;
		}

		public String getOriginal_pic() {
			return original_pic;
		}

		public void setOriginal_pic(String original_pic) {
			this.original_pic = original_pic;
		}

		public BlogUser getBlogUser() {
			return blogUser;
		}

		public void setBlogUser(BlogUser blogUser) {
			this.blogUser = blogUser;
		}
		
	}

	@XmlRootElement(name = "status")
	public static class BlogStatus {
		private String id;
		private String source;
		private String text;
		private String weiboid;
		private String in_reply_to_status_id;
		private String created_at;
		private String favorited;
		private String truncated;
		private String in_reply_to_user_id;
		private String in_reply_to_screen_name;
		private String thumbnail_pic;
		private String bmiddle_pic;
		private String original_pic;
		private String geo;
		private String mid;
		private String type;
		private String bindinged;
		private String weibouserid;
		private String weiboname;
		private String weiboicon;
		private String weibobindurl;
		private String weibobindStringerface;
		private String weiboregisterurl;
		private String mbaccount;
		
		@XmlElement(type = BlogUser.class, name="user")
		private BlogUser blogUser;
		@XmlElement(type = RetweetedStatus.class, name="retweeted_status")
		private RetweetedStatus retweetedStatus;
		
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getBindinged() {
			return bindinged;
		}

		public void setBindinged(String bindinged) {
			this.bindinged = bindinged;
		}

		public String getWeibouserid() {
			return weibouserid;
		}

		public void setWeibouserid(String weibouserid) {
			this.weibouserid = weibouserid;
		}

		public String getWeiboname() {
			return weiboname;
		}

		public void setWeiboname(String weiboname) {
			this.weiboname = weiboname;
		}

		public String getWeiboicon() {
			return weiboicon;
		}

		public void setWeiboicon(String weiboicon) {
			this.weiboicon = weiboicon;
		}

		public String getWeibobindurl() {
			return weibobindurl;
		}

		public void setWeibobindurl(String weibobindurl) {
			this.weibobindurl = weibobindurl;
		}

		public String getWeibobindStringerface() {
			return weibobindStringerface;
		}

		public void setWeibobindStringerface(String weibobindStringerface) {
			this.weibobindStringerface = weibobindStringerface;
		}

		public String getWeiboregisterurl() {
			return weiboregisterurl;
		}

		public void setWeiboregisterurl(String weiboregisterurl) {
			this.weiboregisterurl = weiboregisterurl;
		}

		public String getMbaccount() {
			return mbaccount;
		}

		public void setMbaccount(String mbaccount) {
			this.mbaccount = mbaccount;
		}

		public String getGeo() {
			return geo;
		}

		public void setGeo(String geo) {
			this.geo = geo;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

		public RetweetedStatus getRetweetedStatus() {
			return retweetedStatus;
		}

		public void setRetweetedStatus(RetweetedStatus retweetedStatus) {
			this.retweetedStatus = retweetedStatus;
		}

		public String getWeiboid() {
			return weiboid;
		}

		public void setWeiboid(String weiboid) {
			this.weiboid = weiboid;
		}

		public String getIn_reply_to_status_id() {
			return in_reply_to_status_id;
		}

		public void setIn_reply_to_status_id(String in_reply_to_status_id) {
			this.in_reply_to_status_id = in_reply_to_status_id;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public String getFavorited() {
			return favorited;
		}

		public void setFavorited(String favorited) {
			this.favorited = favorited;
		}

		public String getTruncated() {
			return truncated;
		}

		public void setTruncated(String truncated) {
			this.truncated = truncated;
		}

		public String getIn_reply_to_user_id() {
			return in_reply_to_user_id;
		}

		public void setIn_reply_to_user_id(String in_reply_to_user_id) {
			this.in_reply_to_user_id = in_reply_to_user_id;
		}

		public String getIn_reply_to_screen_name() {
			return in_reply_to_screen_name;
		}

		public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
			this.in_reply_to_screen_name = in_reply_to_screen_name;
		}

		public String getThumbnail_pic() {
			return thumbnail_pic;
		}

		public void setThumbnail_pic(String thumbnail_pic) {
			this.thumbnail_pic = thumbnail_pic;
		}

		public String getBmiddle_pic() {
			return bmiddle_pic;
		}

		public void setBmiddle_pic(String bmiddle_pic) {
			this.bmiddle_pic = bmiddle_pic;
		}

		public String getOriginal_pic() {
			return original_pic;
		}

		public void setOriginal_pic(String original_pic) {
			this.original_pic = original_pic;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public BlogUser getBlogUser() {
			return blogUser;
		}

		public void setBlogUser(BlogUser blogUser) {
			this.blogUser = blogUser;
		}

	}
	
	@XmlRootElement(name = "comment")
	public static class BlogComment{
		private String created_at;
		private String id;
		private String text;
		private String source;
		private String mid;
		
		@XmlElement(type = BlogUser.class, name = "user")
		private BlogUser user;
		@XmlElement(type = BlogStatus.class, name = "status")
		private BlogStatus status;
		public String getCreated_at() {
			return created_at;
		}
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getMid() {
			return mid;
		}
		public void setMid(String mid) {
			this.mid = mid;
		}
		public BlogUser getUser() {
			return user;
		}
		public void setUser(BlogUser user) {
			this.user = user;
		}
		public BlogStatus getStatus() {
			return status;
		}
		public void setStatus(BlogStatus status) {
			this.status = status;
		}
		
	}

	@XmlRootElement(name = "user")
	public static class BlogUser {
		private String name;
		private String id;
		private String location;
		private String description;
		private String domain;
		private String gender;
		private String city;
		private String province;
		private String weiboid;
		private String screen_name;
		private String url;
		private String profile_image_url;
		private String created_at;
		private String followers_count;
		private String friends_count;
		private String statuses_count;
		private String favourites_count;
		private String following;
		private String verified;
		private String geo_enabled;
		private String remark;
		
		@XmlElement(type = BlogStatus.class, name = "status")
		private BlogStatus status;
		

		public BlogStatus getStatus() {
			return status;
		}

		public void setStatus(BlogStatus status) {
			this.status = status;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getGeo_enabled() {
			return geo_enabled;
		}

		public void setGeo_enabled(String geo_enabled) {
			this.geo_enabled = geo_enabled;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getWeiboid() {
			return weiboid;
		}

		public void setWeiboid(String weiboid) {
			this.weiboid = weiboid;
		}

		public String getScreen_name() {
			return screen_name;
		}

		public void setScreen_name(String screen_name) {
			this.screen_name = screen_name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getProfile_image_url() {
			return profile_image_url;
		}

		public void setProfile_image_url(String profile_image_url) {
			this.profile_image_url = profile_image_url;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public String getFollowers_count() {
			return followers_count;
		}

		public void setFollowers_count(String followers_count) {
			this.followers_count = followers_count;
		}

		public String getFriends_count() {
			return friends_count;
		}

		public void setFriends_count(String friends_count) {
			this.friends_count = friends_count;
		}

		public String getStatuses_count() {
			return statuses_count;
		}

		public void setStatuses_count(String statuses_count) {
			this.statuses_count = statuses_count;
		}

		public String getFavourites_count() {
			return favourites_count;
		}

		public void setFavourites_count(String favourites_count) {
			this.favourites_count = favourites_count;
		}

		public String getFollowing() {
			return following;
		}

		public void setFollowing(String following) {
			this.following = following;
		}

		public String getVerified() {
			return verified;
		}

		public void setVerified(String verified) {
			this.verified = verified;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
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

	public List<BlogStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<BlogStatus> statusList) {
		this.statusList = statusList;
	}

	public BlogComment getComment() {
		return comment;
	}

	public void setComment(BlogComment comment) {
		this.comment = comment;
	}

	
}
