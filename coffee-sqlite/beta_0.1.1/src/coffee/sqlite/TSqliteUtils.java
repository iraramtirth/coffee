package coffee.sqlite;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import coffee.sqlite.annotation.Bean;
import coffee.sqlite.annotation.Column;
import coffee.sqlite.annotation.Id;
import coffee.sqlite.annotation.Transient;
import coffee.util.common.TUtils;


/**
 * 数据库的通用工具类
 * 适用于  mysql Oracle等数据库 
 * @author wangtao
 */
public class TSqliteUtils extends TUtils{

	private static Logger logger = Logger.getLogger("jdbc");
	
	static{
		logger.setLevel(Level.INFO);
	}
	
	/**
	 * 生成建表语句
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static <T> String generateTableSql(Class<T> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS " + getTableName(beanClass) + "(\n");
		for (Field field : fields) {
			Transient nullMap = field.getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			//非基本数据类型 ， 也非String类型
			if(!field.getType().isPrimitive() &&
					field.getType() != String.class){
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			String columnName = field.getName();
			if(column != null && !"".equals(column.name())){
				columnName = column.name();
			}
			sql.append("\t" + columnName);
			switch (TypeUtils.getMappedType(field.getType())) {
			case Integer:
			case Long:
				sql.append(" INTEGER");
				Id id = field.getAnnotation(Id.class);
				if(id != null){
					sql.append(" PRIMARY KEY");
					if(id.isAuto()){
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
				if(column != null){
					len = column.length();
				}
				sql.append(" VARCHAR("+len+")");
				break;
			}
			sql.append(",\n");
		}
		sql.deleteCharAt(sql.length() - 2);
		sql.append(")\n");
		return sql.toString();
	}
	
	
	/**
	 *  获取表名
	 */
	public static <T> String getTableName(Class<T> beanClass) {
		Bean bean = beanClass.getAnnotation(Bean.class);
		if (bean != null) {
			return bean.name();
		} else {
			return beanClass.getSimpleName().toLowerCase();
		}
	}
	/**
	 *  获取列名 
	 */
	public static <T> String getColumnName(Class<T> clazz,String fieldName){
		Column column = getColumn(clazz,fieldName);
		if(column != null && column.name().length() > 0){
			return column.name();
		}else{
			return fieldName;
		}
	}
	/**
	 * 获取列的长度
	 * @param clazz : 类
	 * @param prop	: 属性
	 */
	public static <T> int getColumnLength(Class<T> clazz,String fieldName){
		return getColumn(clazz, fieldName).length();
	}
	
	private static <T> Column getColumn(Class<T> clazz, String fieldName){
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Column column = field.getAnnotation(Column.class);
		return column;
	}
	
	/**
	 * 判断列是否可为空
	 * return : false-不可为空 ; true-可为空
	 */
	public static <T> boolean isNull(Class<T> clazz, String fieldName){
		Column column = getColumn(clazz, fieldName);
		if(column != null){
			return column.nullable();
		}else{
			return true;
		}
	}
	
	/**
	 * 判断指定class的prop是否被映射到数据库
	 * @return 如果被映射，返回true  ； 没被映射， 即 nullMap != null 返回false
	 */
	public static <T> boolean isTransient(Class<T> clazz, String fieldName){
		try {
			Field field = clazz.getDeclaredField(fieldName);
			Transient nullMap = field.getAnnotation(Transient.class);
			if(nullMap != null){
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 将ResultSet组装成List 
	public static <T> List<T> processResultSetToList(ResultSet rs,Class<T> clazz) throws Exception{
		List<T> ls = new ArrayList<T>();
		Field[] fields = clazz.getDeclaredFields();
		while (rs.next()) {
			T tt = clazz.newInstance();
			for (Field field : fields) {
				try {
					if(isTransient(clazz, field.getName())){
						continue;
					}
					Object value = null;
//					int columnIndex = rs.getColumnIndex(field.getName());
					String columnName = getColumnName(clazz, field.getName());
					try{
						switch(TypeUtils.getMappedType(field.getType())){
							case Long : 
								value = Long.valueOf(rs.getLong(columnName));
								break;
							case Integer :
								value = Integer.valueOf(rs.getInt(columnName));
								break;
							default:
								value = rs.getString(columnName);
								break;
						}
					}catch(Exception e){//如果仅仅查询Class的部分字段
						if(e.getMessage().matches("Column\\s+'.+?'\\s+not\\s+found.")){
							switch(TypeUtils.getMappedType(field.getType())){
								case Long : 
								case Integer: 
									value = 0; break;
								default :
									value = null;
									break;
							}
						}
					}
					setValue(tt, field.getName(), value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} 
			}
			ls.add(tt);
		}
		return ls;
	}
	
	
	// 单列查询
	public static List<String> processToStringList(ResultSet rs){
		List<String> lst = new ArrayList<String>();
		try{
			while (rs.next()) {
				String val = rs.getString(0);
				lst.add(val);
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return lst;
	}
	
	
	/**
	 * 判断某Class的某字段是不是主键
	 * 如果该主键必须被Id属性注解
	 * @param entityClass  : 实体类
	 * @param fieldName : 字段名
	 */
	public static <T> boolean isPrimaryKey(Class<T> entityClass, String fieldName){
		try {
			Id id = entityClass.getDeclaredField(fieldName).getAnnotation(Id.class);
			if(id != null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断主键是否是自增类型的
	 * @param entityClass  : 实体类
	 * @param fieldName : 字段名
	 */
	public static <T> boolean isGenerationTypeAuto(Class<T> entityClass, String fieldName){
		try {
			if(isPrimaryKey(entityClass, fieldName) == false){
				return false;
			}
			Id id = entityClass.getDeclaredField(fieldName).getAnnotation(Id.class);
			if(id != null && id.isAuto()){
				return true;
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 返回更新实体的命令语句
	 * @param <T>
	 * @param t
	 * @param token
	 */
	public static <T> String getUpdateSql(T t) throws Exception{
		StringBuffer sql = new StringBuffer("update ").append(TSqliteUtils.getTableName(t.getClass())).append(" set ");
		long id = 0;
		Class<?> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Transient nullMap = fields[i].getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			String columnName = getColumnName(clazz, fields[i].getName());
			Object value = getValue(t, fields[i].getName());
			if(TSqliteUtils.isPrimaryKey(t.getClass(), fields[i].getName())){
				id = Long.valueOf(value.toString());
				continue;
			}else{
				if (value == null) {
					continue;// 忽略空值，如果想赋null值， 字符串写成 fieldName=""; 数值型 fieldName=0
				}
				switch(TypeUtils.getMappedType( fields[i].getType())){
					case Integer :
					case Long : 
					case Float:
					case Double:
						sql.append(columnName).append("=").append(value);
						break;
					case Date :
						value = DateUtils.format(value.toString());
						sql.append(columnName).append("='").append(value).append("'");
						break;
					case String :
						sql.append(columnName).append("='").append(value).append("'");
						break;
					default:
						continue;
				}
				if (value != null && fields.length > i) {
					sql.append(",");
				}
			}
		}
		if (sql.toString().endsWith(",")) {// 除去末尾的 ,
			sql.deleteCharAt(sql.length()-1);
		}
		sql.append(" where id = ").append(id);
		return sql.toString();
	}
	
	// 获取插入记录的sql语句
	public static <T> String getInsertSql(T t) throws Exception {
		StringBuffer sql = new StringBuffer("insert into ").append(
				TSqliteUtils.getTableName(t.getClass())).append(" ");
		
		Field[] fields = t.getClass().getDeclaredFields();
		//k-v 映射的column名字 : 属性  LinkedHashMap 按照插入的顺序排序
		Map<String,Field> propMap = new LinkedHashMap<String, Field>();
		sql.append("(");
		
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Transient nullMap = field.getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			//非基本数据类型 ， 也非String类型
			if(!field.getType().isPrimitive() &&
					field.getType() != String.class){
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			String columnName = field.getName();
			if (column != null && !"".equals(column.name())) {
				columnName = column.name();
			}
			sql.append(columnName);
			propMap.put(columnName, fields[i]);
			if (i + 1 < fields.length) {
				sql.append(",");
			}
		}
		sql.append(")values(");
		for (String column : propMap.keySet()) {
			Field field = propMap.get(column);
			if(isPrimaryKey(t.getClass(), field.getName())&&
					isGenerationTypeAuto(t.getClass(), field.getName())){
				sql.append("null");	
			}else{
				Object fieldValue = getValue(t, field.getName());
				switch(TypeUtils.getMappedType(field.getType())){
					case Integer :
					case Long :
					case Float:
					case Double:
						sql.append(fieldValue);
						break;
					case Date :
						fieldValue = DateUtils.format(fieldValue);
						sql.append(null == fieldValue ? "null" : "'" + fieldValue + "'");
						break;
					case String :
						sql.append(null == fieldValue ? "null" : "'" + fieldValue + "'");
						break;
					default :
						continue;
				}
			}
			sql.append(",");
		}
		sql.deleteCharAt(sql.length()-1);// 除去sql语句后面最后一个 逗号
		sql.append(")");
		return sql.toString();
	}
	
	
	/**
	 *  解析日期，返回string 
	 */
	public static String parseDate(Object value){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(value);
		} catch (Exception e) {
			e.printStackTrace();
			//如果发生异常则回返null
			return null;
		}
	}

	static class TypeUtils {
		public enum Type {
			Byte,
			Character,
			Short,
			Integer,
			Long,
			Float,
			Double,
			Boolean,
			String,
			Date,
			Object
		}
		/**
		 * 支持基本数据类型以及其封装类型
		 * @param clazz ： 传入 field.getType对象
		 */
		public static Type getMappedType(Class<?> fieldType){
			String type = fieldType.getSimpleName().toLowerCase();
			if(type.contains("long")){
				return Type.Long;
			}else if(type.contains("int")){
				return Type.Integer;
			}else if(type.contains("float")){
				return Type.Float;
			}else if(type.contains("double")){
				return Type.Double;
			}else if(type.contains("date")){
				return Type.Date;
			}else if(type.contains("string")){
				return Type.String;
			}
			return Type.Object;
		}
		
	}
}
