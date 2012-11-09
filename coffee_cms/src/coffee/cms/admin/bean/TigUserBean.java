package coffee.cms.admin.bean;


import coffee.Config;
import coffee.database.annotation.Bean;
import coffee.database.annotation.Column;
import coffee.database.annotation.Id;

/**
 * Tigase XMPP Server创建的User
 *
 * @author coffee
 * 20122012-11-9下午12:15:39
 */
@Bean(name="tig_user")
public class TigUserBean {

	@Id
	@Column(name="uid")
	private long id;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="user_pw")
	private String password;
	
	@Column(name="acc_create_time")
	private String accCreateTime;
	
	@Column(name="last_login")
	private String lastLogin;
	
	@Column(name="last_logout")
	private String lastLogout;

	@Column(name="online_status")
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
		if(!userId.endsWith("@"+Config.XMPP_HOST))
		{
			userId += "@" + Config.XMPP_HOST;
		}
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccCreateTime() {
		return accCreateTime;
	}

	public void setAccCreateTime(String accCreateTime) {
		this.accCreateTime = accCreateTime;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastLogout() {
		return lastLogout;
	}

	public void setLastLogout(String lastLogout) {
		this.lastLogout = lastLogout;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
}
