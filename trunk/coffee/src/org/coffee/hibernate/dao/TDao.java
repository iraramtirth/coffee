package org.coffee.hibernate.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

/**
 * 系统DAO的公共/通用父类接口 
 * @author wangtao
 */
public interface TDao {
	/**
	 * 
	 * @param <T> 传入一Model对象，
	 * 			其中Model名称跟field名称分别对应数据表的表名跟字段名
	 * @param t 
	 * @throws SQLException
	 */
	public <T> void  insert(T t) throws SQLException;
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @throws SQLException
	 */
	public <T> void update(T t) throws SQLException;
	/**
	 * 删除对象实体
	 * @param <T>
	 * @param 传入一个待查询的实体对象类型，该类型必须存在ID
	 * @throws SQLException
	 */
	public <T> void delete(T t) throws SQLException;
	
	public <T> void delete(Class<T> clazz, long id) throws SQLException;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public CachedRowSet queryForResultSet(String sql) throws SQLException;
	/**
	 * 获得数量用于建立分页信息
	 * @return
	 * @throws SQLException
	 */
	public int queryForCount(String sql) throws SQLException;
	/**
	 * 返回一个查询列表
	 * @param <T>
	 * @param sql
	 * @param t
	 * @return
	 */
	public <T> List<T> queryForList(String sql,Class<T> clazz) throws SQLException;
	/**
	 * 返回一个查询对象
	 * @param <T>
	 * @param t 传入一个待查询的实体对象类型，该类型必须存在ID
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForobject(T t) throws SQLException;
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForobject(Class<T> clazz, long id) throws SQLException;
//	public <T> T queryForobjectTwo(Class<T> clazz, long id) throws SQLException;
	
	/**
	 * 执行指定sql语句
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException;
	
}
