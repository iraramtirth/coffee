package org.coffee.jdbc.table;

import java.beans.PropertyDescriptor;

import static org.coffee.jdbc.dao.util.TUtils.*;

/**
 * 自动生成sql语句
 * 
 * @author wangtao
 */
public class TableCreator {

	/**
	 * 生成建表语句
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static <T> String generateTableSql(Class<T> beanClass) {
		PropertyDescriptor[] props = getPropertyDescriptor(beanClass);
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + getTableName(beanClass) + "(\n");
		for (PropertyDescriptor prop : props) {
			sql.append("\t" + getColumnName(beanClass, prop));
			switch (getMappedType(prop)) {
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
		sql.deleteCharAt(sql.length() - 2);
		sql.append(")\n");
		return sql.toString();
	}

	public static void main(String[] args) {
		// Class<Fans> clazz = Fans.class;
		// PropertyDescriptor[] props = getPropertyDescriptor(clazz);
		// StringBuilder sql = new StringBuilder();
		// sql.append("create table "+getTableName(clazz)+"(\n");
		// for(PropertyDescriptor prop : props){
		// sql.append("\t"+getColumnName(clazz, prop));
		// switch(getMappedType(prop)){
		// case Integer:
		// sql.append(" int");
		// break;
		// case Date:
		// sql.append(" datetime");
		// break;
		// case String:
		// sql.append(" varchar(255)");
		// break;
		// }
		// sql.append(",\n");
		// }
		// sql.deleteCharAt(sql.length()-2);
		// sql.append(")\n");
		// System.out.println(sql);
	}
}
