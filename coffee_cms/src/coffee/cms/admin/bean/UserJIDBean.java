package coffee.cms.admin.bean;


import coffee.Config;
import coffee.database.annotation.Bean;
import coffee.database.annotation.Column;
import coffee.database.annotation.Id;

/**
 * 聊天JID
 *
 * @author coffee
 * 20122012-11-9下午12:15:39
 */
@Bean(name="user_jid")
public class UserJIDBean {

	@Id
	@Column(name="jid_id")
	private long id;
	private String jid;
	@Column(name="jid_sha")
	private String password;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		if(!jid.endsWith("@"+Config.XMPP_HOST))
		{
			jid += "@" + Config.XMPP_HOST;
		}
		this.jid = jid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
