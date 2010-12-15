package cn.demo.bean;

import java.util.Date;

import org.coffee.hibernate.annotation.Entity;
import org.coffee.hibernate.annotation.Id;
import org.coffee.hibernate.annotation.Table;

@Entity
@Table(name="users")
public class User {
	@Id	// 主键
	private Integer id;
	private String username;
	private String password;
	private Integer age;
	private Date birthday;
	private String describe;
	
	private User child;
	public User getChild() {
		return child;
	}
	public void setChild(User child) {
		this.child = child;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
