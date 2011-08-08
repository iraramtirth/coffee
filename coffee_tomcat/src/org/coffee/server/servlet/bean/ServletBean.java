package org.coffee.server.servlet.bean;

/**
 * 
 * xml文件下对应的Servlet配置
 * 
 * @author wangtao
 */
public class ServletBean {
	/**
	 *  servlet 名
	 */
	private String name;
	/**
	 * servlet 类 
	 */
	private String clazz;
	/**
	 *  pattern
	 */
	private String pattern;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}