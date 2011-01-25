package org.coffee.jdbc.table;

import static org.coffee.jdbc.dao.util.TUtils.getColumnName;
import static org.coffee.jdbc.dao.util.TUtils.getMappedType;
import static org.coffee.jdbc.dao.util.TUtils.getPropertyDescriptor;
import static org.coffee.jdbc.dao.util.TUtils.getTableName;

import java.beans.PropertyDescriptor;

import cn.demo.bean.User;


/**
 * 自动生成sql语句 
 * @author wangtao
 */
public class Creater {
	
	public static void main(String[] args) {
		Class<User> clazz = User.class;
		PropertyDescriptor[] props = getPropertyDescriptor(clazz);
		StringBuilder sql = new StringBuilder();
		sql.append("create table "+getTableName(clazz)+"(\n");
		for(PropertyDescriptor prop : props){
			sql.append("\t"+getColumnName(clazz, prop));
			switch(getMappedType(prop)){
				case Integer: 
					sql.append(" int");
					break;
				case Date:
					sql.append(" datetime");
					break;
				case String:
					sql.append(" varchar(255)");
					break;
			}
			sql.append(",\n");
		}
		sql.deleteCharAt(sql.length()-2);
		sql.append(")\n");
		System.out.println(sql);
	}
}
