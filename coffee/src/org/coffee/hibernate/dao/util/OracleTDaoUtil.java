package org.coffee.hibernate.dao.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;

import org.coffee.hibernate.annotation.Column;
import org.coffee.hibernate.annotation.Entity;
import org.coffee.hibernate.annotation.Table;

public class OracleTDaoUtil {
	// 反射获取实体的注解信息：表名
	public static <T> String getTableName(T t){
		if(t.getClass().getAnnotation(Entity.class) != null 
				&& t.getClass().getAnnotation(Table.class) != null){
			Table tableAnno = t.getClass().getAnnotation(Table.class); 
			 return tableAnno.name().toLowerCase();
			
		}else{
			return t.getClass().getSimpleName().toLowerCase(); 
		}
	}
	// 获取 序列名
	public static <T> String getSequenceName(T t){
		if(t.getClass().getAnnotation(Entity.class) != null 
				&& t.getClass().getAnnotation(Table.class) != null){
			Table tableAnno = t.getClass().getAnnotation(Table.class); 
			return tableAnno.sequence().toLowerCase();
		}else{
			return ""; 
		}
	}
	
	public static <T> String getInsertSql(T t) throws Exception{
		StringBuffer sql = new StringBuffer("insert into ").append(OracleTDaoUtil.getTableName(t)).append(" ");
		
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		
		sql.append(" (");
		for (int i = 0; i < props.length; i++) {
			Column column = props[i].getReadMethod().getAnnotation(Column.class);
			if(column != null){
				sql.append(column.name());
			}else{
				sql.append(props[i].getName());
			}
			if (i + 1 < props.length) {
				sql.append(",");
			}
		}
		sql.append(") values (");
		for (int i = 0; i < props.length; i++) {
			try {
				if ("Integer".equals(props[i].getPropertyType().getSimpleName())
						|| "Long".equals(props[i].getPropertyType().getSimpleName())) {
					if ("id".equals(props[i].getName())) {
						sql.append(OracleTDaoUtil.getSequenceName(t)+".nextval");
					} else {
						sql.append(props[i].getReadMethod().invoke(t,
								(Object[]) null));
					}
				} else if (props[i].getPropertyType().getCanonicalName()
						.endsWith("Date")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String value = "";
					try {
						value = sdf.format(props[i].getReadMethod().invoke(t, (Object[]) null));
					} catch (Exception e) {
						value = "null";
					}// 如果时间为空
					if ("null".equals(value)) {
						sql.append(value);
					} else { //HH:mi:ss
						/**
						 * 可能是像Java那样指定日期格式，比如：
							to_date('2006-06-01 18:00:00' 'yyyy-mm-dd hh:MM:ss')
							而在Oracle中的日期格式是不区分大小写的,所以 mm 出现了两次。
							正确的写法是：
							to_date('2006-06-01 18:00:00' 'yyyy-mm-dd hh:mi:ss')
						 */
						sql.append(" to_date('").append(value).append("','yyyy-MM-dd HH24:mi:ss') ");
					}
				} else {
					Object value = props[i].getReadMethod().invoke(t,
							(Object[]) null);
					if (value == null) {
						sql.append("null");
					} else {
						sql.append(" '").append(value.toString()).append(
								"' ");
					}
				}
				if (i + 1 < props.length) {
					sql.append(",");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sql.append(" )");
		return sql.toString();
	}

	public static <T> String getUpdateSql(T t) throws Exception{
		StringBuffer sql = new StringBuffer("update ").append(TDaoUtil.getTableName(t.getClass())).append(" set ");
		long id = 0;
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			try {
				Object value = null;
				if ("Integer".equals(props[i].getPropertyType().getSimpleName())
						|| "Long".equals(props[i].getPropertyType().getSimpleName())) {
					// 处理 ID 主键
					if ("id".equals(props[i].getName())) {
						id = Long.valueOf(props[i].getReadMethod().invoke(
								t, (Object[]) null).toString());
					} else {
						value = props[i].getReadMethod().invoke(t,
								(Object[]) null);
						if (value != null) {
							sql.append(props[i].getName()).append(" = ")
									.append(value);
						} else {
							continue;
						}
					}
				} else if (props[i].getPropertyType().getCanonicalName()
						.endsWith("Date")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						value = sdf.format(props[i].getReadMethod().invoke(t, (Object[]) null));
					} catch (Exception e) {
						value = "null";
					}
					if ("null".equals(value) || null == value) {
						continue;
					} else {// mysql 
						sql.append(props[i].getName()).append(" = ").append("to_date('").append(value).append("','yyyy-MM-dd HH24:mi:ss') ");
					}
				} else {
					value = props[i].getReadMethod().invoke(t,(Object[]) null);
					if (value == null) {
						continue;
					} else {
						sql.append(props[i].getName()).append(" = '").append(value.toString()).append("' ");
					}
				}
				if (value != null && i + 1 < props.length) {
					sql.append(",");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sql.trimToSize();
		sql = new StringBuffer(sql.toString().trim());
		// 除去末尾的 ,
		while (sql.toString().endsWith(",")) {
			sql.deleteCharAt(sql.length());
		}
		sql.append("where id = ").append(id);
		return sql.toString();
}

	

}