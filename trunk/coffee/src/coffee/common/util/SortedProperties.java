package coffee.common.util;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * 经过排序的Properties 按照Properties文件的key顺序排列
 * 
 * @author 王涛
 */
public class SortedProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<String> keys;

	public SortedProperties() {
		super();
		keys = new Vector<String>();
	}

	public Enumeration<String> propertyNames() {
		return keys.elements();
	}

	public Object put(String key, String value) {
		if (keys.contains(key)) {
			keys.remove(key);
		}

		keys.add(key);

		return super.put(key, value);
	}

	public Object remove(Object key) {
		keys.remove(key);

		return super.remove(key);
	}

}
