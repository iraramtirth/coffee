package org.coffee.hibernate.dao.util;

import org.coffee.hibernate.annotation.Entity;
import org.coffee.hibernate.annotation.Table;

/**
 * 数据库的通用工具类
 * 适用于  mysql Oracle等数据库 
 * @author wangtao
 */
public class TDaoUtil {

	public static <T> String getTableName(Class<T> clazz) {
		if (clazz.getAnnotation(Entity.class) != null
				&& clazz.getAnnotation(Table.class) != null) {
			// mysql 数据库对数据表名大小写敏感
			return clazz.getAnnotation(Table.class).name().toLowerCase();
		} else {
			return clazz.getSimpleName().toLowerCase();
		}
	}

}
