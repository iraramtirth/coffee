package coffee.browser.http;

/**
 * webview的相关工具类
 * @author coffee
 */
public class WebViewUtils {
	
	
	protected static String metaLow = "<meta name=\"viewport\" "
		+ " content=\"user-scalable=0; width=device-width; "
		+ " initial-scale=0.75; maximum-scale=1.5; target-densitydpi=low-dpi\" />";

	protected static String metaMedium = "<meta name=\"viewport\" "
		+ " content=\"user-scalable=0; width=device-width; "
		+ " initial-scale=1; maximum-scale=1.5; target-densitydpi=medium-dpi\" />";
	
	protected static String metaHign = "<meta name=\"viewport\" "
		+ " content=\"user-scalable=0; width=device-width; "
		+ " initial-scale=1.5; maximum-scale=1.5; target-densitydpi=hign-dpi\" />";
	
	protected static String cssRemoveHighLight = "* { "+
		" -webkit-tap-highlight-color: rgba(0, 0, 0, 0); "+	//去掉高亮的边框
		" }";
	
	/**
	 * 解析url
	 * 将URl转化成标准的格式  http://domain
	 * 如果以ftp开头则忽略
	 */
	public static String parserUrl(String base, String url){
		if(url == null){
			return url;
		}
		if(url.startsWith("/")){
			url = base + url;
		}
		if(url.startsWith("http://")){
			return url; 
		}
		if(url.startsWith("ftp://")){
			return url;
		}
		url = "http://" + url;
		return url;
	}
	
	private final static String digits = "0123456789ABCDEF";
	// doc = doc.replace("#", "%23");
	// doc = doc.replace("%", "%25");
	// doc = doc.replace("\\", "%27");
	// doc = doc.replace("?", "%3f");
	/**
	 * webView.loadData(data,....)
	 * 第一个参数官方给的解释
	 * data A String of data in the given encoding. 
	 * The date must be URI-escaped -- '#', '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
	 * 但是搞不懂为啥要这样编码
	 */
	public static String encode(String s) {
		// Guess a bit bigger for encoded form
		StringBuilder buf = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| (ch >= '0' && ch <= '9') || ".-*_".indexOf(ch) > -1) { //$NON-NLS-1$   
				buf.append(ch);
			} else {
				byte[] bytes = new String(new char[] { ch }).getBytes();
				for (int j = 0; j < bytes.length; j++) {
					buf.append('%');
					buf.append(digits.charAt((bytes[j] & 0xf0) >> 4));
					buf.append(digits.charAt(bytes[j] & 0xf));
				}
			}
		}
		return buf.toString();
	}
}
