package coffee.util.sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import coffee.util.sqlite.annotation.Column;
import coffee.util.sqlite.annotation.Entity;
import coffee.util.sqlite.annotation.Id;
import coffee.util.sqlite.annotation.Table;
import coffee.util.sqlite.annotation.Transient;

import android.database.Cursor;


/**
 * 数据库的通用工具类
 * 适用于  mysql Oracle等数据库 
 * @author wangtao
 */
public class TUtils {

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
			Column column = field.getAnnotation(Column.class);
			String columnName = field.getName();
			if(column != null && !"".equals(column.name())){
				columnName = column.name();
			}
			sql.append("\t" + columnName);
			switch (TypeUtils.getMappedType(field.getType())) {
			case Integer:
				sql.append(" INTEGER");
				Id id = field.getAnnotation(Id.class);
				if(id != null){
					sql.append(" PRIMARY KEY AUTOINCREMENT");
				}
				break;
			case Date:
				sql.append(" DATETIME");
				break;
			case String:
				int len = 20;
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
	public static <T> String getTableName(Class<T> clazz) {
		if (clazz.getAnnotation(Entity.class) != null
				&& clazz.getAnnotation(Table.class) != null) {
			return clazz.getAnnotation(Table.class).name();
		} else {
			return clazz.getSimpleName().toLowerCase();
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
	public static <T> boolean isTransient(Class<T> clazz, String fieldName) throws Exception{
		Field field = clazz.getDeclaredField(fieldName);
		Transient nullMap = field.getAnnotation(Transient.class);
		if(nullMap != null){
			return true;
		}
		return false;
	}
	
	// 将ResultSet组装成List 
	public static <T> List<T> processResultSetToList(Cursor rs,Class<T> clazz) throws Exception{
		List<T> ls = new ArrayList<T>();
		Field[] fields = clazz.getDeclaredFields();
		while (rs.moveToNext()) {
			T tt = clazz.newInstance();
			for (Field field : fields) {
				try {
					if(isTransient(clazz, field.getName())){
						continue;
					}
					Object value = null;
					int columnIndex = rs.getColumnIndex(field.getName());
					try{
						switch(TypeUtils.getMappedType(field.getType())){
							case Long : 
								value = Long.valueOf(rs.getLong(columnIndex));
								break;
							case Integer :
								value = Integer.valueOf(rs.getInt(columnIndex));
								break;
							default:
								value = rs.getString(columnIndex);
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
					String methodName = "set" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
					Method method = clazz.getMethod(methodName, new Class[]{field.getType()});
					method.invoke(tt, new Object[] {value});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} 
			}
			ls.add(tt);
		}
		return ls;
	}
	
	
	// 单列查询
	public static List<String> processToStringList(Cursor rs){
		List<String> lst = new ArrayList<String>();
		while (rs.moveToNext()) {
			String val = rs.getString(0);
			lst.add(val);
		}
		return lst;
	}
	
	
	/**
	 * 判断某Class的某字段是不是主键
	 * 如果该注解被Id属性注解了；或者字段名为Id，则是主键
	 * @param clazz
	 * @param prop
	 * @return ：若是则返回true;否则返回false
	 */
	public static <T> boolean isPrimaryKey(Class<T> clazz, String fieldName) throws Exception{
		Id id = clazz.getDeclaredField(fieldName).getAnnotation(Id.class);
		if(id != null){
			return true;
		}else{//如果没有被Id注解过；则查看判断该字段名字是否是Id
			if("id".equals(fieldName.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	
	private static Method getReadMethod(Class<?> clazz, String fieldName){
		String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		Method method = null;
		try {
			method = clazz.getMethod(methodName, new Class[]{});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return method;
	}
	
	/**
	 * 返回更新实体的命令语句
	 * @param <T>
	 * @param t
	 * @param token
	 */
	public static <T> String getUpdateSql(T t) throws Exception{
		StringBuffer sql = new StringBuffer("update ").append(TUtils.getTableName(t.getClass())).append(" set ");
		long id = 0;
		Class<?> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Transient nullMap = fields[i].getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			Object value = getReadMethod(clazz, fields[i].getName()).invoke(t,new Object[]{});
			if(TUtils.isPrimaryKey(t.getClass(), fields[i].getName())){
				id = Long.valueOf(value.toString());
				continue;
			}else{
				boolean bool = false;//
				switch(TypeUtils.getMappedType( fields[i].getType())){
					case Integer :
					case Long : 
						if (value != null) {
							sql.append(fields[i].getName()).append("=").append(value);
						} else {
							continue;
						}
						break;
					case Date :
						value = DateUtils.format(value.toString());
						bool = true;
						break;
					case String :
						bool = true;
						break;
				}
				if(bool){
					if ("null".equals(value) || null == value) {
						continue;
					} else {
						sql.append(TUtils.getColumnName(t.getClass(), fields[i].getName()))
							.append("='").append(value.toString()).append("'");
					}
				}
			}
			if (value != null && fields.length > i) {
				sql.append(",");
			}
		}
		sql = new StringBuffer(sql.toString().trim());
		while (sql.toString().endsWith(",")) {// 除去末尾的 ,
			sql.deleteCharAt(sql.length()-1);
		}
		sql.append(" where id = ").append(id);
		return sql.toString();
	}
	
	// 获取插入记录的sql语句
	public static <T> String getInsertSql(T t) throws Exception {
		StringBuffer sql = new StringBuffer("insert into ").append(
				TUtils.getTableName(t.getClass())).append(" ");
		
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
			Object value = "";
			if(TUtils.isPrimaryKey(t.getClass(), field.getName())){
					sql.append("null");	
			}else{
				Method method =  getReadMethod(t.getClass(), field.getName());
				switch(TypeUtils.getMappedType(field.getType())){
					case Integer :
					case Long :
						sql.append(method.invoke(t,(Object[]) null));
						break;
					case Date :
						value = DateUtils.format(method.invoke(t,(Object[]) null));
						sql.append(null == value ? "null" : "'" + value.toString() + "'");
//						sql.append(" to_date('").append(value).append("','yyyy-MM-dd HH24:mi:ss') ");
						break;
					case String :
						value = method.invoke(t,(Object[]) null);
						sql.append(null == value ? "null" : "'" + value.toString() + "'");
						break;
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
			FormFile,	//该类型用于文件上传
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
			}else if(type.contains("date")){
				return Type.Date;
			}else if(type.contains("string")){
				return Type.String;
			}
			return Type.Object;
		}
		
	}
}
