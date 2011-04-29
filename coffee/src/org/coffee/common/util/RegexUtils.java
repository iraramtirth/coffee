package org.coffee.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	/**
	 * 获取匹配的字符串
	 * 默认匹配整个group
	 */
	public static String match(String content, String regex) {
		String str = "";// content.replaceAll("[^"+regex+"]+", "");
		Matcher matcher = Pattern.compile(regex).matcher(content);
		while (matcher.find()) {
			str = matcher.group();
		}
		return str;
	}

	/**
	 * 获取匹配的字符串
	 * 匹配指定的group
	 */
	public static String match(String content, String regex, int group) {
		String str = "";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		while (matcher.find()) {
			str = matcher.group(group);
		}
		return str;
	}

	public static void main(String[] args) {
		// String[] str = "三星SGH_i728".split("[^(\\w|\\s)]+"); 
		String str = RegexUtils.match("货号]XM216620", "货号[\\]:]?([\\w\\d]+)",1);
		System.out.println(str);
	}

}
