package com.zhutou;

import coffee.util.database.annotation.Bean;

@Bean(name="graduate")
public class GraduateBean {
	//学号
	private String id;
	//姓名
	private String name;
	//专业方向
	private String edu;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEdu() {
		return edu;
	}
	public void setEdu(String edu) {
		this.edu = edu;
	}
}
