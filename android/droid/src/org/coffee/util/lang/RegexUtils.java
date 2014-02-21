package org.coffee.util.lang;

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
	 * 
	 * @param group
	 *            : 0 代表取所有的组, 其他特定数字1、2、3 代表取指定的某组
	 */
	public static String[] matchAll(String content, String regex, int group) {
		Matcher matcher = Pattern.compile(regex).matcher(content);
		List<String> items = new ArrayList<String>();
		String item;
		if (group == 0) {
			group = matcher.groupCount();
		}
		if (matcher.find()) {
			for (int i = 1; i <= group; i++) {
				item = matcher.group(i);
				items.add(item);
			}
		}

		return items.toArray(new String[0]);
	}

	public static void main(String[] args) {
		String data = "	<voip> " + "<from>100025@im.wo.com.cn/woclient</from>" + "<session-id>C6ZWHr11</session-id>" + "<call-type>voip-voice</call-type>" + "<voip>";
		// String[] str = "三星SGH_i728".split("[^(\\w|\\s)]+");
		String[] str = RegexUtils.matchAll(data, ".+?\\<from\\>(.+?)\\</from\\>.*?(\\<session-id\\>.+?\\</session-id\\>)", 0);

		for (String s : str) {
			System.out.println(s);
		}

		String  from = match(data, "<from>(.+?)</from>", 1);
		System.out.println(from);
		
	}

}
