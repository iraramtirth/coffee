package com.xml.puller.test;

import com.chinaunicom.woyou.utils.lang.Reader;
import com.chinaunicom.woyou.utils.xml.parser.XmlParser;
import com.xml.puller.TreeGroupData.GroupInfo;

public class TreeGroupTest {
	
	public static void main(String[] args) {
		   String xml = new Reader("tree_group.xml").readAll();

	        XmlParser parser = new XmlParser(xml);

	        System.out.println(xml);

	        GroupInfo groupMember = parser.pullerT(GroupInfo.class);

	        System.out.println(groupMember);
	}
}
