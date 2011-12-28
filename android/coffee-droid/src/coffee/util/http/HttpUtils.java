package coffee.util.http;

public class HttpUtils {
	/**
	 * 解析url
	 * 将URl转化成标准的格式  http://domain
	 * 如果以ftp开头则忽略
	 */
	public static String parserUrl(String url){
		if(url == null){
			return url;
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
	
}
