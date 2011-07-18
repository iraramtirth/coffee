package org.coffee.jdbc.dao.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.coffee.jdbc.dao.util.Configuration.DialectType;
import org.coffee.jdbc.dao.util.Configuration.MappedType;


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
		if(column != null){
			return column.name();
		}else{
			return fieldName;
		}
	}
	/**
	 * 获取序列名；只适用于oracle数据库
	 * @param t
	 */
	public static <T> String getSequenceName(Class<T> clazz){
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			SequenceGenerator seq = field.getAnnotation(SequenceGenerator.class);
			if(seq != null){
				return seq.sequenceName();
			}
		}
		return null;
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
	 * 获取PropertyDescriptor[]对象
	 * @param clazz : 对象
	 */
	public static <T> PropertyDescriptor[] getPropertyDescriptor(Class<T> clazz){
		BeanInfo bi = null;
		try {
			bi = Introspector.getBeanInfo(clazz, Object.class);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		return props;
	}
	
	/**
	 * 判断指定class的prop是否被映射到数据库
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
					try{
						String fieldName = TUtils.getColumnName(clazz, prop.getName());
						switch(TypeUtils.getMappedType(prop)){
							case Long : 
								value = Long.valueOf(rs.getLong(fieldName));
								break;
							case Integer :
								value = Integer.valueOf(rs.getInt(fieldName));
								break;
							default :
								value = rs.getObject(fieldName);
								break;
						}
					}catch(Exception e){//如果仅仅查询Class的部分字段
						if(e.getMessage().matches("Column\\s+'.+?'\\s+not\\s+found.")){
							switch(TypeUtils.getMappedType(prop)){
								case Long : 
								case Integer: 
									value = 0; break;
								default :
									value = null;
									break;
							}
						}
					}
					prop.getWriteMethod().invoke(tt, new Object[] {value});
				} catch (IllegalArgumentException e) {
					System.out.print(prop.getName()+" ----	");
					System.out.println(rs.getInt(TUtils.getColumnName(clazz, prop.getName())));
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
//			if("id".equals(prop.getName().toLowerCase())){
//				return true;
//			}
		}
		return false;
	}
	
	/**
	 * 返回更新实体的命令语句
	 * @param <T>
	 * @param t
	 * @param token
	 */
	public static <T> String getUpdateSql(T t,DialectType DialectType) throws Exception{
		String token = Configuration.getToken(DialectType);
		StringBuffer sql = new StringBuffer("update ").append(TUtils.getTableName(t.getClass())).append(" set ");
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
			if(TUtils.isPrimaryKey(t.getClass(), props[i])){
				id = Long.valueOf(props[i].getReadMethod().invoke(t,(Object[]) null).toString());
			}else{
				boolean bool = false;
				switch(TypeUtils.getMappedType( props[i])){
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
						sql.append(token).append(TUtils.getColumnName(t.getClass(), props[i].getName()).toUpperCase()).append(token)
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
	
	// 获取插入记录的sql语句
	@SuppressWarnings("static-access")
	public static <T> String getInsertSql(T t, DialectType DialectType) throws Exception {
		String token = Configuration.getToken(DialectType);
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer("insert into ").append(
				TUtils.getTableName(t.getClass())).append(" ");
		
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		//k-v 映射的column名字 : 属性  LinkedHashMap 按照插入的顺序排序
		Map<String,PropertyDescriptor> propMap = new LinkedHashMap<String, PropertyDescriptor>();
		sql.append("(");
		
		for (int i = 0; i < props.length; i++) {
			Field field = t.getClass().getDeclaredField(props[i].getName());
			Column column = field.getAnnotation(Column.class);
			Transient nullMap = field.getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			Id id = field.getAnnotation(Id.class);
			if(id != null){//主键
				if(Configuration.dialect == DialectType.HSQLDB){
					continue;
				}
			}
			if (column != null) {// 数字 1 键旁边的反引号；处理关键字
				sql.append(token).append(column.name().toUpperCase()).append(token);
				propMap.put(column.name(), props[i]);
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
			if(TUtils.isPrimaryKey(t.getClass(), prop)){
				switch(DialectType){
				case MYSQL:
					sql.append("null");	
					break;
				case ORACLE:
					sql.append(TUtils.getSequenceName(t.getClass())+".nextval");
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
//						sql.append(" to_date('").append(value).append("','yyyy-MM-dd HH24:mi:ss') ");
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
	
	
	/**
	 * 组装update Sql语句
	 * @param t : 实体
	 * @param DialectType ： 指定该数据库的方言 {@link Configuration}
	 */
	public static <T> String getUpdateSql(T t) throws Exception{
		StringBuffer sql = new StringBuffer("update ").append(TUtils.getTableName(t.getClass())).append(" set ");
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
			if(TUtils.isPrimaryKey(t.getClass(), props[i])){
				id = Long.valueOf(props[i].getReadMethod().invoke(t,(Object[]) null).toString());
			}else{
				switch(TUtils.getMappedType( props[i].getPropertyType())){
					case Integer :
					case Long : 
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						if (value != null) {
							sql.append(props[i].getName().toUpperCase()).append("=").append(value);
						} else {
							continue;
						}
						break;
					case Date : // 对应java.util.Date类型
						value = TUtils.parseDate(props[i].getReadMethod().invoke(t,(Object[]) null));
						 if ("null".equals(value) || null == value) {
								continue;
						 } else {
								sql.append(TUtils.getColumnName(t.getClass(), props[i].getName()))
									.append("='").append(value.toString()).append("'");
						 }
						 break;
					case String :
						value = props[i].getReadMethod().invoke(t,(Object[]) null);
						 if ("null".equals(value) || null == value) {
							continue;
						} else {
							sql.append(TUtils.getColumnName(t.getClass(), props[i].getName()).toUpperCase())
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
	
	/**
	 * 获取插入记录的sql语句
	 * @param t : 实体
	 */ 
	public static <T> String getInsertSql(T t) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer("insert into ").append(
				TUtils.getTableName(t.getClass())).append(" ");
		
		BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		//k-v 映射的column名字 : 属性  LinkedHashMap 按照插入的顺序排序
		Map<String,PropertyDescriptor> propMap = new LinkedHashMap<String, PropertyDescriptor>();
		sql.append("(");
		
		GenerationType generationType = null;
		String seqName = null;
		for (int i = 0; i < props.length; i++) {
			Field field = t.getClass().getDeclaredField(props[i].getName());
			Column column = field.getAnnotation(Column.class);
			Transient nullMap = field.getAnnotation(Transient.class);
			if(nullMap != null){
				continue;
			}
			Id id = field.getAnnotation(Id.class);
			if(id != null){//主键
				//如果是自增主键则不对其进行处理
				GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
				if(gv == null){
					throw new Exception("未指定主键生成策略");
				}
				generationType = gv.strategy();
				switch(generationType){
				case IDENTITY : continue;
				case SEQUENCE :
					SequenceGenerator generator = field.getAnnotation(SequenceGenerator.class);
					seqName = generator.sequenceName();
					break;
				}
			}
			if (column != null) {// 数字 1 键旁边的反引号；处理关键字
				sql.append(column.name());
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
			if(TUtils.isPrimaryKey(t.getClass(), prop)){
				
				switch(generationType){
				case AUTO: sql.append("null"); break;
				case IDENTITY : break;
				case SEQUENCE :	sql.append(seqName+".nextval"); break;
				}
			}else{
				switch(TUtils.getMappedType(prop.getPropertyType())){
					case Integer :
					case Long :
						sql.append(prop.getReadMethod().invoke(t,(Object[]) null));
						break;
					case Date :
						value = TUtils.parseDate(prop.getReadMethod().invoke(t,(Object[]) null));
						if(Configuration.dialect == DialectType.ORACLE){
							sql.append(" to_date('").append(value).append("','yyyy-MM-dd HH24:mi:ss') ");
						}else{
							sql.append(null == value ? "null" : "'" + value.toString() + "'");
						}
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
	/**
	public static <T> String getSequenceName(T t,PropertyDescriptor prop){
		String seqName = "";
		try {
			SequenceGenerator generator =  t.getClass().getDeclaredField(prop.getName())
						.getAnnotation(SequenceGenerator.class);
			seqName = generator.sequenceName();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return seqName;
	}
	/**
	 * 获取Field的类型
	 */ 
	public static <T> MappedType getMappedType(Class<?> fieldType){
		String typeName = fieldType.getSimpleName().toLowerCase();
		if(typeName.contains("long")){
			return MappedType.Long;
		}
		if(typeName.contains("int")){
			return MappedType.Integer;
		}
		if(typeName.equalsIgnoreCase("date")){
			return MappedType.Date;
		}
		return MappedType.String;
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
}
