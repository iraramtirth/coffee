package org.coffee.tools.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 注意 
 * @author 王涛
 */
@XmlRootElement
public class JaxbList {
	
	private List<XmlItem> resultList = new ArrayList<XmlItem>();

	
	public void add(XmlItem t){
		this.resultList.add(t);
	}
	
	/**
	 * 返回只读对象 
	 */
	@XmlElement(name="item")
	public List<XmlItem> getResultList() {
		List<XmlItem> copyList = new ArrayList<XmlItem>();
		copyList.addAll(resultList);
		return copyList;
	}
	
	public static class XmlItem{
		private int id;
		private String username;
		private String password;
		
		public XmlItem() {
		}
		public XmlItem(int id, String username, String password) {
			this.id = id;
			this.username = username;
			this.password = password;
		}
		@XmlAttribute(name="id")
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
}
