package org.bluetooth.adapter.bean;

/**
 * 聊天内容
 * 
 * @author Administrator
 */
public class ChatItemBean {
	private String name;		//聊天人姓名
	private String content;		//聊天内容
	
	public ChatItemBean(){
	}
	
	public ChatItemBean(String name, String content){
		this.name = name;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return this.name + ": " + this.content;
	}
}
