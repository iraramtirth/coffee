package org.coffee.server.http;

import java.util.HashMap;
import java.util.Map;

/**
 *  
 * @author wangtao
 *
 **/
public class ServletRequest extends Request {
	
	private String uri;
	private String queryString;
	
	//
	private Map<String,String> paramsMap = new HashMap<String, String>();
	
	//
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
		//解析参数
		String[] ss = queryString.split("&");
		for(String s : ss){
			String[] kv = s.split("=");
			if(kv.length == 1){
				this.paramsMap.put(kv[0], "");
			}else{	
				this.paramsMap.put(kv[0], kv[1]);
			}
		}
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Map<String, String> getParamsMap() {
		return paramsMap;
	}
	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}
}
