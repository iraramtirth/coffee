package org.coffee.hibernate.dao.oracle.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.coffee.hibernate.SqlConnection;
import org.coffee.hibernate.dao.TDao;
import org.coffee.hibernate.dao.util.OracleTDaoUtil;
import org.coffee.hibernate.dao.util.TDaoUtil;

import com.sun.rowset.CachedRowSetImpl;

/**
 * 系统DAO的公共/通用父类接口 实现类
 * 
 * @author wangtao
 * 
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class OracleTDaoImpl implements TDao {

	private Connection conn;

	public OracleTDaoImpl() {
		
	}
	/**
	 * 删除实体
	 */
	@Override
	public <T> void delete(Class<T> clazz, long id) throws SQLException {
		try {
			conn = new SqlConnection().getConnection();
			String sql = "delete from " + TDaoUtil.getTableName(clazz) + " where id = " + id;
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
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(OracleTDaoUtil.getInsertSql(t));
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
		try{
			String sql = OracleTDaoUtil.getUpdateSql(t);
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
			String sql = "select * from " + OracleTDaoUtil.getTableName(clazz) + " where id = " + id;
			Statement stmt = conn.createStatement();
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
		OracleTDaoImpl tdao = new OracleTDaoImpl();
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
