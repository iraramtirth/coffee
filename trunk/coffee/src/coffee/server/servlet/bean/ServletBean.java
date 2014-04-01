package coffee.server.servlet.bean;

/**
 * 
 * xml�ļ��¶�Ӧ��Servlet����
 * 
 * @author coffee
 */
public class ServletBean {
	/**
	 *  servlet ��
	 */
	private String name;
	/**
	 * servlet �� 
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