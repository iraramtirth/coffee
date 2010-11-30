package org.coffee.hibernate.dao.oracle.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.coffee.hibernate.SqlConnection;
import org.coffee.hibernate.annotation.Column;
import org.coffee.hibernate.annotation.Entity;
import org.coffee.hibernate.annotation.Table;
import org.coffee.hibernate.dao.TDao;

import com.sun.rowset.CachedRowSetImpl;

/**
 * 系统DAO的公共/通用父类接口 实现类
 * 
 * @author wangtao
 * 
 * @version 1.0
 */
public class OracleDaoImpl implements TDao {

	private Connection conn;

	private String tableName = ""; 	//表名
	private String sequenceName = "";// 表对应的序列
	public OracleDaoImpl() {
		
	}
	
	// 反射获取实体的注解信息：表名、序列号
	private <T> void setTableNameOrSequenceName(T t){
		//Class<?> clazz = Class.forName(t.getClass().getName());
		if(t.getClass().getAnnotation(Entity.class) != null 
				&& t.getClass().getAnnotation(Table.class) != null){
			Table tableAnno = t.getClass().getAnnotation(Table.class); 
			tableName = tableAnno.name().toLowerCase();
			sequenceName = tableAnno.sequence().toLowerCase();
		}else{
			tableName = t.getClass().getSimpleName().toLowerCase(); 
		}
	}
	
	/**
	 * 删除实体
	 */
	@Override
	public <T> void delete(Class<T> clazz, long id) throws SQLException {
		try {
			conn = new SqlConnection().getConnection();
			this.setTableNameOrSequenceName(clazz.newInstance());
			String sql = "delete from " + this.tableName + " where id = " + id;
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
	}
	/**
	 *  插入实体
	 */
	@Override
	public <T> void insert(T t) throws SQLException {
		conn = new SqlConnection().getConnection();
		if(t == null){
			throw new SQLException("插入数据失败，实体为null");
		}
		/**
		 * 如果实体通过 Entity 跟 Table同时注解了(二者缺一不可)
		 */
		try {
			// 获取Java泛型的注解信息(表名，序列号)
			this.setTableNameOrSequenceName(t);
			
			StringBuffer sql = new StringBuffer("insert into ").append(tableName).append(" ");
			
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			
			sql.append(" (");
			for (int i = 0; i < props.length; i++) {
				Column column = props[i].getReadMethod().getAnnotation(Column.class);
				// 通过注解配置字段信息
//				Column column = clazz.getField(props[i].getName()).getAnnotation(Column.class);
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
						// 处理 ID 主键
						if ("id".equals(props[i].getName())) {
							sql.append(this.sequenceName+".nextval");
						} else {
							sql.append(props[i].getReadMethod().invoke(t,
									(Object[]) null));
						}
					} else if (props[i].getPropertyType().getCanonicalName()
							.endsWith("Date")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String value = "";
						try {
							value = sdf.format(props[i].getReadMethod().invoke(
									t, (Object[]) null));
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
			Statement stmt = conn.createStatement();
			System.out.println(sql.toString());
			stmt.executeUpdate(sql.toString());
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
	}

	@Override
	public <T> void update(T t) throws SQLException {
		conn = new SqlConnection().getConnection();
		
		this.setTableNameOrSequenceName(t);
		
		StringBuffer sql = new StringBuffer("update ").append(
				this.tableName).append(" set ");
		long id = 0;
		try {
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
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						try {
							value = sdf.format(props[i].getReadMethod().invoke(
									t, (Object[]) null));
						} catch (Exception e) {
							value = "null";
						}
						if ("null".equals(value) || null == value) {
							continue;
						} else {// mysql
//							sql.append(props[i].getName()).append(" = '")
//									.append(value).append("' ");
							sql.append(props[i].getName()).append(" = ").append("to_date('").append(value).append("','yyyy-MM-dd HH24:mi:ss') ");
						}
					} else {
						value = props[i].getReadMethod().invoke(t,
								(Object[]) null);
						if (value == null) {
							continue;
						} else {
							sql.append(props[i].getName()).append(" = '")
									.append(value.toString()).append("' ");
						}
					}
					if (value != null && i + 1 < props.length) {
						sql.append(",");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String str = sql.toString().trim();
			// 出去去 末尾的 ,
			while (str.trim().endsWith(",")) {
				str = str.substring(0, str.length() - 1);
			}
			str += " where id = " + id;
			System.out.println(str);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(str);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
	}
	/**
	 *  返回离线数据集
	 */
	@Override
	public CachedRowSet queryForResultSet(String sql) throws SQLException {
		conn = new SqlConnection().getConnection();
		CachedRowSet crs = new CachedRowSetImpl();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			crs.populate(rs);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return crs;
	}
	/**
	 *  查询总记录数
	 *  sql = "select count(*) from ..."
	 */
	@Override
	public int queryForCount(String sql) throws SQLException {
		int count = 0;
		try {
			conn = new SqlConnection().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return count;
	}

	/**
	 * 查询单个实体
	 */
	@Override
	public <T> T queryForObject(Class<T> clazz, long id) throws SQLException {
		try {
			conn = new SqlConnection().getConnection();
			this.setTableNameOrSequenceName(clazz.newInstance());
			String sql = "select * from " + this.tableName + " where id = " + id;
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			T t = this.processresultSetToObject(rs,clazz);
			rs.close();
			stmt.close();
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return null;
	}

	/**
	 *  查询返回list
	 */
	@Override
	public <T> List<T> queryForList(String sql, Class<T> clazz)
			throws SQLException {
		List<T> ls = new ArrayList<T>();
		try {
			conn = new SqlConnection().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			this.processresultSetToList(rs, clazz);
			stmt.close();
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return null;
	}

	@Override
	public <T> List<T> queryForList(String sql, long start, int size,
			Class<T> clazz) throws SQLException {
		List<T> ls = new ArrayList<T>();
		try {
			conn = new SqlConnection().getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stmt.setMaxRows((int)(start + size));
			ResultSet rs = stmt.executeQuery(sql);
			// 设置分页
			rs.first();
			rs.relative((int)start - 1);
			// 处理resultSet 实现分页查询
			ls = this.processresultSetToList(rs,clazz);
			stmt.close();
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return null;
	}
	
	private <T> List<T> processresultSetToList(ResultSet rs,Class<T> clazz) throws Exception{
		List<T> ls = new ArrayList<T>();
		BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
		PropertyDescriptor[] props = bi.getPropertyDescriptors();
		while (rs.next()) {
			T tt = clazz.newInstance();
			for (PropertyDescriptor p : props) {
				try {
					/**
					 * 如果Oracle数据库中的类型是 number
					 * p.getWriteMethod().invoke(tt, new Object[] { rs.getObject(p.getName()) })
					 * 会报如下错误
					 * java.lang.IllegalArgumentException: argument type mismatch
					 */ 
					if("java.lang.Long".equals(p.getPropertyType().getName())){
						p.getWriteMethod().invoke(tt, new Object[] { Long.valueOf(rs.getLong(p.getName())) });
					}else{
						p.getWriteMethod().invoke(tt, new Object[] { rs.getObject(p.getName()) });
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			ls.add(tt);
		}
		rs.close();
		return ls;
	}
	
	
	private <T> T processresultSetToObject(ResultSet rs,Class<T> clazz){
		try {	
			T t = clazz.newInstance();
			BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			if (rs.next()) {
				for (PropertyDescriptor p : props) {
					if("java.lang.Long".equals(p.getPropertyType().getName())){
						p.getWriteMethod().invoke(t, new Object[] { Long.valueOf(rs.getLong(p.getName())) });
					}else{
						p.getWriteMethod().invoke(t, new Object[] { rs.getObject(p.getName()) });
					}
				}
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	@Override
	public int executeUpdate(String sql) throws SQLException {
		try {
			conn = new SqlConnection().getConnection();
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return 0;
	}

	/**
	 * 关闭数据库连接
	 */
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public Connection getConnection(){
		return conn;
	}
	public static void main(String[] args) throws Exception {
		String sql = "select * from activities";
		OracleDaoImpl tdao = new OracleDaoImpl();
		CachedRowSet rs = tdao.queryForResultSet(sql);
		tdao.conn.setAutoCommit(false);
		if(rs.next()){
			System.out.println(rs.getString(2));
			rs.updateString(2, "sddssd");
			rs.updateRow();
		}
		rs.acceptChanges(tdao.getConnection());
		rs.close();
	}

}
