package org.coffee.server.http;

import java.util.StringTokenizer;

/**
 * 解析http请求
 * @author wangtao 
 */
public class RequestParser {
	
	/**
	 * 解析 
	 */
	public static Request parse(String requestData){
		Request request = new Request();
		//System.out.println(requestData);
		StringTokenizer stk = new StringTokenizer(requestData);
		if(stk.hasMoreTokens()){
			request.setType(stk.nextToken());
			request.setUri(stk.nextToken());
			request.setProtocol(stk.nextToken());
		}
		return request;
	}
	
	public static void main(String[] args) {
		String requestData = 
				"GET / HTTP/1.1 " +
				"Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/QVOD, application/QVOD, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, "+
				"Accept-Language: zh-cn"+
				"Accept-Encoding: gzip, deflate"+
				"User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; CIBA; TheWorld)"+
				"Host: localhost:8080"+
				"Connection: Keep-Alive";
		
		StringTokenizer stk = new StringTokenizer(requestData);
		System.out.println(stk.countTokens());
		while(stk.hasMoreTokens()){
			System.out.println(stk.nextToken());
			//request.setType(stk.nextToken());
			//request.setUri(stk.nextToken());
			//request.setProtocol(stk.nextToken());
		}
	}
	
}
