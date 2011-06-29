package org.coffee.tools.xml;

import javax.xml.bind.annotation.XmlAttribute;



public class Test {
	public static void main(String[] args) {
		JaxbList<User> userList = new JaxbList<User>();
		userList.add(new User(1, "111", "111"));
		userList.add(new User(2, "222", "222"));
		JaxbUtils.marshall(userList, "c:/text.xml", null, User.class);
		System.out.println("ok");
		
		JaxbList<User> userList2= new JaxbList<User>();
		userList2 = JaxbUtils.unmarshall("c:/text.xml", null, User.class);
		System.out.println(userList2);
	}
		
	

	public static class User {
		private int id;
		private String username;
		private String password;
		
		public User() {
		}
		public User(int id, String username, String password) {
			this.id = id;
			this.username = username;
			this.password = password;
		}
		
		@XmlAttribute(name="id")
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
}
