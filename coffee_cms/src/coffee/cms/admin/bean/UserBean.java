package coffee.cms.admin.bean;

public class UserBean {

	private String id;
	private String username;
	private String password;
	/**
	 * 角色
	 * 1:普通用户
	 * 0:管理员
	 */
	private int role = 1;	

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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
}
