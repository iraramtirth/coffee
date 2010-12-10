package org.coffee.hibernate.dao.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.coffee.hibernate.annotation.Column;
import org.coffee.hibernate.annotation.Entity;
import org.coffee.hibernate.annotation.Id;
import org.coffee.hibernate.annotation.NullMap;
import org.coffee.hibernate.annotation.Table;
import org.coffee.hibernate.dao.util.Configuration.MappedType;

/**
 * 数据库的通用工具类
 * 适用于  mysql Oracle等数据库 
 * @author wangtao
 */
public class TDaoUtil {

	private static Logger logger = Logger.getLogger("jdbc");
	static{
		logger.setLevel(Level.INFO);
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
	public static <T> String getColumnName(Class<T> clazz,PropertyDescriptor prop) throws Exception{
		Field field = clazz.getDeclaredField(prop.getName());
		Column column = field.getAnnotation(Column.class);
		if(column != null){
			return column.value();
		}else{
			return prop.getName();
		}
	}
	/**
	 *  获取T的id
	 */
	public static <T> Object getObjectId(T t){
		Object id = null;
		try {
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (PropertyDescriptor p : props) {
				if (p.getName().equals("id")) {
					id = p.getReadMethod().invoke(t, (Object[]) null);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return id;
	}
	/**
	 *  格式化日期类型 
	 */
	public static String parseDate(Object value){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(value);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 判断指定class指定prop是否被映射到数据库
	 * @return 如果被映射，返回true  ； 没被映射， 即 nullMap != null 返回false
	 */
	public static <T> boolean isNullMap(Class<T> clazz, PropertyDescriptor prop) throws Exception{
		Field field = clazz.getDeclaredField(prop.getName());
		NullMap nullMap = field.getAnnotation(NullMap.class);
		if(nullMap != null){
			return true;
		}
		return false;
	}
	
	// 将ResultSet组装成List 
	public static <T> List<T> processResultSetToList(ResultSet rs,Class<T> clazz) throws Exception{
		List<T> ls = new ArrayList<T>();
		BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		while (rs.next()) {
			T tt = clazz.newInstance();
			for (PropertyDescriptor prop : props) {
				try {
					if(isNullMap(clazz, prop)){
						continue;
					}
					/**
					 * 如果Oracle数据库中的类型是 number
					 * p.getWriteMethod().invoke(tt, new Object[] { rs.getObject(p.getName()) })
					 * 会报如下错误
					 * java.lang.IllegalArgumentException: argument type mismatch
					 * ---
					 * 另外如果mysql数据库中的bigint
					 * 在进行setXxxx(Integer val)时候也会抛出该异常
					 * 也会抛出该异常
					 */ 
					Object value = null;
					switch(getMappedType(prop)){
						case Long : 
							value = Long.valueOf(rs.getLong(TDaoUtil.getColumnName(clazz, prop)));
							break;
						case Integer :
							value = Integer.valueOf(rs.getInt(TDaoUtil.getColumnName(clazz, prop)));
							break;
						case String :
							value = rs.getObject(TDaoUtil.getColumnName(clazz, prop));
							break;
					}
					prop.getWriteMethod().invoke(tt, new Object[] {value});
				} catch (IllegalArgumentException e) {
					System.out.print(prop.getName()+" ----	");
					System.out.println(rs.getInt(TDaoUtil.getColumnName(clazz, prop)));
					e.printStackTrace();
				} 
			}
			ls.add(tt);
		}
		return ls;
	}
	
	
	// 获取Field被映射的类型
	public static <T> MappedType getMappedType(PropertyDescriptor prop) throws Exception{
		if(prop.getPropertyType().getSimpleName().equals("Long")){
			return MappedType.Long;
		}
		if(prop.getPropertyType().getSimpleName().equals("Integer")){
			return MappedType.Integer;
		}
		if(prop.getPropertyType().getSimpleName().equals("Date")){
			return MappedType.Date;
		}
		return MappedType.String;
	}
	/**
	 * 判断某Class的某字段是不是主键
	 * @param clazz
	 * @param prop
	 * @return ：若是则返回true;否则返回false
	 */
	public static <T> boolean isPrimaryKey(Class<T> clazz, PropertyDescriptor prop) throws Exception{
		Id id = clazz.getDeclaredField(prop.getName()).getAnnotation(Id.class);
		if(id != null){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @param token
	 */
	public static <T> String getUpdateSql(T t,String dialect) throws Exception{
		String token = Configuration.getToken(dialect);
		StringBuffer sql = new StringBuffer("update ").append(TDaoUtil.getTableName(t.getClass())).append(" set ");
		long id = 0;
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			Field field = t.getClass().getDeclaredField(props[i].getName());
			NullMap nullMap = field.getAnnotation(NullMap.class);
			if(nullMap != null){
				continue;
			}
			Object value = null;
			if(TDaoUtil.isPrimaryKey(t.getClass(), props[i])){
				id = Long.valueOf(props[i].getReadMethod().invoke(t,(Object[]) null).toString());
			}else{
				switch(TDaoUtil.getMappedType( props[i])){
					case Integer :
					case Long : 
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						if (value != null) {
							sql.append(token).append(props[i].getName().toUpperCase()).append(token).append("=").append(value);
						} else {
							continue;
						}
						break;
					case Date :
						value = TDaoUtil.parseDate(props[i].getReadMethod().invoke(t,(Object[]) null));
					case String :
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						if ("null".equals(value) || null == value) {
							continue;
						} else {
							sql.append(token).append(TDaoUtil.getColumnName(t.getClass(), props[i]).toUpperCase()).append(token)
								.append("='").append(value.toString()).append("'");
						}
						break;
				}
			}
			if (value != null && i+1 < props.length) {
				sql.append(",");
			}
		}
		sql = new StringBuffer(sql.toString().trim());
		while (sql.toString().endsWith(",")) {// 除去末尾的 ,
			sql.deleteCharAt(sql.length());
		}
		sql.append(" where id = ").append(id);
		System.out.println(sql);
		return sql.toString();
	}
	
	// 获取插入记录的sql语句
	public static <T> String getInsertSql(T t,String dialect) throws Exception {
		String token = Configuration.getToken(dialect);
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer("insert into ").append(
				TDaoUtil.getTableName(t.getClass())).append(" ");
		
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		//k-v 映射的column名字 : 属性  LinkedHashMap 按照插入的顺序排序
		Map<String,PropertyDescriptor> propMap = new LinkedHashMap<String, PropertyDescriptor>();
		sql.append("(");
		
		for (int i = 0; i < props.length; i++) {
			Field field = t.getClass().getDeclaredField(props[i].getName());
			Column column = field.getAnnotation(Column.class);
			NullMap nullMap = field.getAnnotation(NullMap.class);
			if(nullMap != null){
				continue;
			}
			Id id = field.getAnnotation(Id.class);
			if(id != null){//主键
				if(Configuration.getDialect().contains("HSQLDB")){
					continue;
				}
			}
			if (column != null) {// 数字 1 键旁边的反引号；处理关键字
				sql.append(token).append(column.value().toUpperCase()).append(token);
				propMap.put(column.value(), props[i]);
			} else {
				sql.append(token).append(props[i].getName().toUpperCase()).append(token);
				propMap.put(props[i].getName(), props[i]);
			}
			if (i + 1 < props.length) {
				sql.append(",");
			}
		}
		sql.append(")values(");
		for (String column : propMap.keySet()) {
			PropertyDescriptor prop = propMap.get(column);
			Object value = "";
			if(TDaoUtil.isPrimaryKey(t.getClass(), prop)){
				if(dialect.toUpperCase().contains("MYSQL")){
					sql.append("null");	
				}
				else if(dialect.toUpperCase().contains("ORACLE")){
					sql.append(TDaoUtil.getSequenceName(t)+".nextval");
				}else{
					sql.append(TDaoUtil.getSequenceName(t)+".nextval");
				}
			}else{
				switch(TDaoUtil.getMappedType(prop)){
					case Integer :
					case Long :
						sql.append(prop.getReadMethod().invoke(t,(Object[]) null));
						break;
					case Date :
						value = TDaoUtil.parseDate(prop.getReadMethod().invoke(t,(Object[]) null));
						sql.append(null == value ? "null" : "'" + value.toString() + "'");
						break;
					case String :
						value = prop.getReadMethod().invoke(t,(Object[]) null);
						sql.append(null == value ? "null" : "'" + value.toString() + "'");
						break;
				}
			}
			sql.append(",");
		}
		sql.deleteCharAt(sql.length()-1);// 除去sql语句后面最后一个 逗号
		sql.append(")");
		long end = System.currentTimeMillis();
		logger.info("生成sql耗时 " + (end - start) + " ms");
		logger.info(sql.toString());
		return sql.toString();
	}
	
	public static <T> String getSequenceName(T t){
		if(t.getClass().getAnnotation(Entity.class) != null 
				&& t.getClass().getAnnotation(Table.class) != null){
			Table tableAnno = t.getClass().getAnnotation(Table.class); 
			return tableAnno.sequence().toLowerCase();
		}else{
			return ""; 
		}
	}
}
