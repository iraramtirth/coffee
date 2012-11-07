package coffee.database.table;

import static coffee.jdbc.dao.util.TUtils.getTableName;

import java.lang.reflect.Field;

import coffee.jdbc.annotation.Column;
import coffee.jdbc.annotation.Id;
import coffee.jdbc.annotation.Transient;
import coffee.jdbc.dao.util.TUtils;
import coffee.jdbc.dao.util.TypeUtils;

/**
 * 自动生成sql语句
 * 
 * @author wangtao
 */
public class TableCreator {

	/**
	 * 生成建表语句
	 * 
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static <T> String generateTableSql(Class<T> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS " + getTableName(beanClass)
				+ "(\n");
		for (Field field : fields) {
			Transient nullMap = field.getAnnotation(Transient.class);
			if (nullMap != null) {
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			String columnName = field.getName();
			if (column != null && !"".equals(column.name())) {
				columnName = column.name();
			}
			sql.append("\t" + columnName);
			switch (TypeUtils.getMappedType(field.getType())) {
			case Integer:
			case Long:
				sql.append(" INTEGER");
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					sql.append(" PRIMARY KEY");
					if (id != null && id.isAuto()) {
						sql.append(" AUTOINCREMENT ");
					}
				}
				break;
			case Float:
				sql.append(" FLOAT ");
				break;
			case Date:
				sql.append(" DATETIME");
				break;
			case String:
				int len = 255;
				if (column != null) {
					len = column.length();
				}
				sql.append(" VARCHAR(" + len + ")");
				break;
			default:
				break;
			}

			// 非基本数据类型 ， 也非String类型
			if (TUtils.isField(clazz, fieldName)) {
				sql.append(" PRIMARY KEY");
			}

			sql.append(",\n");
		}
		sql.deleteCharAt(sql.length() - 2);
		sql.append(")\n");
		return sql.toString();
	}

	public static void main(String[] args) {
		// generateTableSql(User.class);
	}
}
