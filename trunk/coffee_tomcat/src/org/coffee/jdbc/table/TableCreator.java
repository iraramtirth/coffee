package org.coffee.jdbc.table;

import static org.coffee.jdbc.dao.util.TUtils.getColumnName;
import static org.coffee.jdbc.dao.util.TUtils.getMappedType;
import static org.coffee.jdbc.dao.util.TUtils.getTableName;

import java.lang.reflect.Field;

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

//	/**
//	 * 生成建表语句
//	 * @param <T>
//	 * @param beanClass
//	 * @return
//	 */
//	public static <T> String generateTableSql(Class<T> beanClass) {
//		Field[] fields = beanClass.getDeclaredFields();
//		StringBuilder sql = new StringBuilder();
//		sql.append("CREATE TABLE IF NOT EXISTS " + getTableName(beanClass) + "(\n");
//		for (Field field : fields) {
//			Transient nullMap = field.getAnnotation(Transient.class);
//			if(nullMap != null){
//				continue;
//			}
//			//非基本数据类型 ， 也非String类型
//			if(!field.getType().isPrimitive() &&
//					field.getType() != String.class){
//				continue;
//			}
//			Column column = field.getAnnotation(Column.class);
//			String columnName = field.getName();
//			if(column != null && !"".equals(column.name())){
//				columnName = column.name();
//			}
//			sql.append("\t" + columnName);
//			switch (TypeUtils.getMappedType(field.getType())) {
//			case Integer:
//			case Long:
//				sql.append(" INTEGER");
//				Id id = field.getAnnotation(Id.class);
//				if(id != null){
//					sql.append(" PRIMARY KEY");
//					GeneratedValue gen = field.getAnnotation(GeneratedValue.class);
//					if(gen != null && gen.strategy() == GenerationType.AUTO){
//						sql.append(" AUTOINCREMENT ");
//					}
//				}
//				break;
//			case Float:
//				sql.append(" FLOAT ");
//				break;
//			case Date:
//				sql.append(" DATETIME");
//				break;
//			case String:
//				int len = 255;
//				if(column != null){
//					len = column.length();
//				}
//				sql.append(" VARCHAR("+len+")");
//				break;
//			}
//			sql.append(",\n");
//		}
//		sql.deleteCharAt(sql.length() - 2);
//		sql.append(")\n");
//		return sql.toString();
//	}
	
	public static void main(String[] args) {
		generateTableSql(AdminBean.class);
	}
}
