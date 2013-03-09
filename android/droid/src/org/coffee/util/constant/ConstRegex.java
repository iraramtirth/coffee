package org.coffee.util.constant;

/**
 * 正则表达式常量
 * 
 * @author coffee<br>
 *         2013下午3:09:10
 */
public class ConstRegex {

	/**
	 * 匹配任意字符<br>
	 * 注意不能用([.\\n])解析不出为啥,很变态<br>
	 * (\\s\\S)还可以用(\\d\\D)等
	 */
	public static final String REGEX_ANY_CHAR = "(\\s\\S)";

	/**
	 * 用于处理URL的正则表达式
	 * 
	 * \\b(https?|ftp|file):\\/\\/[-A-Z0-9+&@#\\/%?=~_
	 * |!:,.;]*[-A-Z0-9+&@#\\/%=~_|]
	 * http://x.x.x.com/path/path/?name=value&name=value
	 */
	// 只匹配域名(不包含 http:// ftp://)
	private static final String REGEX_URL_ONLY_DOMAIN = "(ftp://|http://|https://)"
			+ "[0-9a-zA-Z]+[0-9a-zA-Z\\.\\-]*[0-9a-zA-Z:]+"
			+ "[#0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]*)"
			+ "|(www\\.[0-9a-zA-Z]+[0-9a-zA-Z\\.\\-]*[0-9a-zA-Z]+"
			+ "[#0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]*";
	// 带有通信协议 只匹配 以 ftp http:// https://开头的
	private static final String REGEX_URL_WITH_PROTOCOL = "\\b(https?|ftp|file):\\/\\/[-A-Z0-9+&@#\\/%?=~_|!:,.;]*[-A-Z0-9+&@#\\/%=~_|]";

	public static final String REGEX_URL = "(" + REGEX_URL_ONLY_DOMAIN + ")|("
			+ REGEX_URL_WITH_PROTOCOL + ")";

}
