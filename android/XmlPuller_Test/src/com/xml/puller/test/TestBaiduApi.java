package com.xml.puller.test;

import java.util.List;

import coffee.lang.Reader;
import coffee.xml.parser.XmlParser;

import com.xml.puller.bean.BaiduMapResult;

public class TestBaiduApi {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xml = new Reader("xml/baidu/baidumap.xml").readAll();

        XmlParser parser = new XmlParser(xml);

        System.out.println(xml);

        List<BaiduMapResult> lst = parser.pullerList(BaiduMapResult.class);

        System.out.println(lst);
	}

}
