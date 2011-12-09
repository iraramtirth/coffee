/*
 * Created on 2009-10-23
 *
 */
package org.coffee.jdbc.table;

/**
 * 作者：李北金
 * 
 * 创建日期：2009-10-23
 * 
 * 说明：管理员，先写死，数据库里不建表
 * 
 * wangtao修改..
 */
public class AdminBean {
    public int id;
    public String username;
    public String password;
    
    public String fr;	//可以查询的渠道号
    
    public final String tableName = "mmb_user";
    
    public AdminBean(){
    }
	public AdminBean(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

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
	public String getFr() {
		return fr;
	}
	public void setFr(String fr) {
		this.fr = fr;
	}
}
