package org.coffee.hibernate;

import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

/**
 * 系统DAO的公共/通用父类接口 
 * @author wangtao
 */
public interface TDao {
	public <T> void  insert(T t) throws SQLException;
	public <T> void update(T t) throws SQLException;
	public <T> void delete(Class<T> clazz, long id) throws SQLException;
	public <T> void deleteBatch(Class<T> clazz, String[] ids) throws SQLException;
	
	// 返回Integer Long String 等类型
	public <T> T queryForObject(Class<T> clazz,String sql) throws SQLException;
	// 返回自定义Object
	public <T> T queryForObject(Class<T> clazz, long id) throws SQLException;
	public <T> List<T> queryForList(String sql, long offset, int size,Class<T> clazz) throws SQLException;
	public <T> List<T> queryForList(String sql,Class<T> clazz) throws SQLException;
	public CachedRowSet queryForResultSet(String sql) throws SQLException;
	
	public int executeUpdate(String sql) throws SQLException;
	
	public void close() throws SQLException;
}
