package com.xml.puller.test;

import coffee.lang.Reader;
import coffee.xml.parser.XmlParser;

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
