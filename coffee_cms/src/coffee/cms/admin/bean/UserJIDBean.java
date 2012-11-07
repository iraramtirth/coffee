package coffee.cms.admin.bean;


import coffee.database.annotation.Bean;
import coffee.database.annotation.Column;
import coffee.database.annotation.Id;

@Bean(name="user_jid")
public class UserJIDBean {

	@Id
	@Column(name="jid_id")
	private String id;
	private String jid;
	@Column(name="jid_sha")
	private String password;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
