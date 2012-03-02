package org.coffee.seven.bean;

import coffee.util.sqlite.annotation.Bean;
import coffee.util.sqlite.annotation.Id;

/***
 * 热门关键字
 * @author coffee
 */
@Bean(name="coffee_keywords")
public class KeywordsBean {
	@Id(isAuto=true)
	private int id;
	private String name;
	
	public KeywordsBean(){
	}
	public KeywordsBean(String name){
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
