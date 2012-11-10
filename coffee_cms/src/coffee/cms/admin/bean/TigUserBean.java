package coffee.cms.admin.bean;

import java.util.Date;

import coffee.Config;
import coffee.common.MD5Utils;
import coffee.database.annotation.Bean;
import coffee.database.annotation.Column;
import coffee.database.annotation.Id;

/**
 * Tigase XMPP Server创建的User
 * 
 * @author coffee 20122012-11-9下午12:15:39
 */
@Bean(name = "tig_users")
public class TigUserBean {

	@Id
	@Column(name = "uid")
	private long id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "sha1_user_id")
	private String shalUserId;

	@Column(name = "user_pw")
	private String password;

	@Column(name = "acc_create_time")
	private Date accCreateTime;

	@Column(name = "last_login")
	private Date lastLoginTime;

	@Column(name = "last_logout")
	private Date lastLogoutTime;

	@Column(name = "online_status")
	private int onlineStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		if (!userId.endsWith("@" + Config.XMPP_HOST)) {
			userId += "@" + Config.XMPP_HOST;
		}
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.setShalUserId(MD5Utils.getMD5Str(this.password));
	}

	public String getShalUserId() {
		return shalUserId;
	}

	public void setShalUserId(String shalUserId) {
		this.shalUserId = shalUserId;
	}

	public Date getAccCreateTime() {
		return accCreateTime;
	}

	public void setAccCreateTime(Date accCreateTime) {
		this.accCreateTime = accCreateTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Date lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
}
