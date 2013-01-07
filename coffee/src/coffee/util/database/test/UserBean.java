package coffee.util.database.test;

import coffee.util.database.annotation.Bean;
import coffee.util.database.annotation.Column;
import coffee.util.database.annotation.Id;

@Bean(name="user_jid")
public class UserBean {

	@Id
	@Column(name="jid_id")
	private String id;
	@Column(name="jid")
	private String username;
	@Column(name="jid_sha")
	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
