package org.coffee.hibernate.dao.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;

import org.coffee.hibernate.annotation.Column;

/**
 * TDdao 工具类
 * 
 * @author wangtao
 */
public class MysqlDaoUtil {
	/**
	 * 获取数据表的 表名
	 */
	
	// 获取插入记录的sql语句
	public static <T> String getInsertSql(T t) throws Exception {
		StringBuffer sql = new StringBuffer("insert into ").append(
				TDaoUtil.getTableName(t.getClass())).append(" ");

		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		sql.append("(");
		for (int i = 0; i < props.length; i++) {
			Column column = props[i].getReadMethod().getAnnotation(Column.class);
			if (column != null) {
				sql.append("`").append(column.name()).append("`");
			} else {
				sql.append("`").append(props[i].getName()).append("`");
			}
			if (i + 1 < props.length) {
				sql.append(",");
			}
		}
		sql.append(") values (");
		for (int i = 0; i < props.length; i++) {
			Object value = "";
			try {// 处理 数值类型
				if (isNumberType(props[i])) {
					if ("id".equals(props[i].getName())) {
						sql.append("null");
					} else {
						sql.append(props[i].getReadMethod().invoke(t,(Object[]) null));
					}
				} else { // 处理时间类型
					if (isDateType(props[i])) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						try {
							value = sdf.format(props[i].getReadMethod().invoke(t, (Object[]) null));
						} catch (Exception e) {
							value = "null";
						}
					} else {
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
					}
					sql.append("null".equals(value) ? "null" : "'" + value.toString() + "'");
				}
				if (i + 1 < props.length) {
					sql.append(",");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sql.append(")");
		System.out.println(sql.toString());
		return sql.toString();
	}

	public static <T> String getUpdateSql(T t) {
		StringBuffer sql = new StringBuffer("update ").append(
				t.getClass().getSimpleName().toLowerCase()).append(" set ");
		long id = 0;
		try {
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (int i = 0; i < props.length; i++) {
				Object value = null;
				if (isNumberType(props[i])) {
					// 处理 ID 主键
					if ("id".equals(props[i].getName())) {
						id = Long.valueOf(props[i].getReadMethod().invoke(t,(Object[]) null).toString());
					} else {
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						if (value != null) {
							sql.append(props[i].getName()).append(" = ").append(value);
						} else {
							continue;
						}
					}
				} else {
					if (isDateType(props[i])) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							value = sdf.format(props[i].getReadMethod().invoke(t, (Object[]) null));
						} catch (Exception e) {
							value = "null";
						}
					} else {
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
					}
					if ("null".equals(value) || null == value) {
						continue;
					} else {
						sql.append(props[i].getName()).append(" = '").append(value.toString()).append("' ");
					}
				}
				if (value != null && i + 1 < props.length) {
					sql.append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	// 判断是否是数字型
	private static boolean isNumberType(PropertyDescriptor prop) {
		String type = prop.getPropertyType().getSimpleName();
		if ("int".equals(type) || "Integer".equals(type) || "long".equals(type)
				|| "Long".equals(type)) {
			return true;
		}
		return false;
	}

	// 判断是否是时间类型
	private static boolean isDateType(PropertyDescriptor prop) {
		String type = prop.getPropertyType().getSimpleName();
		if ("Date".equals(type)) {
			return true;
		}
		return false;
	}
}
