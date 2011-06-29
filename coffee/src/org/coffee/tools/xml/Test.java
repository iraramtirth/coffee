package org.coffee.tools.xml;



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
		
}
