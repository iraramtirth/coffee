package coffee.test.activity;

import coffee.util.sqlite.annotation.Column;
import coffee.util.sqlite.annotation.Id;

public class User {
	@Id
	private int id;
	@Column(length=20)
	private String username;
	@Column(length=30)
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
