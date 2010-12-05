package org.coffee.hibernate.dao.mysql.impl;

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
import org.coffee.hibernate.dao.impl.TDaoImpl;
import org.coffee.hibernate.dao.util.MysqlDaoUtil;
import org.coffee.hibernate.dao.util.TDaoUtil;
import org.coffee.spring.ioc.annotation.Repository;

import com.sun.rowset.CachedRowSetImpl;

/**
 * 系统DAO的公共/通用父类接口 实现类
 * 
 * @author wangtao
 * 
 * @version 1.0
 */
@Repository(name="mysqlDao")
public class MysqlDaoImpl extends TDaoImpl {
	private Connection conn;

	public MysqlDaoImpl() {
		conn = new SqlConnection().getConnection();
	}
 
	@Override
	public <T> void insert(T t) throws SQLException {
		if(t == null){
			throw new SQLException("插入数据失败，实体为null");
		}
		try {
			String sql = MysqlDaoUtil.getInsertSql(t);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> void update(T t) throws SQLException {
		try{	
			String sql = MysqlDaoUtil.getUpdateSql(t); 
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			conn.close();
		}
	}
	/**
	 *  返回离线数据集
	 */
	@Override
	public CachedRowSet queryForResultSet(String sql) throws SQLException {
		CachedRowSet crs = new CachedRowSetImpl();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			crs.populate(rs);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
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
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * 查询单个实体
	 */
	@Override
	public <T> T queryForObject(Class<T> clazz, long id) throws SQLException {
		try {
			BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			String sql = "select * from " + TDaoUtil.getTableName(clazz) + " where id = " + id;
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			T t = clazz.newInstance();
			if (rs.next()) {
				for (PropertyDescriptor p : props) {
					p.getWriteMethod().invoke(t,new Object[] { rs.getObject(p.getName()) });
				}
			}
			rs.close();
			stmt.close();
			return t;
		} catch (Exception e) {
			e.printStackTrace();
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
			BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				T tt = clazz.newInstance();
				for (PropertyDescriptor p : props) {
					try {
						p.getWriteMethod().invoke(tt,new Object[] { rs.getObject(p.getName())});
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
				ls.add(tt);
			}
			rs.close();
			stmt.close();
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> List<T> queryForList(String sql, long start, int size,
			Class<T> clazz) throws SQLException {
		
		return null;
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
//		User user = new User();
//		user.setAge(17);
//		user.setBirthday(new Date());
//		user.setUser_name("ssss");
//		new MysqlDaoImpl().insert(user);
//		
//		Integer id = 0 ;
//		System.out.println(id.getClass().getCanonicalName());
	}

}