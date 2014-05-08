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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((json == null) ? 0 : json.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridDataBean other = (GridDataBean) obj;
		if (json == null) {
			if (other.json != null)
				return false;
		} else if (!json.equals(other.json))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
