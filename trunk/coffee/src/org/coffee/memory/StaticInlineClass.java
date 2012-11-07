package org.coffee.memory;

public class StaticInlineClass {

	static class User {
		private String username;
		private String password;

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

	public static void main(String[] args) {

		for (int i = 0; i < 2; i++) {
			User user1 = new User();
			System.out.println(user1);
			user1 = new User();
			System.out.println(user1);
		}

	}
}
