package org.coffee.jdbc.table;

import static org.coffee.jdbc.dao.util.TUtils.getColumnName;
import static org.coffee.jdbc.dao.util.TUtils.getMappedType;
import static org.coffee.jdbc.dao.util.TUtils.getTableName;

import java.lang.reflect.Field;

import cn.demo.bean.User;

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
		Field[] fields = beanClass.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + getTableName(beanClass) + "(\n");
		for (Field field : fields) {
			sql.append("\t" + getColumnName(beanClass, field.getName()));
			switch (getMappedType(field.getType())) {
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
		System.out.println(sql.toString());
		return sql.toString();
	}

	public static void main(String[] args) {
		generateTableSql(User.class);
	}
}
