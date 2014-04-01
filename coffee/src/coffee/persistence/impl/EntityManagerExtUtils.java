package coffee.persistence.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import coffee.common.util.DateUtils;
import coffee.common.util.TypeUtils;

/**
 * 数据库的通用工具类
 * 适用于  mysql Oracle等数据库 
 * @author coffee
 */
public class EntityManagerExtUtils {

	private static Logger log = Logger.getLogger("jdbc");
	static{
		log.setLevel(Level.INFO);
	}
	
	public static enum EntityProperty{
		PkName,		// 主键名
		PkValue,	// 主键值
		TableName	// 表名
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
			return column.name();
		}else{
			return prop.getName();
		}
	}
	/**
	 * 获取主键字段在数据表中映射的列名称
	 */
	public static <T> String getPrimaryKeyColumnName(Class<T> entityClass){
		for(Field field : entityClass.getDeclaredFields()){
			Id id = field.getAnnotation(Id.class);
			if(id != null){
				Column column = field.getAnnotation(Column.class);
				if(column != null){
					return column.name();
				}
			}
		}//默认返回id
		return "id";
	}
	/**
	 *  获取主键值
	 */
	public static <T> Object getPrimaryKeyValue(T t){
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
	 * 获取主键相关信息
	 * 遍历一次即可将全部相关信息获取
	 * 以此方式提高速度
	 */
	public static Map<EntityProperty,Object> getEntityProperty(Object entity){
		Map<EntityProperty,Object> map = new HashMap<EntityProperty, Object>();
		try {
			BeanInfo bi = Introspector.getBeanInfo(entity.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			Class<?> clazz = entity.getClass();
			//获取表名
			Table table = clazz.getAnnotation(Table.class);
			if(table != null){
				String name = table.name();
				if(name == null){
					name = clazz.getSimpleName();
				}
				map.put(EntityProperty.TableName, name);
			}
			for (PropertyDescriptor p : props) {
				//获取
				if(isPrimaryKey(clazz, p)){
					String pkName = p.getName();
					//主键名
					map.put(EntityProperty.PkName,pkName);
					Object pkValue = p.getReadMethod().invoke(entity, new Object[]{});
					//主键值
					map.put(EntityProperty.PkValue, pkValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 判断指定class指定prop是否被映射到数据库
	 * @return 如果被映射，返回true  ； 没被映射， 即 nullMap != null 返回false
	 */
	public static <T> boolean isTransient(Class<T> clazz, PropertyDescriptor prop) throws Exception{
		Field field = clazz.getDeclaredField(prop.getName());
		Transient nullMap = field.getAnnotation(Transient.class);
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
					if(isTransient(clazz, prop)){
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
					switch(TypeUtils.getMappedType(prop)){
						case Long : 
							value = Long.valueOf(rs.getLong(getColumnName(clazz, prop)));
							break;
						case Integer :
							value = Integer.valueOf(rs.getInt(getColumnName(clazz, prop)));
							break;
						case Date :
							value = rs.getDate(getColumnName(clazz, prop));
							break;
						default :
							value = rs.getObject(getColumnName(clazz, prop));
							break;
					}
					prop.getWriteMethod().invoke(tt, new Object[] {value});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} 
			}
			ls.add(tt);
		}
		return ls;
	}
	
	
	/**
	 * 判断某Class的某字段是不是主键
	 * 如果该注解被Id属性注解了；或者字段名为Id，则是主键
	 * @param clazz
	 * @param prop
	 * @return ：若是则返回true;否则返回false
	 */
	public static <T> boolean isPrimaryKey(Class<T> clazz, PropertyDescriptor prop) throws Exception{
		Id id = clazz.getDeclaredField(prop.getName()).getAnnotation(Id.class);
		if(id != null){
			return true;
		}else{//如果没有被Id注解过；则查看判断该字段名字是否是Id
			if("id".equals(prop.getName().toLowerCase())){
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @param token
	 */
	public static <T> String getUpdateSql(T t) throws Exception{
		StringBuffer sql = new StringBuffer("update ").append(EntityManagerExtUtils.getTableName(t.getClass())).append(" set ");
		long id = 0;
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			Field field = t.getClass().getDeclaredField(props[i].getName());
			Transient nullMap = field.getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			Object value = null;
			if(EntityManagerExtUtils.isPrimaryKey(t.getClass(), props[i])){
				id = Long.valueOf(props[i].getReadMethod().invoke(t,(Object[]) null).toString());
			}else{
				boolean bool = false;
				switch(TypeUtils.getMappedType( props[i])){
					case Integer :
					case Long : 
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						if (value != null) {
							sql.append(props[i].getName()).append("=").append(value);
						} else {
							continue;
						}
						break;
					case Date :
						value = DateUtils.format(props[i].getReadMethod().invoke(t,(Object[]) null));
						bool = true;
						break;
					case String :
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						bool = true;
						break;
				}
				if(bool){
					if ("null".equals(value) || null == value) {
						continue;
					} else {
						sql.append(getColumnName(t.getClass(), props[i]).toUpperCase())
							.append("='").append(value.toString()).append("'");
					}
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
	
	/**
	 *  获取插入记录的sql语句
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> String getInsertSql(T t) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer("insert into ").append(
				EntityManagerExtUtils.getTableName(t.getClass())).append(" ");
		
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		//k-v 映射的column名字 : 属性  LinkedHashMap 按照插入的顺序排序
		Map<String,PropertyDescriptor> propMap = new LinkedHashMap<String, PropertyDescriptor>();
		sql.append("(");
		
		for (int i = 0; i < props.length; i++) {
			Field field = t.getClass().getDeclaredField(props[i].getName());
			Column column = field.getAnnotation(Column.class);
			Transient trans = field.getAnnotation(Transient.class);
			if(trans != null){
				continue;
			}
			Id id = field.getAnnotation(Id.class);
			if(id != null){//主键
				// 主键生成策略
				GeneratedValue type = field.getAnnotation(GeneratedValue.class);
				switch(type.strategy()){
					case AUTO :
						break;
					case IDENTITY:
						continue; //跳出本次for循环
					case SEQUENCE:
						break;
					default : //AUTO
						break;
				}
			}
			if (column != null) {
				sql.append(column.name().toUpperCase());
				propMap.put(column.name(), props[i]);
			} else {
				sql.append(props[i].getName());
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
			if(EntityManagerExtUtils.isPrimaryKey(t.getClass(), prop)){
				GeneratedValue type = t.getClass().getDeclaredField(prop.getName())
					.getAnnotation(GeneratedValue.class);
				if(type == null){
					log.warning("没有为主键指定GeneratedValue");
					// 默认自动增长;主键值设置为null
					sql.append("null");
					sql.append(",");
					//跳过本次训话
					continue;
				}
				switch(type.strategy()){
					case IDENTITY:
						continue; //跳出本次for循环
					case SEQUENCE:
						sql.append(EntityManagerExtUtils.getSequenceName(t)+".nextval");
						break;
					default : //AUTO
						sql.append("null");
						break;
				}
			}else{
				switch(TypeUtils.getMappedType(prop)){
					case Integer :
					case Long :
						sql.append(prop.getReadMethod().invoke(t,(Object[]) null));
						break;
					case Date :
						value = DateUtils.format(prop.getReadMethod().invoke(t,(Object[]) null));
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
		log.info("生成sql耗时 " + (end - start) + " ms");
		log.info(sql.toString());
		return sql.toString();
	}
	/**
	 * 获取序列名称  ：仅适用于Oracle数据库
	 * @param <T>
	 * @param t
	 * @return  : 返回Entity指定的
	 * @throws Exception 
	 */
	public static <T> String getSequenceName(T t) throws Exception{
		for(Field field : t.getClass().getDeclaredFields()){
			if(field.getAnnotation(Id.class) != null){
				// 查找 GeneratedValue注解
				GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
				if(generatedValue != null){
					// String generatorName = generatedValue.generator();
					 //查找指定的sequenceGenerator
					 SequenceGenerator sequenceGenerator = field.getAnnotation(SequenceGenerator.class);
					 if(sequenceGenerator != null){
						 return sequenceGenerator.sequenceName();
					 }
				}else{//null
					throw new Exception("实体的主键未指定GeneratedValue");
				}
				break;
			}
		}
		// 默认返回实体名字+"_seq"
		return getTableName(t.getClass())+"_seq";
	}
}
