package org.coffee.tools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 注意 
 * @author 王涛
 */
@XmlRootElement
public class JaxbList<T> {
	
	private List<T> resultList = new ArrayList<T>();

	public void add(T t){
		this.resultList.add(t);
	}
	
	/**
	 * 返回只读对象 
	 */
	@XmlElement(name="item")
	public List<T> getResultList() {
		List<T> copyList = new ArrayList<T>();
		copyList.addAll(resultList);
		return copyList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	
}
