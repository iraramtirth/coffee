package test;

import java.net.URL;
import java.net.URLClassLoader;

import coffee.util.database.core.DBUtils;

public class Main {
	public static void main(String[] args) {

		try {
			String url = "file:/"
					+ "D:/workspace/android/apad_frame/bin/classes/";
			// 传入一个URL数组,注意末尾那个反斜杠不能丢
			URLClassLoader ul = new URLClassLoader(new URL[] { new URL(url) });
			Class<?> c = ul.loadClass("com.fetion.apad.model.MessageModel");
			
			
			DBUtils.generateTableSql(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
