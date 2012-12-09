package coffee.tools.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;



public class Test {
	public static void main(String[] args) {
		JaxbList<User> userList = new JaxbList<User>();
		userList.add(new User(1, "111", "111", "v"));
		userList.add(new User(2, "222", "222", "df"));
		JaxbUtils.marshall(userList, "c:/text_2.xml", null, User.class);
		System.out.println("ok");
//		
//		JaxbList<User> userList2= new JaxbList<User>();
//		userList2 = JaxbUtils.unmarshall("c:/text.xml", null, User.class);
//		System.out.println(userList2);
	}
		
	//XmlAccessorOrder定义元素出现的顺序，XmlAccessorOrder按照字母首字符排序
	@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
//	@XmlType(propOrder={"username", "password"})
	public static class User {
		private int id;
		private String abdc;
		private String username;
		private String password;
		
		public User() {
		}
		public User(int id, String username, String password, String abdc) {
			this.id = id;
			this.username = username;
			this.password = password;
			this.abdc = abdc;
		}
		
		@XmlAttribute(name="id")
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getAbdc() {
			return abdc;
		}
		public void setAbdc(String abdc) {
			this.abdc = abdc;
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
