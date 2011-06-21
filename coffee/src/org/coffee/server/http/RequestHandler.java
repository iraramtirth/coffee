package org.coffee.server.http;


/**
 * 处理 Request请求
 * 
 * @author wangtao
 */
public class RequestHandler{
	

	/**
	 *  requestInfo
	 *
	 	GET /index.jsp?query=dfdfdf HTTP/1.1
		Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/QVOD, application/QVOD, 
		Accept-Language: zh-cn
		Accept-Encoding: gzip, deflate
		User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; CIBA; TheWorld)
		Host: localhost:8080
		Connection: Keep-Alive
	 */
	public static String handle(Request req) {
		ServletRequest request = new ServletRequest();
		String  url = req.getUrl();
		String[] strs = url.split("\\?");
		request.setUri(strs[0]);
		if(strs.length > 1){
			request.setQueryString(strs[1]);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 200 OK\n");//注意每行后面有一个 \n
		sb.append("\n");
		//String sql = 
		sb.append("hello world");
		return sb.toString();
	}
	
	
}