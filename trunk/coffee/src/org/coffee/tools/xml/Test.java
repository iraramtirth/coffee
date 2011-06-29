package org.coffee.tools.xml;

import org.coffee.tools.xml.JaxbList.XmlItem;


public class Test {
	public static void main(String[] args) {
		JaxbList userList = new JaxbList();
		userList.add(new XmlItem(1, "111", "111"));
		userList.add(new XmlItem(2, "222", "222"));
		JaxbUtils.marshall(userList, "c:/text.xml", null);
		System.out.println("ok");
	}
		
}
