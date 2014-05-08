package coffee.frame.game2048.bean;

import java.io.Serializable;

/**
 * 数据
 * 
 * @author coffee<br>
 *         2014年5月8日上午11:02:39
 */
public class GridDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888897373848200108L;
	private String value;
	private String json;

	public GridDataBean(String value, String json) {
		this.value = value;
		this.json = json;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
