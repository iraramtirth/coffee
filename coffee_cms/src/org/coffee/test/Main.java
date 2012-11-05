package org.coffee.test;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceBundle res = ResourceBundle.getBundle("test",Locale.CHINA);
		System.out.println(res.getString("ddd"));
	}

}
