package org.coffee.server.http;


/**
 * http 请求信息 
 * 
 * @author wangtao
 */
public class Request {
	/**
	 * 请求方式 GET POST ...
	 */
	private String type;
	/**
	 * 请求路径
	 */
	private String uri;
	/**
	 *  协议版本
	 */
	private String protocol;
	/**
	 * Accept
	 */
	private String accept;
	/**
	 *  Accept-Language
	 */
	private String acceptLanguage;
	/**
	 * Accept-Encoding
	 */
	private String acceptEncoding;
	/**
	 * userAgent
	 */
	private String userAgent;
	/**
	 * 主机 Host
	 */
	private String host;
	/**
	 * connection
	 */
	private String connection;
	
	//
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getAccept() {
		return accept;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	public String getAcceptLanguage() {
		return acceptLanguage;
	}
	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}
	public String getAcceptEncoding() {
		return acceptEncoding;
	}
	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getConnection() {
		return connection;
	}
	public void setConnection(String connection) {
		this.connection = connection;
	}
	
}
