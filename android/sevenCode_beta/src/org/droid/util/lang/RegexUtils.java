package org.droid.util.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	/**
	 * 获取匹配的字符串 默认匹配整个group
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
	 * 获取匹配的字符串 匹配指定的group : 0 代表整体
	 * 
	 * @param content
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String match(String content, String regex, int group) {
		String str = "";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		while (matcher.find()) {
			str = matcher.group(group);
		}
		return str;
	}

	/**
	 * 匹配所有的结果
	 */
	public String[] matchAll(String content, String regex, int group) {
		Matcher matcher = Pattern.compile(regex).matcher(content);
		List<String> items = new ArrayList<String>();
		String item;
		while (matcher.find()) {
			item = matcher.group(group);
			items.add(item);
		}
		return items.toArray(new String[0]);
	}

}